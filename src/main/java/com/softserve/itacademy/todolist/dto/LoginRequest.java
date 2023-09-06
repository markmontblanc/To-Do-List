package com.softserve.itacademy.todolist.dto;

import lombok.Data;
import lombok.Getter;

public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
