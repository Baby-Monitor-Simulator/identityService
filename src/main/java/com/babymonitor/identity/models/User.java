package com.babymonitor.identity.models;

import java.util.Set;

public class User {

    private String username;
    private String password;
    private String requesttest;
    private Set<Role> roles;

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.requesttest = "top het werkt user is aangemaakt";
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRequesttest() {
        return requesttest;
    }

    public void setRequesttest(String requesttest) {
        this.requesttest = requesttest;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}