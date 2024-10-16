package com.babymonitor.identity.controllers;

import com.babymonitor.identity.models.LoginRequest;
import com.babymonitor.identity.models.User;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
public class AuthController {

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> authenticateUser(@RequestBody LoginRequest loginRequest) {

        User user = new User(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
                return ResponseEntity.ok("The identity server is up and running.");
    }
}
