package com.easygame.easygame.controller;

import com.easygame.easygame.redis.redisRepository.UserStatusRepository;
import com.easygame.easygame.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GlobalSocketController {
    private final UserService userService;

    private final UserStatusRepository statusRepo;
    private final SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        if (event != null){
            String username = event.getUser().getName();
            statusRepo.setUserOnline(username);
            System.out.println(simpUserRegistry.getUsers());
        }
    }

//    @EventListener
//    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
//        if (event != null) {
//            System.out.println("\n\n\n"+event+"\n\n\n");
//            String username = event.getUser().getName();
//            statusRepo.setUserOffline(username);
//        }
//    }

}

