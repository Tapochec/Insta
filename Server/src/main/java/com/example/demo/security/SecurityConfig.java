package com.example.demo.security;

import com.example.demo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig { // класс конфигурации безопасности

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // аутентификация
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint) // настройка обработки исключений
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Использование сессий бесстатусных
                )
                .authorizeHttpRequests(auth -> // Настраивает авторизацию запросов
                        auth
                                .requestMatchers("/api/auth/signup", "/api/auth/**").permitAll() // Разрешение определенных путей без аутентификации
                                .anyRequest().authenticated() // Требование аутентификации для всех остальных путей
                );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Добавление фильтра JWT

        return http.build(); // Строим и возвращаем SecurityFilterChain
    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //Метод, который конфигурирует менеджер аутентификации
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder()); //Устанавливает CustomUserDetailsService как сервис
        // для получения информации о пользователях и bCryptPasswordEncoder() как шифратор паролей.
    }


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() { // Создает бин BCryptPasswordEncoder для шифрования паролей
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() { // Создает бин JWTAuthenticationFilter,
        // который будет использоваться для проверки JWT-токенов
    return new JWTAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception { // у меня там autowired не работал для AuthenticationManager, пришлось тут конструктор сделать
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
}

