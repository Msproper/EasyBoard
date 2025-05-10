package com.easygame.easygame.security;

import com.easygame.easygame.DTO.auth.JwtAuthenticationResponse;
import com.easygame.easygame.DTO.auth.SignInRequest;
import com.easygame.easygame.DTO.auth.SignUpRequest;
import com.easygame.easygame.enums.Role;
import com.easygame.easygame.model.UsersDetails;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request, HttpServletResponse response) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        var userDetails = UsersDetails.builder().createAt(formattedDate).build();
        var user = UserModel.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .usersDetails(userDetails)
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var cookie = createCookie(jwtService.generateRefreshToken(user));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        var jwt = jwtService.generateAccessToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    private ResponseCookie createCookie(String refreshToken){
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
//                .secure(true) // Только для HTTPS
                .path("/")
                .maxAge(jwtService.getRefreshExpiration() / 1000) // В секундах
                .sameSite("Strict") // Защита от CSRF
                .build();

    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var cookie = createCookie(jwtService.generateRefreshToken(user));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        var jwt = jwtService.generateAccessToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse refreshAccessToken(String refreshToken, HttpServletResponse response){
        try{
        String username = jwtService.extractUserName(refreshToken);

        var user = userService
                .userDetailsService()
                .loadUserByUsername(username);

        var jwt = jwtService.generateAccessToken(user);
        return new JwtAuthenticationResponse(jwt);
        }
        catch (ExpiredJwtException e){
            deleteCookie(response);
            throw e;
        }
    }

    public void deleteCookie(HttpServletResponse response){
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0) // Удаление cookie
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }
}