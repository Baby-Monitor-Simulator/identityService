package com.babymonitor.identity.controllers;

import com.babymonitor.identity.models.LoginRequest;
import com.babymonitor.identity.models.User;
import com.babymonitor.identity.services.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity")
public class AuthController {



    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

       User user = new User(loginRequest.getUsername(), loginRequest.getPassword());
       return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
    }

    // Register method omitted for brevity
}