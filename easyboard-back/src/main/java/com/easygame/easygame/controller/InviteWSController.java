package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;


@RequiredArgsConstructor
@Tag(name = "АPI для создания комнат")
@Controller
public class InviteWSController {
    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/room/{roomId}/invite")
    public String handleBoardUpdate(
            Principal principal,
            @DestinationVariable Long roomId,
            @Payload InviteDTO inviteDto) {
        return "HELLO";
    }
}
