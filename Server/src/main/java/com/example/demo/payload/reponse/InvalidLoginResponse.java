package com.example.demo.payload.reponse;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Login";
        this.password = "Invalid Password";
    }
}
