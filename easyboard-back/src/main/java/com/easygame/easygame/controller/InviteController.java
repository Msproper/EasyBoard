package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.service.RoomService;
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
    private final RoomService roomService;

    @PostMapping("/{boardId}/sendRequest")
    public ResponseEntity<?> handleGetInvite(
            @PathVariable Long boardId
    ) {
        InviteDTO inviteResponse = roomService.createPendingInvite(boardId);
        return new ResponseEntity<>(inviteResponse, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/sendResponse")
    public ResponseEntity<?> handleBoardUpdate(
            @PathVariable Long boardId,
            @RequestBody InviteDTO payload
    ) {
        roomService.returnInviteResponse(boardId, payload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
