package com.easygame.easygame.security;

import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.model.UsersDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${token.expiration-time.access}")
    private long accessExpiration;
    @Getter
    @Value("${token.expiration-time.refresh}")
    private long refreshExpiration;
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Извлечение имени пользователя из токена
     *
     * @param token токен
     * @return имя пользователя
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Получение access токен
     *
     * @param userDetails данные пользователя
     * @return access токен
     */
    public String generateAccessToken(UserDetails userDetails) {
        Date expirationDate = new Date(System.currentTimeMillis() + accessExpiration);
        return generateToken(userDetails, expirationDate);
    }

    /**
     * Получение refresh токена
     *
     * @param userDetails данные пользователя
     * @return refresh токен
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Date expirationDate = new Date(System.currentTimeMillis() + refreshExpiration);
        return generateToken(userDetails, expirationDate);
    }


    /**
     * Генерация токена
     *
     * @param userDetails данные пользователя
     * @return токен
     */
    public String generateToken(UserDetails userDetails, Date expirationDate) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof UserModel customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails, expirationDate);
    }

    /**
     * Проверка токена на валидность
     *
     * @param token       токен
     * @param userDetails данные пользователя
     * @return true, если токен валиден
     */
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String userName = extractUserName(token);
//        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }

    /**
     * Проверка токена на просроченность
     *
     * @param token токен
     * @return true, если токен просрочен
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /**
     * Извлечение данных из токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения данных
     * @param <T>             тип данных
     * @return данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерация токена
     *
     * @param extraClaims дополнительные данные
     * @param userDetails данные пользователя
     * @return токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Date expirationDate) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Извлечение даты истечения токена
     *
     * @param token токен
     * @return дата истечения
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлечение всех данных из токена
     *
     * @param token токен
     * @return данные
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получение ключа для подписи токена
     *
     * @return ключ
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Создает Principal из JWT токена
     * @param token JWT токен
     * @return объект Authentication (реализация Principal)
     */
    public Authentication getPrincipalFromToken(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();

        // Извлекаем роли из claims (предполагаем, что роли хранятся в claim "role")
        List<String> roles = claims.get("role") != null ?
                (claims.get("role") instanceof List ?
                        (List<String>) claims.get("role") :
                        Collections.singletonList(claims.get("role").toString())) :
                Collections.emptyList();

        // Преобразуем роли в GrantedAuthority
        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                username,
                null, // credentials (пароль) не нужен
                authorities
        );
    }

}