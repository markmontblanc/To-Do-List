package com.softserve.itacademy.todolist.dto;

public class AuthResponse {
    private String username;
    private String accessToken;

    public AuthResponse(String username, String jwtToken) {
        this.username = username;
        accessToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
