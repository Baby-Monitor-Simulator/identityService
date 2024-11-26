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
}
