package com.babymonitor.identity.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.HttpHeaders;

@Component
public class JwtTokenUtil {

    /**
     * Haalt het Bearer-token uit de Authorization-header van de HTTP-aanvraag.
     * Retourneert null als het token ontbreekt of niet geldig is.
     */
    public String extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
