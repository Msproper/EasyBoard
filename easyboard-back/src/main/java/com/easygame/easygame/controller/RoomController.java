package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.room.RoomPermissionDTO;
import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;
    @GetMapping("/userPermission/{uuid}")
    public ResponseEntity<?> getUserPermission(@PathVariable String uuid){
        RoomPermissionDTO roomPermissionDTO = roomService.getPermissionLevel(uuid);
        return new ResponseEntity<>(roomPermissionDTO, HttpStatus.OK);
    }

    @PostMapping("/userPermission/{uuid}")
    public ResponseEntity<?> setUserPermission(@PathVariable String uuid, @RequestBody RoomPermissionDTO roomPermissionDTO){
        roomService.updatePermissionLevel(uuid, roomPermissionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
