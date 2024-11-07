package com.babymonitor.identity.controllers;

import com.babymonitor.identity.models.LoginRequest;
import com.babymonitor.identity.models.User;
import com.babymonitor.identity.models.UserDTO;
import com.babymonitor.identity.services.JwtAuthConverter;
import com.babymonitor.identity.services.UserCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
public class AuthController {



    @Autowired
    private JwtAuthConverter tokenProvider;

    private final UserCreation userCreation;


    public AuthController(UserCreation userCreation) {
        this.userCreation = userCreation;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

       User user = new User(loginRequest.getUsername(), loginRequest.getPassword());
       return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        String userId = userCreation.createUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
        if (userId != null) {
            return ResponseEntity.ok("User created with ID: " + userId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }

    // Register method omitted for brevity
}