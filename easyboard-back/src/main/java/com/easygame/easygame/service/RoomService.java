package com.easygame.easygame.service;

import com.easygame.easygame.DTO.exception.NotFoundException;
import com.easygame.easygame.DTO.room.RoomPermissionDTO;
import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.redis.model.Room;
import com.easygame.easygame.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final UserService userService;
    private final RoomRepository repository;

    public RoomPermissionDTO getPermissionLevel(String uuid){
        Room room = repository.findByUuid(uuid).orElseThrow(()->new NotFoundException("Указанная комната не была найдена"));
        UserModel userModel = userService.getCurrentUser();
        return new RoomPermissionDTO(room.getMembers().get(userModel.getUsername()));
    }

    public RoomPermissionDTO updatePermissionLevel(String uuid, RoomPermissionDTO permissionDTO){
        Room room = repository.findByUuid(uuid).orElseThrow(()->new NotFoundException("Указанная комната не была найдена"));
        UserModel userModel = userService.getCurrentUser();
        room.getMembers().put(userModel.getUsername(), permissionDTO.getPermissionLevel());
        repository.save(room);
        return new RoomPermissionDTO();
    }

    private static String getRedisKey(String boardId) {
        return "Room:" + boardId;
    }
}
