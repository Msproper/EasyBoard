package com.easygame.easygame.controllers;

import com.easygame.easygame.DTO.auth.JwtAuthenticationResponse;
import com.easygame.easygame.DTO.auth.SignUpRequest;
import com.easygame.easygame.controller.AuthController;
import com.easygame.easygame.security.AuthenticationService;
import com.easygame.easygame.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Отключаем фильтры для чистого теста
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signUp_ValidRequest_ReturnsJwtAuthenticationResponse() throws Exception {
        // Подготовка запроса
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUsername("testuser");

        // Ожидаемый ответ
        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
        jwtResponse.setToken("access-token");

        // Мокаем поведение
        Mockito.when(authenticationService.signUp(Mockito.any(SignUpRequest.class), Mockito.any(HttpServletResponse.class)))
                .thenReturn(jwtResponse);

        // Выполняем запрос
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }
}

