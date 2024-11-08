package com.example.demo.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest { //передаем на сервис при попытке авторизации на сайте

    @NotEmpty(message = "error")
    private String username;

    @NotEmpty(message = "error")
    private String password;


}
