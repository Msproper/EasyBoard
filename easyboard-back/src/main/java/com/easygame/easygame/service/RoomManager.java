package com.easygame.easygame.service;

import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.model.BoardModel;
import com.easygame.easygame.redis.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RoomManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public Room findRoom(String boardId) {
        return (Room) redisTemplate.opsForValue().get(getRedisKey(boardId));
    }

    public String upsertRoom(BoardModel board, String username, PermissionLevel level) {
        final String redisKey = getRedisKey(board.getId().toString());
        Room room = findRoom(board.getId().toString());

        if (room == null) {
            room = new Room(board.getUuid(), username, new HashMap<>());
        }

        room.getMembers().put(username, level);
        redisTemplate.opsForValue().set(redisKey, room);

        return room.getUuid();
    }

    public String upsertRoom(BoardModel board, String username) {
        return upsertRoom(board, username, PermissionLevel.VIEWER);
    }

    public String getRedisKey(String boardId) {
        return "board:" + boardId;
    }

}

