package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.PasswordAuthRequest;
import com.cartobucket.auth.routes.mappers.AuthorizationRequestMapper;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.ClientService;
import com.cartobucket.auth.services.UserService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import org.jose4j.jwt.MalformedClaimException;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AuthorizationServer implements AuthorizationServerApi {
    final AccessTokenService accessTokenService;
    final AuthorizationServerService authorizationServerService;
    final ClientService clientService;
    final UserService userService;

    public AuthorizationServer(
            AccessTokenService accessTokenService,
            AuthorizationServerService authorizationServerService,
            ClientService clientService,
            UserService userService) {
        this.accessTokenService = accessTokenService;
        this.authorizationServerService = authorizationServerService;
        this.clientService = clientService;
        this.userService = userService;
    }

    @Override
    @Consumes({"*/*"})
    public Response createAuthorizationCode(UUID authorizationServerId, String clientId, String responseType, String codeChallenge, String codeChallengeMethod, String redirectUri, String scope, String state, String nonce, String username, String password) {
        var authorizationRequest = AuthorizationRequestMapper.from(
                clientId,
                responseType,
                codeChallenge,
                codeChallengeMethod,
                redirectUri,
                scope,
                state,
                nonce
        );
        var userAuthorizationRequest = new PasswordAuthRequest();
        userAuthorizationRequest.setUsername(username);
        userAuthorizationRequest.setPassword(password);
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        final var code = clientService.buildClientCodeForEmailAndPassword(
                authorizationServer,
                authorizationRequest,
                userAuthorizationRequest
        );
        if (code == null) {
            return Response.ok().entity(authorizationServerService.renderLogin(authorizationServerId)).build();
        }
        return Response.status(302).location(
                URI.create(
                        authorizationRequest.getRedirectUri() + "?code=" + URLEncoder.encode(code.getCode(), StandardCharsets.UTF_8) + "&state=" + URLEncoder.encode(authorizationRequest.getState(), StandardCharsets.UTF_8) + "&nonce=" + URLEncoder.encode(authorizationRequest.getNonce(), StandardCharsets.UTF_8) + "&scope" + URLEncoder.encode(authorizationRequest.getScope(), StandardCharsets.UTF_8)
                )
        ).build();
    }

    @Override
    public Response getAuthorizationServerJwks(UUID authorizationServerId) {
        return Response.ok().entity(
                authorizationServerService.getJwksForAuthorizationServer(authorizationServerId)
        ).build();
    }

    @Override
    public Response getOpenIdConnectionWellKnown(UUID authorizationServerId) {
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

    @Override
    public Response getUserInfo(UUID authorizationServerId, String authorization) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(
                authorizationServerId
        );
        // TODO: Maybe this makes more sense in the userService?
        final var jwtClaims = authorizationServerService.validateJwtForAuthorizationServer(
                authorizationServer,
                authorization
        );
        try {
            return Response
                    .ok()
                    .entity(userService.getUser(UUID.fromString(jwtClaims.getSubject())).getProfile())
                    .build();
        } catch (MalformedClaimException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    @Consumes({ "*/*" })
    public Response initiateAuthorization(UUID authorizationServerId, String clientId, String responseType, String codeChallenge, String codeChallengeMethod, String redirectUri, String scope, String state, String nonce) {
        return Response.ok().entity(authorizationServerService.renderLogin(authorizationServerId)).build();
    }

    @Override
    public Response issueToken(UUID authorizationServerId, AccessTokenRequest accessTokenRequest) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        switch (accessTokenRequest.getGrantType()) {
            case CLIENT_CREDENTIALS -> {
                return Response
                        .ok()
                        .entity(accessTokenService.fromClientCredentials(authorizationServer, accessTokenRequest))
                        .build();
            }
            case AUTHORIZATION_CODE -> {
                return Response
                        .ok()
                        .entity(accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest))
                        .build();
            }
        }
        throw new BadRequestException();
    }
}