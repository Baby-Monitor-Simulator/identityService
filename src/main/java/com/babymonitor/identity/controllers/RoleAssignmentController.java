package com.babymonitor.identity.controllers;


import com.babymonitor.identity.services.KeycloakService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class RoleAssignmentController {

    @Autowired
    private KeycloakService keycloakService;

    public RoleAssignmentController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }


    @PostMapping("/{username}/roles/{role}")
    @PreAuthorize("hasRole('realm-admin')")
    public ResponseEntity<String> assignRole(
            @PathVariable String username,
            @PathVariable String role,
            HttpServletRequest request) {
        try {
            String response = keycloakService.assignRoleToUser(username, role, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fout bij het toewijzen van de rol, alleen Admins mogen rollen toewijzen: " + e.getMessage());
        }
    }
}
