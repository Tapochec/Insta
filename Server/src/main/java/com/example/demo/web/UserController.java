package com.example.demo.web;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.facade.UserFacade;
import com.example.demo.services.UserService;
import com.example.demo.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user") //Базовый URL-префикс для всех методов этого контроллера
@CrossOrigin //кросс-доменные запросы, чтобы API могли использоваться из разных доменов
public class UserController {

    @Autowired
    private UserService userService; //Сервис, который предоставляет бизнес-логику для работы с пользователями
    @Autowired
    private UserFacade userFacade; //преобразует из простого объекта в объект ДТО
    @Autowired
    private ResponseErrorValidation responseErrorValidation; //обрабатывает ошибки валидации, чё ещё

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal); //Получает текущего пользователя с помощью
        UserDTO userDTO = userFacade.UserToUserDTO(user); //преобразует его в ДТО

        return new ResponseEntity<>(userDTO, HttpStatus.OK); //возвращает дто
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.UserToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        User user = userService.updateUser(userDTO, principal);

        UserDTO userUpdated = userFacade.UserToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
}
