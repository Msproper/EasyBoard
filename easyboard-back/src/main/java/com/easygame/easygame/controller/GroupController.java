package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.group.GroupRequestDTO;
import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;
    @PostMapping()
    public ResponseEntity<?> createGroup(
            @RequestBody @Valid GroupRequestDTO requestDTO
            ) {
        groupService.createGroup(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupCode(@PathVariable Long groupId){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
