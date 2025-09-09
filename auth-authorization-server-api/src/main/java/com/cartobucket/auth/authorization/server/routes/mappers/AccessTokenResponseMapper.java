package com.cartobucket.auth.authorization.server.routes.mappers;

import com.cartobucket.auth.data.domain.AccessToken;
import com.cartobucket.auth.authorization.server.dto.AccessTokenResponse;

public class AccessTokenResponseMapper {

    public static AccessTokenResponse toAccessTokenResponse(AccessToken accessToken) {
        var accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setAccessToken(accessToken.getAccessToken());
        accessTokenResponse.setExpiresIn(accessToken.getExpiresIn());
        // Convert domain enum to DTO enum using the string value
        if (accessToken.getTokenType() != null) {
            accessTokenResponse.setTokenType(
                AccessTokenResponse.TokenTypeEnum.fromValue(accessToken.getTokenType().value())
            );
        }
        
        // Only set optional fields if they have values
        if (accessToken.getScope() != null) {
            accessTokenResponse.setScope(accessToken.getScope());
        }
        if (accessToken.getRefreshToken() != null) {
            accessTokenResponse.setRefreshToken(accessToken.getRefreshToken());
        }
        if (accessToken.getIdToken() != null) {
            accessTokenResponse.setIdToken(accessToken.getIdToken());
        }
        
        return accessTokenResponse;
    }
}
