package com.babymonitor.identity.models;


import java.util.HashSet;
import java.util.Set;

//@Entity
public class User {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String requesttest;
    //@ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.requesttest = "top het werkt user is aangemaakt";
    }

    public User() {

    }
}

