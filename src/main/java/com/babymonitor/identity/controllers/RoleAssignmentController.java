package com.babymonitor.identity.controllers;


import com.babymonitor.identity.models.RoleAssigning;
import com.babymonitor.identity.services.KeycloakService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/users")
public class RoleAssignmentController {

    @Autowired
    private KeycloakService keycloakService;

    public RoleAssignmentController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping("/role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> assignRole(
            @RequestBody RoleAssigning roleAssigning, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + authentication.getAuthorities());
        // Probeer daarna de oorspronkelijke logica:
        try {
            String response = keycloakService.assignRoleToUser(roleAssigning, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fout bij het toewijzen van de rol, alleen Admins mogen rollen toewijzen: " + e.getMessage());
        }
    }

    @GetMapping("/name/{userid}")
    public ResponseEntity<String> getUserName(@PathVariable UUID userid) {
        System.out.println("getting the name of the following userid"+ userid);
        String name = keycloakService.getNameByUserId(userid);

        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("Fout bij het ophalen van de naam");
        } else {
            return ResponseEntity.ok(name);
        }
    }
}
