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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final BoardService boardService;
    private final RoomManager roomManager;
    private final UserService userService;
    private final InviteRepository inviteRepository;
    private final InviteWebSocketService socketService;
    private static final Long TTL = 30L;
    private final Logger logger = LoggerFactory.getLogger(InviteService.class);

    public InviteDTO createPendingInvite(Long boardId) {
        final String sender = userService.getCurrentUser().getUsername();

        final BoardModel board = boardService.findById(boardId);
        final String boardTitle = board.getTitle();
        final String owner = board.getOwner().getUsername();

        if (inviteRepository.existsActiveInvite(sender, boardId.toString())) {
            throw new DuplicateInviteException("Приглашение уже отправлено. Подождите 30 секунд");
        }

        if (sender.equals(owner)) {
            return acceptInvite(board, sender, PermissionLevel.OWNER, boardTitle);
        }

        if (board.isUserBanned(userService.getCurrentUser())) {
            return InviteDTO.banned(sender, boardTitle);
        }

        if (board.isUserMember(userService.getCurrentUser())) {
            return acceptInvite(board, sender, PermissionLevel.EDITOR, boardTitle);
        }

        return sendAccessRequest(board, boardTitle, sender);
    }

    public void returnInviteResponse(InviteDTO inviteDTO) {
        final Invite invite = inviteRepository.findById(inviteDTO.getId()).orElseThrow(()-> new NotFoundException("Указанное приглашение не найдено"));
        String senderUsername = inviteDTO.getSender();
        UserModel sender = userService.getByUsername(senderUsername);
        Long boardId = invite.getBoardId();
        BoardModel board = boardService.findById(boardId);

        switch (inviteDTO.getStatus()) {
            case ACCEPTED -> handleAcceptedInvite(board, sender, inviteDTO);
            case BANNED -> handleBannedInvite(board, sender);
        }
        Room room = roomManager.findRoom(boardId.toString());
        inviteRepository.deleteById(invite.getId());
        socketService.notifyAdminsAndOwner(room, board.getOwner().getUsername(), inviteDTO);
        socketService.respondToSender(senderUsername, inviteDTO);
    }

    private void handleAcceptedInvite(BoardModel board, UserModel sender, InviteDTO invite) {
        invite.setUuid(roomManager.upsertRoom(board, sender.getUsername(), PermissionLevel.EDITOR));
        boardService.addMember(sender, board);
    }

    private void handleBannedInvite(BoardModel board, UserModel sender) {
        logger.warn("ADDBAN");
        boardService.addBan(sender,board);
    }

    private InviteDTO acceptInvite(BoardModel board, String username, PermissionLevel level, String boardTitle) {
        String uuid = roomManager.upsertRoom(board, username, level);
        return InviteDTO.accepted(username, boardTitle, uuid);
    }

    private InviteDTO sendAccessRequest(BoardModel board, String boardTitle, String sender) {

        Invite invite = new Invite(
                Invite.generateId(sender, board.getId().toString()),
                TTL,
                InviteStatus.PENDING,
                sender,
                board.getId()
        );
        InviteDTO inviteDTO = InviteDTO.pending(sender, boardTitle, invite.getId());
        inviteRepository.save(invite);
        Room room = roomManager.findRoom(board.getId().toString());
        if (room == null) socketService.sendInviteToOwner(board.getOwner().getUsername(), inviteDTO);
        else socketService.notifyAdminsAndOwner(room, board.getOwner().getUsername(), inviteDTO);
        return inviteDTO;
    }
}


