package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.service.InviteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invites")
@Tag(name = "АPI для создания комнат")
public class InviteController {
    private final InviteService roomService;

    @PostMapping("/{boardId}/request")
    public ResponseEntity<?> handleGetInvite(
            @PathVariable Long boardId
    ) {
        InviteDTO inviteResponse = roomService.createPendingInvite(boardId);
        return new ResponseEntity<>(inviteResponse, HttpStatus.OK);
    }

    @PostMapping("/response")
    public ResponseEntity<?> handleBoardUpdate(
            @RequestBody InviteDTO payload
    ) {
        roomService.returnInviteResponse(payload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
