package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.JWKS;
import com.cartobucket.auth.model.generated.JWKSKeysInner;
import com.cartobucket.auth.models.AuthorizationServer;

import java.security.PrivateKey;


public interface AuthorizationServerService {
    AuthorizationServer getDefaultAuthorizationServer();
    JWKSKeysInner getJwkForAuthorizationServer(AuthorizationServer authorizationServer);
    JWKS getJwksForAuthorizationServer(AuthorizationServer authorizationServer);
    PrivateKey getSingingKeyForAuthorizationServer(AuthorizationServer authorizationServer);
}
