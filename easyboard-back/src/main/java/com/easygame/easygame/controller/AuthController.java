package com.easygame.easygame.controller;

import com.easygame.easygame.DTO.auth.JwtAuthenticationResponse;
import com.easygame.easygame.DTO.auth.SignInRequest;
import com.easygame.easygame.DTO.auth.UpdateResponse;
import com.easygame.easygame.DTO.exception.ExceptionResponse;
import com.easygame.easygame.DTO.exception.ValidationRuntimeException;
import com.easygame.easygame.security.AuthenticationService;
import com.easygame.easygame.DTO.auth.SignUpRequest;
import com.easygame.easygame.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;




@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "Получение access токена по refresh токену")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) return new ResponseEntity<>("Не авторизован", HttpStatus.UNAUTHORIZED);
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.refreshAccessToken(refreshToken, response);
        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
    }

    @Operation(summary = "Выход из аккаунта")
    @PostMapping("/logout")
    public ResponseEntity<?> refresh(HttpServletResponse response) {
        authenticationService.deleteCookie(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signUp(request, response);
        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest request, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) throw new ValidationRuntimeException(bindingResult);
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signIn(request, response);
        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.OK);
    }



    @Operation(summary = "Обновление пользователя")
    @GetMapping("/update")
    public ResponseEntity<?> update(){
        if (!userService.isAuthenticated()){
            return new ResponseEntity<>(new ExceptionResponse("пользователь не авторизован"), HttpStatus.UNAUTHORIZED);
        }
        UpdateResponse updateResponse = userService.update();
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }
}

