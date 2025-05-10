package com.easygame.easygame.redis.redisRepository;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserStatusRepository {
    void setUserOnline(String username);
    void setUserOffline(String username);
    boolean isUserOnline(String username);
    Map<String, String> getAllStatuses();
}