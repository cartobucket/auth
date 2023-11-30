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

package com.cartobucket.auth.authorization.server.routes;

import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import com.cartobucket.auth.data.services.ClientService;
import com.cartobucket.auth.data.services.TemplateService;
import com.cartobucket.auth.data.services.UserService;
import io.quarkus.qute.Qute;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class AuthorizationServer implements AuthorizationServerApi {
    final AuthorizationServerService authorizationServerService;
    final ClientService clientService;
    final UserService userService;
    final TemplateService templateService;
    final ApplicationService applicationService;

    public AuthorizationServer(
            AuthorizationServerService authorizationServerService,
            ClientService clientService,
            UserService userService, TemplateService templateService,
            ApplicationService applicationService) {
        this.authorizationServerService = authorizationServerService;
        this.clientService = clientService;
        this.userService = userService;
        this.templateService = templateService;
        this.applicationService = applicationService;
    }

    @Override
    @Consumes({"*/*"})
    public Response createAuthorizationCode(
            UUID authorizationServerId,
            String clientId,
            String responseType,
            String codeChallenge,
            String codeChallengeMethod,
            String redirectUri,
            String scope,
            String state,
            String nonce,
            String username,
            String password) {
        // TODO: Check the CSRF token

        final var user = userService.getUser(username).getLeft();
        if (user == null) {
            return renderLoginScreen(authorizationServerId);
        }
        if (user.getAuthorizationServerId() != authorizationServerId) {
            return renderLoginScreen(authorizationServerId);
        }
        if (!userService.validatePassword(user.getId(), password)) {
            return renderLoginScreen(authorizationServerId);
        }
        final var client = clientService.getClient(clientId);
        if (client == null) {
            return renderLoginScreen(authorizationServerId);
        }
        if (client.getAuthorizationServerId() != authorizationServerId) {
            return renderLoginScreen(authorizationServerId);
        }
        if (client.getRedirectUris().contains(redirectUri)) {
            return renderLoginScreen(authorizationServerId);
        }
        final var scopes = ScopeService.scopeStringToScopeList(scope);
        if (client.getScopes().containsAll(scopes)) {
            return renderLoginScreen(authorizationServerId);
        }

        var clientCode = new ClientCode();
        clientCode.setClientId(clientId);
        clientCode.setRedirectUri(redirectUri);
        clientCode.setAuthorizationServerId(authorizationServerId);
        clientCode.setNonce(nonce);
        clientCode.setState(state);
        clientCode.setCodeChallenge(codeChallenge);
        clientCode.setCodeChallengeMethod(codeChallengeMethod);
        clientCode.setScopes(ScopeService.scopeStringToScopeList(scope));

        final var code = clientService.createClientCode(
                authorizationServerId,
                clientCode
        );

        return Response.status(302).location(
                URI.create(
                        redirectUri + "?code=" + URLEncoder.encode(code.getCode(), StandardCharsets.UTF_8) + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) + "&nonce=" + URLEncoder.encode(nonce, StandardCharsets.UTF_8) + "&scope" + URLEncoder.encode(scope, StandardCharsets.UTF_8)
                )
        ).build();
    }

    @Override
    public Response getAuthorizationServerJwks(UUID authorizationServerId) {
        return Response
                .ok()
                .entity(
                        authorizationServerService.getJwksForAuthorizationServer(authorizationServerId)
                )
                .build();
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
        final var jwtClaims = authorizationServerService.validateJwtForAuthorizationServer(
                authorizationServerId,
                authorization
        );
        return Response
                .ok()
                .entity(userService.getUser(UUID.fromString(String.valueOf(jwtClaims.get("sub")))).getRight().getProfile())
                .build();
    }

    @Override
    @Consumes({ "*/*" })
    public Response initiateAuthorization(UUID authorizationServerId, String clientId, String responseType, String codeChallenge, String codeChallengeMethod, String redirectUri, String scope, String state, String nonce) {
        return renderLoginScreen(authorizationServerId);
    }

    @Override
    public Response issueToken(UUID authorizationServerId, AccessTokenRequest accessTokenRequest) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);

        // TODO: This stuff should be moved to a validator
        switch (accessTokenRequest.getGrantType()) {
            case CLIENT_CREDENTIALS -> {
                if (applicationService.isApplicationSecretValid(
                        authorizationServerId,
                        UUID.fromString(accessTokenRequest.getClientId()),
                        accessTokenRequest.getClientSecret())) {
                    throw new BadRequestException();
                }

                return Response
                        .ok()
                        .entity(
                                authorizationServerService
                                        .generateAccessToken(
                                                authorizationServerId,
                                                UUID.fromString(accessTokenRequest.getClientId()),
                                                accessTokenRequest.getClientId(),
                                                accessTokenRequest.getScope(),
                                                authorizationServer.getClientCredentialsTokenExpiration(),
                                                null
                                        )
                        )
                        .build();
            }
            case AUTHORIZATION_CODE -> {
                final var clientCode = clientService.getClientCode(accessTokenRequest.getCode());
                if (clientCode == null) {
                    throw new BadRequestException();
                }

                if (clientCode.getRedirectUri().equals(accessTokenRequest.getRedirectUri())) {
                    throw new BadRequestException();
                }
                final var scopes = ScopeService.scopeStringToScopeList(accessTokenRequest.getScope());
                // TODO: IntelliJ gave me this hint, not convinced, but gotta think about it more.
                if (!new HashSet<>(clientCode.getScopes()).containsAll(scopes)) {
                    throw new BadRequestException();
                }

                return Response
                        .ok()
                        .entity(
                                authorizationServerService
                                        .generateAccessToken(
                                                authorizationServerId,
                                                clientCode.getUserId(),
                                                String.valueOf(clientCode.getUserId()),
                                                accessTokenRequest.getScope(),
                                                authorizationServer.getAuthorizationCodeTokenExpiration(),
                                                clientCode.getNonce()
                                        )
                        )
                        .build();
            }
        }
        throw new BadRequestException();
    }

    private Response renderLoginScreen(UUID authorizationServerId) {
        final var template = templateService
                .getTemplates(Collections.singletonList(authorizationServerId), new Page(1, 0))
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
}