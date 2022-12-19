package com.cartobucket.auth.services;

import com.cartobucket.auth.models.AuthorizationServer;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;

public interface AuthorizationServerService {
    AuthorizationServer getDefaultAuthorizationServer();
    JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer);
    JWSHeader getJwsHeaderForAuthorizationServer(AuthorizationServer authorizationServer, JWK jwk);
    JWKSet getJwksForAuthorizationServer(AuthorizationServer authorizationServer);
}
