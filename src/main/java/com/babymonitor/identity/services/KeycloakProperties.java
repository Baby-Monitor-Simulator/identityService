package com.babymonitor.identity.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProperties {

    @Value("${KEYCLOAK_AUTH_SERVER_URL}")
    private String authServerUrl;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_CLIENT_ID}")
    private String clientId;

    // Getters en setters
    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.authServerUrl = authServerUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
