package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.models.AuthorizationServer;

public interface AccessTokenService {
    AccessTokenResponse fromClientCredentials(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest);
    AccessTokenResponse fromAuthorizationCode(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest);
}
