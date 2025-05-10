package com.easygame.easygame.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.easygame.easygame.DTO.exception.ExceptionResponse;
import com.easygame.easygame.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            var authHeader = request.getHeader(HEADER_NAME);
            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token is missing");
                return;
            }

            // Обрезаем префикс и получаем имя пользователя из токена
            var jwt = authHeader.substring(BEARER_PREFIX.length());
            var username = jwtService.extractUserName(jwt);

            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService
                        .userDetailsService()
                        .loadUserByUsername(username);

                // Если токен валиден, то аутентифицируем пользователя
                if (!jwtService.isTokenExpired(jwt)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException authenticationException){
            handleAuthenticationException(authenticationException, response);
        } catch (AccessDeniedException accessDeniedException) {
            handleAccessDeniedException(accessDeniedException, response);
        } catch (Exception e) {
            handleOtherException(e, response);
        }
    }

    private void handleAuthenticationException(AuthenticationException exception, HttpServletResponse response) throws IOException {
        logger.warn("Неудачная попытка входа в аккаунт. Причина - " + exception.getMessage());
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
    }

    private void handleAccessDeniedException(AccessDeniedException exception, HttpServletResponse response) throws IOException {
        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
    }

    private void handleOtherException(Exception exception, HttpServletResponse response) throws IOException {
        logger.error("Ошибка в системе авторизации - {}", exception.getMessage());
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Security error");
    }


    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(errorMessage)
                .build();

        String jsonResponse = new ObjectMapper().writeValueAsString(exceptionResponse);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Список публичных эндпоинтов
        return path.equals("/api/auth/sign-up")
                || path.equals("/api/auth/sign-in")
                || path.equals("/api/auth/refresh")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/ws");
    }
}