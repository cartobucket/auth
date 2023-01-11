package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.AuthorizationServer;
import io.quarkus.qute.TemplateInstance;

import java.security.PrivateKey;
import java.util.UUID;


public interface AuthorizationServerService {
    // This method returns the actual AuthorizationServer object as it is a dependency of the rest of the system.
    AuthorizationServer getAuthorizationServer(UUID authorizationServerId);
    JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer);
    JWKS getJwksForAuthorizationServer(AuthorizationServer authorizationServer);
    PrivateKey getSingingKeyForAuthorizationServer(AuthorizationServer authorizationServer);
    AuthorizationServerResponse createAuthorizationServer(AuthorizationServerRequest authorizationServerRequest);
    AuthorizationServerResponse updateAuthorizationServer(UUID authorizationServerId, AuthorizationServerRequest authorizationServerRequest);
    AuthorizationServersResponse getAuthorizationServers();
    void deleteAuthorizationServer(UUID authorizationServerId);
    TemplateInstance renderLogin();
}
