package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.WellKnownApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WellKnown implements WellKnownApi {
    final AuthorizationServerService authorizationServerService;

    public WellKnown(AuthorizationServerService authorizationServerService) {
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response authorizationServerIdWellKnownOpenidConnectGet(UUID authorizationServerId) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        var wellKnown = new com.cartobucket.auth.model.generated.WellKnown();
        wellKnown.setIssuer(String.valueOf(authorizationServer.getServerUrl()));
        wellKnown.setAuthorizationEndpoint(
                authorizationServer.getServerUrl() + "/" + authorizationServer.getId() + "/authorization/");
        wellKnown.setTokenEndpoint(
                authorizationServer.getServerUrl() + "/" + authorizationServer.getId() + "token/");
        wellKnown.setJwksUri(
                authorizationServer.getServerUrl() + "/" + authorizationServer.getId() + "/jwks/");
        wellKnown.setRevocationEndpoint(
                authorizationServer.getServerUrl() + "/" + authorizationServer.getId() + "/revocation/");
        wellKnown.setUserinfoEndpoint(
                authorizationServer.getServerUrl() + "/" + authorizationServer.getId() + "/userinfo/");
        wellKnown.setTokenEndpointAuthMethodsSupported(
                List.of(
                        com.cartobucket.auth.model.generated.WellKnown.TokenEndpointAuthMethodsSupportedEnum.POST
                )
        );
        wellKnown.setIdTokenSigningAlgValuesSupported(
                List.of(
                        com.cartobucket.auth.model.generated.WellKnown.IdTokenSigningAlgValuesSupportedEnum.RS256
                )
        );
        wellKnown.setResponseTypesSupported(
                Arrays.asList(
                        com.cartobucket.auth.model.generated.WellKnown.ResponseTypesSupportedEnum.CODE,
                        com.cartobucket.auth.model.generated.WellKnown.ResponseTypesSupportedEnum.CODE_ID_TOKEN,
                        com.cartobucket.auth.model.generated.WellKnown.ResponseTypesSupportedEnum.TOKEN
                )
        );
        wellKnown.setCodeChallengeMethodsSupported(List.of(com.cartobucket.auth.model.generated.WellKnown.CodeChallengeMethodsSupportedEnum.S256));
        wellKnown.setGrantTypesSupported(
                Arrays.asList(
                        AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS.value(),
                        AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE.value()
                )
        );
        return Response.ok().entity(wellKnown).build();
    }
}
