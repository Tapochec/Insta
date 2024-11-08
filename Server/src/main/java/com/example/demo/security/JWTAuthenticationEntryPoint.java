package com.example.demo.security;

import com.example.demo.payload.reponse.InvalidLoginResponse;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint { // используется для обработки ошибок аутентификации
    private final HttpServletResponse httpServletResponse;

    public JWTAuthenticationEntryPoint(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    // Вызывается Spring Security, когда возникает ошибка аутентификации
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        InvalidLoginResponse loginResponse = new InvalidLoginResponse(); // Создает объект InvalidLoginResponse, который содержит информацию об ошибке аутентификации
        String jsonLoginResponse = new Gson().toJson(loginResponse); // Преобразует объект `InvalidLoginResponse` в JSON-строку с помощью `Gson`
        httpServletResponse.setContentType(SecurityConstants.CONTENT_TYPE); // Устанавливает заголовок
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value()); // Устанавливает статус ответа
        httpServletResponse.getWriter().println(jsonLoginResponse); // Пишет JSON-строку ответа в тело ответа
    }
}
