package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.board.AccessInviteDTO;
import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.service.AccessInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/fastConnect")
@RestController
public class AccessInviteController {
    private final AccessInviteService accessInviteService;

    @GetMapping("/{boardId}")
    private ResponseEntity<?> getFastInviteReq(@PathVariable Long boardId){
        AccessInviteDTO accessInvite = accessInviteService.generateAccessInvite(boardId);
        return new ResponseEntity<>(accessInvite, HttpStatus.OK);
    }

    @PostMapping("/code/{code}")
    private ResponseEntity<?> acceptInviteByCode(@PathVariable String code){
        InviteDTO invite = accessInviteService.acceptInviteByCode(code);
        return new ResponseEntity<>(invite, HttpStatus.OK);
    }

    @PostMapping("/uuid/{uuid}")
    private ResponseEntity<?> acceptInviteByUuid(@PathVariable String uuid){
        InviteDTO invite = accessInviteService.acceptInviteByUUID(uuid);
        return new ResponseEntity<>(invite, HttpStatus.OK);
    }
}
