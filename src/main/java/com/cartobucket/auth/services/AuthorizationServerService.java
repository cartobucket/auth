package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.SigningKey;
import org.jose4j.jwt.JwtClaims;

import java.util.UUID;


public interface AuthorizationServerService {
    // This method returns the actual AuthorizationServer object as it is a dependency of the rest of the system.
    AuthorizationServer getAuthorizationServer(UUID authorizationServerId);
    JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer);
    JWKS getJwksForAuthorizationServer(UUID authorizationServerId);
    AuthorizationServerResponse createAuthorizationServer(AuthorizationServerRequest authorizationServerRequest);
    AuthorizationServerResponse updateAuthorizationServer(UUID authorizationServerId, AuthorizationServerRequest authorizationServerRequest);
    AuthorizationServersResponse getAuthorizationServers();
    void deleteAuthorizationServer(UUID authorizationServerId);
    String renderLogin(UUID authorizationServerId);
    SigningKey getSigningKeysForAuthorizationServer(AuthorizationServer authorizationServer);
    JwtClaims validateJwtForAuthorizationServer(AuthorizationServer authorizationServer, String Jwt);
}
