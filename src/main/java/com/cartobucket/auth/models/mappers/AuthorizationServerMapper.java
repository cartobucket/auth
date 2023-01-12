package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.AuthorizationServerRequest;
import com.cartobucket.auth.model.generated.AuthorizationServerResponse;
import com.cartobucket.auth.models.AuthorizationServer;

import java.net.MalformedURLException;
import java.net.URL;

public class AuthorizationServerMapper {
    public static AuthorizationServer from(AuthorizationServerRequest request) {
        var authorizationServer = new AuthorizationServer();
        try {
            authorizationServer.setServerUrl(new URL(request.getServerUrl()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        authorizationServer.setName(request.getName());
        authorizationServer.setAudience(request.getAudience());
        authorizationServer.setAuthorizationCodeTokenExpiration(Long.valueOf(request.getAuthorizationCodeTokenExpiration()));
        authorizationServer.setClientCredentialsTokenExpiration(Long.valueOf(request.getClientCredentialsTokenExpiration()));
        return authorizationServer;
    }

    public static AuthorizationServerResponse toResponse(AuthorizationServer authorizationServer) {
        var response = new AuthorizationServerResponse();
        response.setId(String.valueOf(authorizationServer.getId()));
        response.setName(authorizationServer.getName());
        response.setServerUrl(String.valueOf(authorizationServer.getServerUrl()));
        response.setAudience(authorizationServer.getAudience());
        response.setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration().intValue());
        response.setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration().intValue());
        return response;
    }
}
