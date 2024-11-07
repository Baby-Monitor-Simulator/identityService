package com.babymonitor.identity.services;

import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import java.util.Collections;

@Service
public class KeycloakService {

    @Value("${KEYCLOAK_AUTH_SERVER_URL}")
    private String authServerUrl;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String clientId;


    private Keycloak keycloak;

    public String assignRoleToUser(String username, String roleName, HttpServletRequest request) {
        try {
            String token = extractBearerToken(request);

            if (token == null) {
                throw new RuntimeException("JWT token ontbreekt in de Authorization header");
            }

            this.keycloak = Keycloak.getInstance(
                    authServerUrl,
                    realm,
                    clientId,
                    token
            );

            UsersResource usersResource = keycloak.realm(realm).users();
            UserRepresentation user = usersResource.search(username).stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Gebruiker niet gevonden"));

            RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();

            keycloak.realm(realm).users().get(user.getId()).roles().realmLevel().add(Collections.singletonList(role));

            return "Rol " + roleName + " succesvol toegewezen aan gebruiker " + username;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fout bij het toewijzen van de rol: " + e.getMessage();
        }
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Haal de token na "Bearer " op
        }
        return null;
    }
}
