package com.easygame.easygame.service;

import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.enums.InviteStatus;
import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.redis.model.Room;
import com.easygame.easygame.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final RoomRepository repository;
    private final AccessInviteService accessInviteService;
    private final InviteService inviteService;

    public InviteDTO acceptAccessInviteByCode(String code){
        BoardModel board = accessInviteService.validateByCode(code).getBoard();
        return acceptAccessInvite(board);
    }

    public InviteDTO acceptAccessInviteByUUID(String uuid){
        BoardModel board = accessInviteService.validateByUuid(uuid).getBoard();
        return acceptAccessInvite(board);
    }

    private InviteDTO acceptAccessInvite(BoardModel board){
        var user = userService.getCurrentUser();
        inviteService.addFastMember(board);
        String uuid = createOrUpdateRoom(board, user.getUsername());

        return InviteDTO.builder().uuid(uuid).build();
    }

    public Room findRoom(String boardId) {
        return (Room) redisTemplate.opsForValue().get(getRedisKey(boardId));
    }

    public String createOrUpdateRoom(BoardModel board, String username){
        return createOrUpdateRoom(board, username, PermissionLevel.VIEWER);
    }

    public String createOrUpdateRoom(BoardModel board, String username, PermissionLevel level) {
        final String redisKey = getRedisKey(board.getId().toString());
        Room room = findRoom(redisKey);

        if (room == null) {
            room = new Room(board.getId().toString(), username, new HashMap<>(), board.getUuid());
        }

        room.getMembers().put(username, level);
        redisTemplate.opsForValue().set(redisKey, room);

        return room.getUuid();
    }

    public PermissionLevel getPermissionLevel(String uuid){
        Room room = repository.findByUuid(uuid).orElseThrow(()->new NotFoundException("Указанная комната не была найдена"));
        UserModel userModel = userService.getCurrentUser();
        return room.getMembers().get(userModel.getUsername());
    }

    private static String getRedisKey(String boardId) {
        return "board:" + boardId;
    }
}
