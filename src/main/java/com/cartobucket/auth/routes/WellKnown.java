package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.WellKnownApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.UUID;

public class WellKnown implements WellKnownApi {
    @Override
    public Response authorizationServerIdWellKnownOpenidConnectGet(UUID authorizationServerId) {
        var wellKnown = new com.cartobucket.auth.model.generated.WellKnown();
        wellKnown.setIssuer("https://sso.cartobucket.com/");
        wellKnown.setAuthorizationEndpoint("https://sso.cartobucket.com/authorizationServer/authorization/");
        wellKnown.setTokenEndpoint("https://sso.cartobucket.com/authorizationServer/token/");
        wellKnown.setJwksUri("https://sso.cartobucket.com/authorizationServer/jwks/");
        wellKnown.setRevocationEndpoint("https://sso.cartobucket.com/authorizationServer/revocation/");
        wellKnown.setUserinfoEndpoint("https://sso.cartobucket.com/authorizationServer/userinfo/");
        wellKnown.setTokenEndpointAuthMethodsSupported(
                Arrays.asList(
                        com.cartobucket.auth.model.generated.WellKnown.TokenEndpointAuthMethodsSupportedEnum.POST
                )
        );
        wellKnown.setIdTokenSigningAlgValuesSupported(
                Arrays.asList(
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
        wellKnown.setCodeChallengeMethodsSupported(Arrays.asList(com.cartobucket.auth.model.generated.WellKnown.CodeChallengeMethodsSupportedEnum.S256));
        wellKnown.setGrantTypesSupported(
                Arrays.asList(
                        AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS.value(),
                        AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE.value()
                )
        );
        return Response.ok().entity(wellKnown).build();
    }
}
