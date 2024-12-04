package com.babymonitor.identity.controllers;

import com.babymonitor.identity.models.LoginRequest;
import com.babymonitor.identity.models.UserDTO;
import com.babymonitor.identity.services.JwtAuthConverter;
import com.babymonitor.identity.services.KeycloakService;
import com.babymonitor.identity.services.UserCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
public class AuthController {


    @Autowired
    private JwtAuthConverter authConverter;


    private final UserCreation userCreation;

    @Autowired
    private KeycloakService keycloakService;

    public AuthController(UserCreation userCreation) {
        this.userCreation = userCreation;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        String userId = userCreation.createUser(userDTO.getEmail(), userDTO.getPassword());
        if (userId != null) {
            return ResponseEntity.ok("User created with ID: " + userId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String jwtToken = keycloakService.authenticateWithKeycloak(
                    loginRequest.getEmail(), loginRequest.getPassword());

            if (jwtToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            return ResponseEntity.ok("Bearer " + jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occurred while logging in");
        }
    }
}