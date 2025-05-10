package com.easygame.easygame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final StringRedisTemplate template;

    public void publishToRoom(String roomId, String message) {
        template.convertAndSend("room:" + roomId + ":channel", message);
    }
}
