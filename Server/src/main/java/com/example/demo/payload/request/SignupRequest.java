package com.example.demo.payload.request;

import com.example.demo.annotations.PasswordMatches;
import com.example.demo.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class SignupRequest { //используется при попытке регистрации нового пользователя

    @Email(message = "Это не похоже на почту")
    @NotBlank(message = "Пользователь с таким емейлом уже существует")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Имя введи, дурень")
    private String firstname;
    @NotEmpty(message = "Фамилию введи, дурень")
    private String lastname;
    @NotEmpty(message = "Никнейм свой введи, дурень")
    private String username;
    @NotEmpty(message = "Введите пароль")
    private String password;
    private String confirmPassword;


}
