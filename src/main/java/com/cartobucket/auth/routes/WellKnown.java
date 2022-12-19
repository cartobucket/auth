package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.WellKnownApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;

import javax.ws.rs.Path;
import java.util.Arrays;

@Path("/.well-known/openid-connect/")
public class WellKnown implements WellKnownApi {
    @Override
    public com.cartobucket.auth.model.generated.WellKnown wellKnownOpenidConnectGet() {
        var wellKnown = new com.cartobucket.auth.model.generated.WellKnown();
        wellKnown.setIssuer("https://sso.cartobucket.com/");
        wellKnown.setAuthorizationEndpoint("https://sso.cartobucket.com/authorizationServer/authorization_endpoint/");
        wellKnown.setTokenEndpoint("https://sso.cartobucket.com/authorizationServer/token_endpoint/");
        wellKnown.setJwksUri("https://sso.cartobucket.com/authorizationServer/jwks_uri/");
        wellKnown.setRevocationEndpoint("https://sso.cartobucket.com/authorizationServer/revocation_endpoint/");
        wellKnown.setUserinfoEndpoint("https://sso.cartobucket.com/authorizationServer/userinfo_endpoint/");
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
        return wellKnown;
    }
}
