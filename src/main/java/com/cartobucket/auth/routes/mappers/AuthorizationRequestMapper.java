package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.AuthorizationRequest;
import com.cartobucket.auth.model.generated.UserAuthorizationRequest;

public class AuthorizationRequestMapper extends AuthorizationRequest {
    public static AuthorizationRequest from(
            String clientId,
            String responseType,
            String codeChallenge,
            String codeChallengeMethod,
            String redirectUri,
            String scope,
            String state,
            String nonce) {
        var authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setClientId(clientId);
        authorizationRequest.setResponseType(ResponseTypeEnum.fromString(responseType));
        authorizationRequest.setCodeChallenge(codeChallenge);
        if (codeChallengeMethod != null && !codeChallengeMethod.isEmpty()) {
            authorizationRequest.setCodeChallengeMethod(CodeChallengeMethodEnum.fromString(codeChallengeMethod));
        }
        authorizationRequest.setRedirectUri(redirectUri);
        authorizationRequest.setScope(scope);
        authorizationRequest.setState(state);
        authorizationRequest.setNonce(nonce);
        return authorizationRequest;
    }
}
