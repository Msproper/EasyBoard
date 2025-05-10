package com.easygame.easygame.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Override
    public void run(String... args) {
        System.out.println("Redis host: " + redisHost);
        System.out.println("Redis port: " + redisPort);
    }
}