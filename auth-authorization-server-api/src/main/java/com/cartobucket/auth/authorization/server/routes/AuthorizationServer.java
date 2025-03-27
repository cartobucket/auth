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

import com.cartobucket.auth.authorization.server.routes.mappers.AccessTokenResponseMapper;
import com.cartobucket.auth.authorization.server.routes.mappers.JwksMapper;
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import com.cartobucket.auth.data.services.ClientService;
import com.cartobucket.auth.data.services.TemplateService;
import com.cartobucket.auth.data.services.UserService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class AuthorizationServer implements AuthorizationServerApi {
    private static final Logger LOG = Logger.getLogger(AuthorizationServer.class);

    final AuthorizationServerService authorizationServerService;
    final ClientService clientService;
    final UserService userService;
    final TemplateService templateService;
    final ApplicationService applicationService;
    final ScopeService scopeService;

    public AuthorizationServer(
            AuthorizationServerService authorizationServerService,
            ClientService clientService,
            UserService userService, TemplateService templateService,
            ApplicationService applicationService, ScopeService scopeService) {
        this.authorizationServerService = authorizationServerService;
        this.clientService = clientService;
        this.userService = userService;
        this.templateService = templateService;
        this.applicationService = applicationService;
        this.scopeService = scopeService;
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
            LOG.error("User not found: " + username);
            return renderLoginScreen(authorizationServerId);
        }
        if (!user.getAuthorizationServerId().equals(authorizationServerId)) {
            LOG.error("AuthorizationServer does not equal the user AuthorizationServer: " + authorizationServerId);
            return renderLoginScreen(authorizationServerId);
        }
        if (!userService.validatePassword(user.getId(), password)) {
            LOG.error("Password was not correct: " + password);
            return renderLoginScreen(authorizationServerId);
        }
        final var client = clientService.getClient(clientId);
        if (client == null) {
            LOG.error("Client not found: " + clientId);
            return renderLoginScreen(authorizationServerId);
        }
        if (!client.getAuthorizationServerId().equals(authorizationServerId)) {
            LOG.error("Client not found in AuthorizationServer: " + authorizationServerId);
            return renderLoginScreen(authorizationServerId);
        }
        if (client.getRedirectUris().contains(redirectUri)) {
            LOG.error("Redirects not found: " + redirectUri);
            return renderLoginScreen(authorizationServerId);
        }

        // TODO: FIx this
//        final var scopes = ScopeService.scopeStringToScopeList(scope);
//        if (client.getScopes().containsAll(scopes)) {
//            LOG.error("Scopes not found: " + scopes + " and " + client.getScopes().toString());
//            return renderLoginScreen(authorizationServerId);
//        }

        var clientCode = new ClientCode();
        clientCode.setClientId(clientId);
        clientCode.setRedirectUri(redirectUri);
        clientCode.setAuthorizationServerId(authorizationServerId);
        clientCode.setNonce(nonce);
        clientCode.setState(state);
        clientCode.setCodeChallenge(codeChallenge);
        clientCode.setCodeChallengeMethod(codeChallengeMethod);
        clientCode.setScopes(ScopeService.scopeStringToScopeList(scope));
        clientCode.setUserId(user.getId());

        LOG.error("We are about to call into the client service now");
        final var code = clientService.createClientCode(
                authorizationServerId,
                clientCode
        );

        return Response.status(302).location(
                URI.create(
                        redirectUri +
                                "?code=" +
                                URLEncoder.encode(code.getCode(), StandardCharsets.UTF_8) +
                                "&state=" + URLEncoder.encode(code.getState(), StandardCharsets.UTF_8) +
                                "&nonce=" + URLEncoder.encode(code.getNonce(), StandardCharsets.UTF_8) +
                                "&scope" + URLEncoder.encode("openid", StandardCharsets.UTF_8)
//                                        ScopeService.scopeListToScopeString(
//                                                code
//                                                        .getScopes()
//                                                        .stream()
//                                                        .map(Scope::getName)
//                                                        .toList()
//                                        ), StandardCharsets.UTF_8)
                )
        ).build();
    }

    @Override
    public Response getAuthorizationServerJwks(UUID authorizationServerId) {
        return Response
                .ok()
                .entity(
                        JwksMapper.toJwksResponse(
                                authorizationServerService.getJwksForAuthorizationServer(authorizationServerId)
                        )
                )
                .build();
    }

    @Path(".well-known/openid-configuration")
    @GET
    @Consumes({"*/*"})
    public Response getOpenIdConfiguration(UUID authorizationServerId) {
        return getOpenIdConnectionWellKnown(authorizationServerId);
    }

    @Override
    public Response getOpenIdConnectionWellKnown(UUID authorizationServerId) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        var wellKnown = new com.cartobucket.auth.model.generated.WellKnown();
        wellKnown.setIssuer(authorizationServer.getServerUrl() + "/" + authorizationServer.getId() + "/");
        wellKnown.setAuthorizationEndpoint(
                authorizationServer.getServerUrl().toString() + "/"  + authorizationServer.getId() + "/authorization/");
        wellKnown.setTokenEndpoint(
                authorizationServer.getServerUrl().toString() + "/"  + authorizationServer.getId() + "/token/");
        wellKnown.setJwksUri(
                authorizationServer.getServerUrl().toString() + "/"  + authorizationServer.getId() + "/jwks/");
        wellKnown.setRevocationEndpoint(
                authorizationServer.getServerUrl().toString() + "/"  + authorizationServer.getId() + "/revocation/");
        wellKnown.setUserinfoEndpoint(
                authorizationServer.getServerUrl().toString() + "/"  + authorizationServer.getId() + "/userinfo/");
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

    @POST
    @Path("/token/")
    @Consumes({"application/x-www-form-urlencoded"})
    public Response issueToken(
            UUID authorizationServerId,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret,
            @FormParam("grant_type") String grantType,
            @FormParam("code") String code,
            @FormParam("redirect_uri") String redirectUri,
            @FormParam("code_verifier") String codeVerifier,
            @FormParam("scope") String scope) {
        final var accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClientId(clientId);
        accessTokenRequest.setClientSecret(clientSecret);
        accessTokenRequest.setGrantType(AccessTokenRequest.GrantTypeEnum.fromValue(grantType));
        accessTokenRequest.setCode(code);
        accessTokenRequest.setRedirectUri(redirectUri);
        accessTokenRequest.setCodeVerifier(codeVerifier);
        accessTokenRequest.setScope(scope != null ? scope : "");
        return issueToken(authorizationServerId, accessTokenRequest);
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

                final var scopes = scopeService.filterScopesForAuthorizationServerId(
                        authorizationServerId,
                        accessTokenRequest.getScope()
                );

                return Response
                        .ok()
                        .entity(
                                authorizationServerService
                                        .generateAccessToken(
                                                authorizationServerId,
                                                UUID.fromString(accessTokenRequest.getClientId()),
                                                accessTokenRequest.getClientId(),
                                                // TODO: Fix this
                                                scopeService.getScopes(List.of(authorizationServerId), new Page(100, 0)),
                                                //scopes,
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

                if (!clientCode.getRedirectUri().equals(accessTokenRequest.getRedirectUri())) {
                    throw new BadRequestException();
                }

                return Response
                        .ok()
                        .entity(
                                AccessTokenResponseMapper.toAccessTokenResponse(
                                        authorizationServerService
                                                .generateAccessToken(
                                                        authorizationServerId,
                                                        clientCode.getUserId(),
                                                        String.valueOf(clientCode.getUserId()),
                                                        clientCode.getScopes(),
                                                        authorizationServer.getAuthorizationCodeTokenExpiration(),
                                                        clientCode.getNonce()
                                                )
                                )
                        )
                        .build();
            }
        }
        throw new BadRequestException();
    }

    protected abstract Response renderLoginScreen(UUID authorizationServerId);
}