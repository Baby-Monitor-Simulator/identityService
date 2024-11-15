package com.babymonitor.identity.services;

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
            return authorizationHeader.substring(7);
        }
        return null;
    }

    // Verifieer de gebruikersnaam en het wachtwoord
    public String authenticateWithKeycloak(String username, String password) {
        // Bouw de URL voor het token endpoint
        String tokenEndpoint = KeycloakUriBuilder.fromUri(authServerUrl)
                .path("/realms/" + realm + "/protocol/openid-connect/token")
                .build()
                .toString();

        // Maak een POST-verzoek naar Keycloak om de gebruiker te verifiëren
        try {
            // Stel de body in voor de password grant
            String body = "grant_type=" + OAuth2Constants.PASSWORD +
                    "&client_id=" + clientId +
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
                    AccessTokenResponse tokenResponse = objectMapper.readValue(response.getEntity().getContent(), AccessTokenResponse.class);

                    // Als het verzoek succesvol is, retourneer dan het JWT-token
                    return tokenResponse.getToken();  // Het token is nu geldig voor deze gebruiker
                } else {
                    return null;  // Fout bij inloggen
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Fout bij de aanvraag
        }
    }
}
