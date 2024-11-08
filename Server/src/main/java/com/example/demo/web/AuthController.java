package com.example.demo.web;

import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JWTTokenSuccessResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.JWTTokenProvider;
import com.example.demo.security.SecurityConstants;
import com.example.demo.services.UserService;
import com.example.demo.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController { // контроллер для обработки аутентификации и регистрации пользователей

    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping("/signin") // Обрабатывает POST-запрос на /api/auth/signin для аутентификации пользователя
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult); // для проверка валидности входящих данных (`loginRequest`)
        if (!ObjectUtils.isEmpty(errors)) {
            return errors; // возвращает ошибки валидации, если они есть
        }

        // Использует `authenticationManager` для аутентификации пользователя с помощью имени пользователя и пароля из `loginRequest`
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication); // Если аутентификация успешна, генерирует JWT-токен  и добавляет префикс

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt)); // Возвращает успешный HTTP-ответ (200 OK) с сгенерированным JWT-токеном в JSON-формате
    }


    @PostMapping("/signup") // Обрабатывает POST-запрос на `/api/auth/signup` для регистрации нового пользователя
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {


        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) { // Проверяет валидность входящих данных (`signupRequest`) и возвращает ошибки валидации, если они есть
            return errors;
        }

        userService.createUser(signupRequest); // Использует `userService.createUser` для создания нового пользователя в базе данных
        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегестрирован")); // Возвращает успешный HTTP-ответ (200 OK) с сообщением о успешной регистрации
    }

}
