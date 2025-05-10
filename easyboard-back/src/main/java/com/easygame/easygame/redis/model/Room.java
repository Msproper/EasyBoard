package com.easygame.easygame.redis.model;

import com.easygame.easygame.enums.PermissionLevel;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@RedisHash("Room")
public class Room {
    @Id
    private String boardId;

    private String ownerUsername;
    private Map<String, PermissionLevel> members = new HashMap<>();
    private UUID sharedAccessToken;
}
