package com.sp.entities;
public class LoginResponse {
    private boolean success;
    private String token;

    public LoginResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    // Getters and setters
}