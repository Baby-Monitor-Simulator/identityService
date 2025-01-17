package com.babymonitor.identity.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private  String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private  String resourceId;


    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipalClaimName(jwt)
        );
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if(principleAttribute != null){
            claimName = principleAttribute;
        }
        return jwt.getClaim(claimName);
    };

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        // Eerste stap: Verkrijg rollen uit de 'resource_access' claim (al geïmplementeerd)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        // Verkrijg de rollen voor de resource 'realm-management' (deze resource kan variëren, pas het aan als nodig)
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Verkrijg de rollen uit 'realm_access' (deze claim bevat de standaard rollen zoals 'admin')
        if (jwt.getClaim("realm_access") != null) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            // Verkrijg de rollen van 'realm_access' en map ze naar GrantedAuthority
            if (realmAccess.containsKey("roles")) {
                Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
                authorities.addAll(realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Voeg de rol toe als "ROLE_<rolnaam>"
                        .collect(Collectors.toSet()));
            }
        }

        // Verkrijg de rollen uit de 'resource_access' claim zoals je eerder deed
        if (resourceAccess != null) {
            resourceAccess.forEach((key, value) -> {
                if (value instanceof Map) {
                    Map<String, Object> resource = (Map<String, Object>) value;
                    if (resource.containsKey("roles")) {
                        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
                        authorities.addAll(resourceRoles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toSet()));
                    }
                }
            });
        }

        return authorities;
    }
}
