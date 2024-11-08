package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider { // реализация токена

    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class); // ошибки кладу сюда

    public String generateToken(Authentication authentication) { // Генерация JWT-токена для аутентифицированного пользователя
        User user = (User) authentication.getPrincipal(); //извлечение информации о пользователе из authentication

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME); // Устанавливает время истечения токена

        String userId = Long.toString(user.getId());
        Map<String, Object> claimsMap = new HashMap<>(); // собираем данные о пользователе в claimsMap
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getUsername());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());

        return Jwts.builder() // постройка токена
                .setSubject(userId) // устанавливает ID пользователя в качестве темы токена
                .addClaims(claimsMap) // добавляет данные о пользователе в качестве claims
                .setIssuedAt(now) // устанавливает время выдачи токена
                .setExpiration(expiryDate) // время истечения токена
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET) // подписывает токен с помощью секретного ключа алгоритмом `HS512`
                .compact(); // возвращает токен
    }

    public boolean validateToken(String token) { // Проверка валидности JWT-токена
        try {
            Jwts.parser() // парсит токен и проверяет секретный ключ
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex) {
            LOG.error(ex.getMessage());
            return false; // если проверка токена не прошла, возвращает фолс
        }
    }

    public Long getUserIdFromToken(String token) { // Извлечение ID пользователя из JWT-токена
        Claims claims = Jwts.parser() // Парсит JWT-токен, чтобы получить claims
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id"); // извлечение айди
        return Long.parseLong(id); // возвращает айди пользователя из токена
    }
}
