package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.Profile;

public interface AccessTokenService {
    AccessTokenResponse fromClientCredentials(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest);
    AccessTokenResponse fromAuthorizationCode(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest);
    AccessTokenResponse buildAccessToken(AuthorizationServer authorizationServer, Profile profile, ClientCode clientCode);

}
