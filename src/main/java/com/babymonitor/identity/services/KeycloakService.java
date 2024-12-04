package com.babymonitor.identity.services;

import com.babymonitor.identity.models.LoginRequest;
import com.babymonitor.identity.models.RoleAssigning;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import java.util.Collections;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.OAuth2Constants;

@Service
public class KeycloakService {

    private final KeycloakProperties keycloakProperties;
    private final JwtTokenUtil jwtTokenUtil;

    public KeycloakService(KeycloakProperties keycloakProperties, JwtTokenUtil jwtTokenUtil) {
        this.keycloakProperties = keycloakProperties;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String assignRoleToUser(RoleAssigning roleAssigning, HttpServletRequest request) {
        try {
            String token = jwtTokenUtil.extractBearerToken(request);
            if (token == null) {
                throw new RuntimeException("JWT token ontbreekt in de Authorization header");
            }

            Keycloak keycloak = Keycloak.getInstance(
                    keycloakProperties.getAuthServerUrl(),
                    keycloakProperties.getRealm(),
                    keycloakProperties.getClientId(),
                    token
            );

            UsersResource usersResource = keycloak.realm(keycloakProperties.getRealm()).users();
            UserRepresentation user = usersResource.get(roleAssigning.getUserID().toString()).toRepresentation();
            RoleRepresentation role = keycloak.realm(keycloakProperties.getRealm()).roles()
                    .get(roleAssigning.getRoleName()).toRepresentation();

            usersResource.get(user.getId()).roles().realmLevel().add(Collections.singletonList(role));

            return "Rol " + roleAssigning.getRoleName() + " succesvol toegewezen aan gebruiker " + roleAssigning.getUserID();
        } catch (Exception e) {
            e.printStackTrace();
            return "Fout bij het toewijzen van de rol: " + e.getMessage();
        }
    }

    public String authenticateWithKeycloak(String username, String password) {
        try {
            // Bouw de URL voor het token endpoint
            String tokenEndpoint = keycloakProperties.getAuthServerUrl() +
                    "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token";

            // Stel de body in voor de password grant
            String body = "grant_type=" + OAuth2Constants.PASSWORD +
                    "&client_id=" + keycloakProperties.getClientId() +
                    "&username=" + username +
                    "&password=" + password;

            // Verstuur de POST-verzoek
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(tokenEndpoint);
                post.setEntity(new StringEntity(body));
                post.setHeader("Content-Type", "application/x-www-form-urlencoded");

                HttpResponse response = client.execute(post);

                // Controleer de statuscode van de response
                if (response.getStatusLine().getStatusCode() == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    AccessTokenResponse tokenResponse = objectMapper.readValue(
                            response.getEntity().getContent(), AccessTokenResponse.class);

                    // Retourneer het JWT-token
                    return tokenResponse.getToken();
                } else {
                    throw new RuntimeException("Keycloak authentication failed: " +
                            response.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error during authentication with Keycloak", e);
        }
    }
}
