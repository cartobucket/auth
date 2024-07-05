package com.cartobucket.auth.authorization.server.routes.mappers;

import com.cartobucket.auth.data.domain.AccessToken;
import com.cartobucket.auth.model.generated.AccessTokenResponse;

public class AccessTokenResponseMapper {

    public static AccessTokenResponse toAccessTokenResponse(AccessToken accessToken) {
        var accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setAccessToken(accessToken.getAccessToken());
        accessTokenResponse.setExpiresIn(accessToken.getExpiresIn());
        accessTokenResponse.setTokenType(switch (accessToken.getTokenType()) {
            case BEARER -> AccessTokenResponse.TokenTypeEnum.BEARER;
        });
        accessTokenResponse.setScope(accessToken.getScope());
        accessTokenResponse.setRefreshToken(accessToken.getRefreshToken());
        accessTokenResponse.setIdToken(accessToken.getIdToken());
        return accessTokenResponse;
    }
}
