package com.easygame.easygame.redis.model;

import com.easygame.easygame.enums.InviteStatus;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash("Invite")
@AllArgsConstructor
@NoArgsConstructor
public class Invite {
    @Id
    private String id;  // Формат: "sender:roomId"

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl = 30L;
    private String boardTitle;
    private InviteStatus status;
    private String senderUsername;
    private String boardId;
    private String timestamp;



    // Генерация ключа
    public static String generateId(String sender, String boardId) {
        return String.format("%s:%s", sender, boardId);
    }
}
