package com.easygame.easygame.redis.redisRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserStatusRepositoryImpl implements UserStatusRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String HASH_KEY = "user:status";

    @Override
    public void setUserOnline(String username) {
        redisTemplate.opsForHash().put(HASH_KEY, username, "online");
        redisTemplate.expire(HASH_KEY, 10, TimeUnit.MINUTES);
    }

    @Override
    public void setUserOffline(String username) {
        redisTemplate.opsForHash().put(HASH_KEY, username, "offline");
    }

    @Override
    public boolean isUserOnline(String username) {
        Object status = redisTemplate.opsForHash().get(HASH_KEY, username);
        return "online".equals(status);
    }

    @Override
    public Map<String, String> getAllStatuses() {
        return redisTemplate.<String, String>opsForHash()
                .entries(HASH_KEY)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)
                );
    }
}