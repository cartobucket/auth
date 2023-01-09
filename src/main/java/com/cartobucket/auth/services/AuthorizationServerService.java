package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.AuthorizationServer;

import java.security.PrivateKey;
import java.util.UUID;


public interface AuthorizationServerService {
    AuthorizationServer getDefaultAuthorizationServer();
    JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer);
    JWKS getJwksForAuthorizationServer(AuthorizationServer authorizationServer);
    PrivateKey getSingingKeyForAuthorizationServer(AuthorizationServer authorizationServer);
    AuthorizationServerResponse createAuthorizationServer(AuthorizationServerRequest authorizationServerRequest);
    AuthorizationServerResponse getAuthorizationServer(UUID authorizationServerId);
    AuthorizationServerResponse updateAuthorizationServer(UUID authorizationServerId, AuthorizationServerRequest authorizationServerRequest);
    AuthorizationServersResponse getAuthorizationServers();
    void deleteAuthorizationServer(UUID authorizationServerId);
}
