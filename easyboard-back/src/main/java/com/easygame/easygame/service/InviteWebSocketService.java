package com.easygame.easygame.service;

import com.easygame.easygame.DTO.room.InviteDTO;
import com.easygame.easygame.enums.PermissionLevel;
import com.easygame.easygame.redis.model.Room;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InviteWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(InviteWebSocketService.class);

    private static final String BOARD_REQUEST_TOPIC = "queue/invite.Requests";
    private static final String BOARD_RESPONSE_TOPIC = "queue/invite.Response";

    public void sendInviteToOwner(String ownerUsername, InviteDTO invite) {
        messagingTemplate.convertAndSendToUser(ownerUsername, BOARD_REQUEST_TOPIC, invite);
    }

    public void respondToSender(String senderUsername, InviteDTO invite) {
        messagingTemplate.convertAndSendToUser(senderUsername, BOARD_RESPONSE_TOPIC, invite);
    }

    public void notifyAdminsAndOwner(Room room, String ownerUsername, InviteDTO payload) {
        messagingTemplate.convertAndSendToUser(ownerUsername, BOARD_REQUEST_TOPIC, payload);
        if (room == null) return;

        room.getMembers().entrySet().stream()
                .filter(entry -> entry.getValue() == PermissionLevel.ADMIN)
                .forEach(entry -> messagingTemplate.convertAndSendToUser(
                        entry.getKey(),
                        BOARD_REQUEST_TOPIC,
                        payload
                ));
    }
}
