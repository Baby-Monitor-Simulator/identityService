package com.babymonitor.identity.services;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
public class UserCreation {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;  // Zorg ervoor dat deze waarde in je properties-bestand staat

    @Value("${keycloak.realm}")
    private String realm;  // Het realm waar je de gebruiker aan wilt toevoegen

    @Value("${keycloak.client-id}")
    private String clientId;  // Je Keycloak client ID



    private Keycloak getKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("TaskService")
                .clientId(clientId)
                .username("hassan")
                .password("hassan")
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    public String createUser(String username, String email, String password) {
            Keycloak keycloak = getKeycloakClient();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);

            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(password);
            cred.setTemporary(false);

            user.setCredentials(Collections.singletonList(cred));

            Response response = usersResource.create(user);
            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                response.close();
                return userId;
            } else {
                System.err.println("Fout bij het aanmaken van de gebruiker:" + response.getStatusInfo());
                response.close();
                return null;
            }
    }
}
