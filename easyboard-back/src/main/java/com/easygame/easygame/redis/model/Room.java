package com.easygame.easygame.redis.model;

import com.easygame.easygame.enums.PermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.HashMap;
import java.util.Map;



@Data
@RedisHash("Room")
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Indexed
    @Id
    private String uuid;
    private String ownerUsername;
    private Map<String, PermissionLevel> members;

}
