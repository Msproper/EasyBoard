package com.easygame.easygame.service;

import com.easygame.easygame.DTO.exception.DuplicateInviteException;
import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.enums.InviteStatus;
import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.redis.model.Invite;
import com.easygame.easygame.redis.model.Room;
import com.easygame.easygame.repository.BoardRepository;
import com.easygame.easygame.repository.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final SimpUserRegistry simpUserRegistry;
    private final Long TTL = 30L;
    private final BoardRepository boardRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final InviteRepository inviteRepository;

    private static final String BOARD_REQUEST_TOPIC = "queue/invite.Requests";
    private static final String BOARD_RESPONSE_TOPIC = "queue/invite.Response";

    /**
     *
     * @param boardId - id доски, для которой создается комната
     * @return - объект данных, в котором хранится статус и уникальный id комнаты
     */
    public InviteDTO createPendingInvite(Long boardId) {
        final String sender = userService.getCurrentUser().getUsername();
        final BoardModel board = findBoardById(boardId);
        final String boardTitle = board.getTitle();
        final String ownerUsername = board.getOwner().getUsername();

        validateNoDuplicateInvite(sender, boardId.toString());

        if (sender.equals(ownerUsername)) {
            return acceptInvite(boardId.toString(), sender, PermissionLevel.OWNER, boardTitle);
        }

        if (board.isUserBanned(userService.getCurrentUser())) {
            InviteDTO invite = new InviteDTO();
            invite.setStatus(InviteStatus.BANNED);
            invite.setBoardTitle(boardTitle);
            return invite;
        }

        if (board.isUserMember(userService.getCurrentUser())) {
            return acceptInvite(boardId.toString(), sender, PermissionLevel.EDITOR, boardTitle);
        }
        return sendAccessRequest(board,boardTitle, sender);
    }

    public void returnInviteResponse(Long boardId, InviteDTO invite){
        String senderUsername = invite.getSender();
        UserModel sender = userService.getByUsername(senderUsername);
        BoardModel board = findBoardById(boardId);
        switch (invite.getStatus()){
            case ACCEPTED -> {
                invite.setUuid(
                        createOrUpdateRoom(boardId.toString(), senderUsername, PermissionLevel.EDITOR)
                                .toString());
                board.addMember(sender);
                boardRepository.save(board);
            }
            case BANNED -> {
                board.addBannedUser(sender);
                boardRepository.save(board);
            }
        }
        inviteRepository.deleteById(invite.getId());
        notifyAdmins(boardId.toString(), invite);
        messagingTemplate.convertAndSendToUser(senderUsername, BOARD_RESPONSE_TOPIC, invite);
    }

    /**
     * Метод поиска доски по указанному id
     *
     * @param boardId - id доски
     * @return - найденная доска
     */
    private BoardModel findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Указанная доска не была найдена"));
    }

    /**
     *
     * @param sender - отправитель
     * @param boardId - id доски
     */
    private void validateNoDuplicateInvite(String sender, String boardId) {
        if (inviteRepository.existsActiveInvite(sender, boardId)) {
            throw new DuplicateInviteException("Вы уже отправили приглашение. Дождитесь ответа или попробуйте через 30 секунд");
        }
    }

    private InviteDTO acceptInvite(String boardId, String username, PermissionLevel level, String boardTitle) {
        UUID uuid = createOrUpdateRoom(boardId, username, level);
        return new InviteDTO(InviteStatus.ACCEPTED, username,boardTitle,"",LocalDateTime.now().toString(),boardId, uuid.toString());
    }

    private InviteDTO sendAccessRequest(BoardModel board,String boardTitle,  String sender) {
        final String boardId = board.getId().toString();
        final String ownerUsername = board.getOwner().getUsername();
        final String id = Invite.generateId(sender, boardId);

        // Можно добавить сохранение invite, если оно нужно
        Invite invite = new Invite(
                id,
                TTL,
                boardTitle,
                InviteStatus.PENDING,
                sender,
                boardId,
                LocalDateTime.now().toString()
        );
        inviteRepository.save(invite);

        InviteDTO inviteDTO = new InviteDTO(invite);

        notifyAdmins(boardId, inviteDTO);

        messagingTemplate.convertAndSendToUser(ownerUsername, BOARD_REQUEST_TOPIC, inviteDTO);


        return inviteDTO;
    }

    private void notifyAdmins(String boardId, InviteDTO payload) {
        Room room = findRoom(getRedisKey(boardId));
        if (room == null) return;

        room.getMembers().entrySet().stream()
                .filter(entry -> entry.getValue() == PermissionLevel.ADMIN)
                .forEach(entry -> messagingTemplate.convertAndSendToUser(
                        entry.getKey(),
                        BOARD_REQUEST_TOPIC,
                        payload
                ));
    }

    private Room findRoom(String redisKey) {
        return (Room) redisTemplate.opsForValue().get(redisKey);
    }

    private UUID createOrUpdateRoom(String boardId, String username, PermissionLevel level) {
        final String redisKey = getRedisKey(boardId);
        Room room = findRoom(redisKey);

        if (room == null) {
            room = new Room(boardId, username, new HashMap<>(), UUID.randomUUID());
        }

        room.getMembers().put(username, level);
        redisTemplate.opsForValue().set(redisKey, room);

        return room.getSharedAccessToken();
    }

    private String getRedisKey(String boardId) {
        return "board:" + boardId;
    }
}
