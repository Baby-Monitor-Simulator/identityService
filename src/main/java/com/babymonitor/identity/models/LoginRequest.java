package com.babymonitor.identity.models;

public class LoginRequest {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
