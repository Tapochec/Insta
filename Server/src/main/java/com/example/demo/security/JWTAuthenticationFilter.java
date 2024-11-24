package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;



    @Override
    // Проверяет наличие JWT-токена в запросе и, если токен валиден, устанавливает аутентификацию в контексте Spring Security
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(request); // Извлекает JWT-токен из заголовка `Authorization` запроса
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) { // Проверяет валидность токена
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt); // достает айди пользователя из токена
                User userDetails = customUserDetailsService.loadUserById(userId); // Загружает информацию о пользователе в бд

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList()
                ); // создание объекта аутентифицированного пользователя

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));;
                SecurityContextHolder.getContext().setAuthentication(authentication); // Устанавливает объект аутентификации в контекст Spring Security
        }
        } catch (Exception e) {
            LOG.error("Авторизация невозможна"); // ну ошибка, чё
        }

        filterChain.doFilter(request, response); // вызывает следующий фильтр в цепочке
    }

    private String getJWTFromRequest(HttpServletRequest request) { // Извлечение JWT-токена из заголовка запроса
        String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING); // Извлекает значение заголовка `Authorization`
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) { // Проверяет, начинается ли значение с префикса
            return bearerToken.split(" ")[1]; // извлеченный токен
        }
        return null;
    }
}
