/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.routes;

import com.cartobucket.auth.exceptions.notfound.TemplateNotFound;
import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.models.PasswordAuthRequest;
import com.cartobucket.auth.models.TemplateTypeEnum;
import com.cartobucket.auth.routes.mappers.AuthorizationRequestMapper;
import com.cartobucket.auth.services.*;
import io.quarkus.qute.Qute;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import org.jose4j.jwt.MalformedClaimException;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AuthorizationServer implements AuthorizationServerApi {
    final TokenService tokenService;
    final AuthorizationServerService authorizationServerService;
    final ClientService clientService;
    final UserService userService;
    final TemplateService templateService;

    public AuthorizationServer(
            TokenService accessTokenService,
            AuthorizationServerService authorizationServerService,
            ClientService clientService,
            UserService userService, TemplateService templateService) {
        this.tokenService = accessTokenService;
        this.authorizationServerService = authorizationServerService;
        this.clientService = clientService;
        this.userService = userService;
        this.templateService = templateService;
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
        var passwordAuthRequest = new PasswordAuthRequest();
        passwordAuthRequest.setUsername(username);
        passwordAuthRequest.setPassword(password);
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        final var code = clientService.buildClientCodeForEmailAndPassword(
                authorizationServer,
                UUID.fromString(clientId),
                scope,
                redirectUri,
                nonce,
                state,
                codeChallenge,
                codeChallengeMethod,
                passwordAuthRequest
        );
        if (code == null) {
            return renderLoginScreen(authorizationServerId);
        }
        return Response.status(302).location(
                URI.create(
                        authorizationRequest.getRedirectUri() + "?code=" + URLEncoder.encode(code.getCode(), StandardCharsets.UTF_8) + "&state=" + URLEncoder.encode(authorizationRequest.getState(), StandardCharsets.UTF_8) + "&nonce=" + URLEncoder.encode(authorizationRequest.getNonce(), StandardCharsets.UTF_8) + "&scope" + URLEncoder.encode(authorizationRequest.getScope(), StandardCharsets.UTF_8)
                )
        ).build();
    }

    private Response renderLoginScreen(UUID authorizationServerId) {
        final var template = templateService
                .getTemplates(Collections.singletonList(authorizationServerId))
                .stream()
                .filter(t -> t.getTemplateType() == TemplateTypeEnum.LOGIN)
                .findFirst()
                .orElseThrow(TemplateNotFound::new);

        return Response
                .ok()
                .entity(
                        Qute.fmt(new String(Base64.getDecoder().decode(template.getTemplate())))
                                .render()
                )
                .build();
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
                    .entity(userService.getUser(UUID.fromString(jwtClaims.getSubject())).getRight().getProfile())
                    .build();
        } catch (MalformedClaimException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    @Consumes({ "*/*" })
    public Response initiateAuthorization(UUID authorizationServerId, String clientId, String responseType, String codeChallenge, String codeChallengeMethod, String redirectUri, String scope, String state, String nonce) {
        return renderLoginScreen(authorizationServerId);
    }

    @Override
    public Response issueToken(UUID authorizationServerId, AccessTokenRequest accessTokenRequest) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        switch (accessTokenRequest.getGrantType()) {
            case CLIENT_CREDENTIALS -> {
                return Response
                        .ok()
                        .entity(
                                tokenService
                                        .fromClientCredentials(
                                                authorizationServer,
                                                accessTokenRequest.getClientId(),
                                                accessTokenRequest.getClientSecret(),
                                                accessTokenRequest.getScope()
                                        )
                        )
                        .build();
            }
            case AUTHORIZATION_CODE -> {
                return Response
                        .ok()
                        .entity(
                                tokenService
                                        .fromAuthorizationCode(
                                                authorizationServer,
                                                accessTokenRequest.getCode(),
                                                accessTokenRequest.getClientId(),
                                                accessTokenRequest.getRedirectUri(),
                                                accessTokenRequest.getCodeVerifier()
                                        )
                        )
                        .build();
            }
        }
        throw new BadRequestException();
    }
}