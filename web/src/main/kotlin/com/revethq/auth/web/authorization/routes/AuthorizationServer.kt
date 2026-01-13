/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.web.authorization.routes

import com.revethq.auth.core.authorization.dto.AccessTokenRequest
import com.revethq.auth.core.authorization.dto.WellKnown
import com.revethq.auth.core.authorization.interfaces.AuthorizationServerApi
import com.revethq.auth.web.authorization.routes.mappers.AccessTokenResponseMapper
import com.revethq.auth.web.authorization.routes.mappers.JwksMapper
import com.revethq.auth.core.domain.ClientCode
import com.revethq.auth.core.services.ApplicationService
import com.revethq.auth.core.services.AuthorizationServerService
import com.revethq.auth.core.services.ClientService
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.core.services.TemplateService
import com.revethq.auth.core.services.UserService
import jakarta.ws.rs.BadRequestException
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.UUID

abstract class AuthorizationServer : AuthorizationServerApi {

    protected lateinit var authorizationServerService: AuthorizationServerService
    protected lateinit var clientService: ClientService
    protected lateinit var userService: UserService
    protected lateinit var templateService: TemplateService
    protected lateinit var applicationService: ApplicationService
    protected lateinit var scopeService: ScopeService

    @Suppress("unused")
    protected constructor()

    constructor(
        authorizationServerService: AuthorizationServerService,
        clientService: ClientService,
        userService: UserService,
        templateService: TemplateService,
        applicationService: ApplicationService,
        scopeService: ScopeService
    ) {
        this.authorizationServerService = authorizationServerService
        this.clientService = clientService
        this.userService = userService
        this.templateService = templateService
        this.applicationService = applicationService
        this.scopeService = scopeService
    }

    companion object {
        private val LOG: Logger = Logger.getLogger(AuthorizationServer::class.java)
    }

    @Consumes("multipart/form-data", MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON)
    override fun createAuthorizationCode(
        authorizationServerId: UUID,
        clientId: String?,
        responseType: String?,
        codeChallenge: String?,
        codeChallengeMethod: String?,
        redirectUri: String?,
        scope: String?,
        state: String?,
        nonce: String?,
        username: String?,
        password: String?
    ): Response {
        // TODO: Check the CSRF token

        // If no username/password provided, show login screen
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return renderLoginScreen(authorizationServerId)
        }

        val user = try {
            userService.getUser(username).left
        } catch (e: Exception) {
            LOG.error("User not found: $username")
            return renderLoginScreen(authorizationServerId, "Invalid username or password")
        }

        if (user == null) {
            LOG.error("User not found: $username")
            return renderLoginScreen(authorizationServerId, "Invalid username or password")
        }
        if (user.authorizationServerId != authorizationServerId) {
            LOG.error("AuthorizationServer does not equal the user AuthorizationServer: $authorizationServerId")
            return renderLoginScreen(authorizationServerId, "Invalid username or password")
        }

        val userId = user.id ?: run {
            LOG.error("User ID is null")
            return renderLoginScreen(authorizationServerId, "Invalid username or password")
        }

        if (!userService.validatePassword(userId, password)) {
            LOG.error("Password was not correct")
            return renderLoginScreen(authorizationServerId, "Invalid username or password")
        }

        // Client validation should have already happened in initiateAuthorization
        // This is just a safety check for the POST endpoint
        val client = try {
            clientService.getClient(clientId ?: "").also {
                if (it.authorizationServerId != authorizationServerId) {
                    LOG.error("Client not authorized for server in POST: $clientId")
                    return renderLoginScreen(authorizationServerId, "Invalid client_id")
                }
            }
        } catch (e: com.revethq.auth.core.exceptions.notfound.ClientNotFound) {
            LOG.error("Client not found in POST: $clientId")
            return renderLoginScreen(authorizationServerId, "Invalid client_id")
        }

        // These validations should have happened in initiateAuthorization
        // Keep them here as safety checks for direct POST requests
        val validScopes = scopeService.filterScopesForAuthorizationServerId(
            authorizationServerId,
            scope ?: ""
        )

        val clientCode = ClientCode().apply {
            this.clientId = clientId
            this.redirectUri = redirectUri
            this.authorizationServerId = authorizationServerId
            this.nonce = nonce
            this.state = state
            this.codeChallenge = codeChallenge
            this.codeChallengeMethod = codeChallengeMethod
            this.scopes = validScopes
            this.userId = userId
        }

        LOG.error("We are about to call into the client service now")
        val code = clientService.createClientCode(authorizationServerId, clientCode)

        var redirectUrl = "$redirectUri?code=${URLEncoder.encode(code.code, StandardCharsets.UTF_8)}"

        if (code.state != null) {
            redirectUrl += "&state=${URLEncoder.encode(code.state, StandardCharsets.UTF_8)}"
        }

        if (code.nonce != null) {
            redirectUrl += "&nonce=${URLEncoder.encode(code.nonce, StandardCharsets.UTF_8)}"
        }

        // Include the actual scopes that were validated and stored
        val codeScopes = code.scopes
        if (!codeScopes.isNullOrEmpty()) {
            val scopeString = ScopeService.scopeListToScopeString(
                codeScopes.mapNotNull { it.name }
            )
            redirectUrl += "&scope=${URLEncoder.encode(scopeString, StandardCharsets.UTF_8)}"
        } else {
            redirectUrl += "&scope=${URLEncoder.encode("openid", StandardCharsets.UTF_8)}"
        }

        return Response.status(302).location(URI.create(redirectUrl)).build()
    }

    override fun getAuthorizationServerJwks(authorizationServerId: UUID): Response {
        return Response
            .ok()
            .entity(
                JwksMapper.toJwksResponse(
                    authorizationServerService.getJwksForAuthorizationServer(authorizationServerId)
                )
            )
            .build()
    }

    @Path(".well-known/openid-configuration")
    @GET
    fun getOpenIdConfiguration(authorizationServerId: UUID): Response {
        return getOpenIdConnectionWellKnown(authorizationServerId)
    }

    override fun getOpenIdConnectionWellKnown(authorizationServerId: UUID): Response {
        val authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId)
        val wellKnown = WellKnown(
            issuer = "${authorizationServer.serverUrl}/${authorizationServer.id}/",
            authorizationEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/authorization/",
            tokenEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/token/",
            jwksUri = "${authorizationServer.serverUrl}/${authorizationServer.id}/jwks/",
            revocationEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/revocation/",
            userinfoEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/userinfo/",
            tokenEndpointAuthMethodsSupported = listOf(WellKnown.TokenEndpointAuthMethodsSupportedEnum.POST.value()),
            idTokenSigningAlgValuesSupported = listOf(WellKnown.IdTokenSigningAlgValuesSupportedEnum.RS256.value()),
            responseTypesSupported = listOf(
                WellKnown.ResponseTypesSupportedEnum.CODE.value(),
                WellKnown.ResponseTypesSupportedEnum.CODE_ID_TOKEN.value(),
                WellKnown.ResponseTypesSupportedEnum.TOKEN.value()
            ),
            codeChallengeMethodsSupported = listOf(WellKnown.CodeChallengeMethodsSupportedEnum.S256.value()),
            grantTypesSupported = listOf(
                AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS.value(),
                AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE.value(),
                AccessTokenRequest.GrantTypeEnum.REFRESH_TOKEN.value()
            )
        )
        return Response.ok().entity(wellKnown).build()
    }

    override fun getUserInfo(authorizationServerId: UUID, authorization: String?): Response {
        val jwtClaims = authorizationServerService.validateJwtForAuthorizationServer(
            authorizationServerId,
            authorization ?: ""
        )
        val profile = userService.getUser(UUID.fromString(jwtClaims["sub"].toString())).right?.profile
        return Response
            .ok()
            .entity(profile)
            .build()
    }

    override fun getAuthorization(
        authorizationServerId: UUID,
        clientId: String?,
        responseType: String?,
        redirectUri: String?,
        scope: String?,
        state: String?,
        nonce: String?,
        codeChallenge: String?,
        codeChallengeMethod: String?
    ): Response {
        return initiateAuthorization(
            authorizationServerId,
            clientId,
            responseType,
            codeChallenge,
            codeChallengeMethod,
            redirectUri,
            scope,
            state,
            nonce
        )
    }

    fun initiateAuthorization(
        authorizationServerId: UUID,
        clientId: String?,
        responseType: String?,
        codeChallenge: String?,
        codeChallengeMethod: String?,
        redirectUri: String?,
        scope: String?,
        state: String?,
        nonce: String?
    ): Response {
        // Validate client_id first (OAuth2 spec: if invalid, don't redirect)
        if (clientId.isNullOrEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"invalid_request\",\"error_description\":\"client_id is required\"}")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        val client = try {
            clientService.getClient(clientId)
        } catch (e: com.revethq.auth.core.exceptions.notfound.ClientNotFound) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"invalid_client\",\"error_description\":\"Client authentication failed\"}")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        if (client.authorizationServerId != authorizationServerId) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"invalid_client\",\"error_description\":\"Client not authorized for this server\"}")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        // Validate redirect_uri (OAuth2 spec: if invalid, don't redirect)
        if (redirectUri.isNullOrEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"invalid_request\",\"error_description\":\"redirect_uri is required\"}")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        val redirectUriAllowed = try {
            val requestedUri = URI.create(redirectUri)
            client.redirectUris?.any { allowedUri ->
                try {
                    normalizeUri(allowedUri) == normalizeUri(requestedUri)
                } catch (e: Exception) {
                    LOG.warn("Invalid URI in client redirect URIs: $allowedUri")
                    false
                }
            } ?: false
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"invalid_request\",\"error_description\":\"Invalid redirect_uri format\"}")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        if (!redirectUriAllowed) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"invalid_request\",\"error_description\":\"redirect_uri not registered for this client\"}")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        // Validate scopes (OAuth2 spec: if invalid, redirect with error)
        if (!scope.isNullOrEmpty()) {
            val requestedScopes = ScopeService.scopeStringToScopeList(scope)
            val validScopes = scopeService.filterScopesForAuthorizationServerId(authorizationServerId, scope)

            val validScopeNames = validScopes.mapNotNull { it.name }
            val invalidScopeNames = requestedScopes
                .mapNotNull { it.name }
                .filter { !validScopeNames.contains(it) }

            if (invalidScopeNames.isNotEmpty()) {
                // Redirect with error for invalid scopes
                var errorUri = redirectUri +
                    (if (redirectUri.contains("?")) "&" else "?") +
                    "error=invalid_scope" +
                    "&error_description=${URLEncoder.encode("Invalid scopes: ${invalidScopeNames.joinToString(", ")}", StandardCharsets.UTF_8)}"
                if (state != null) {
                    errorUri += "&state=$state"
                }
                return Response.status(Response.Status.FOUND)
                    .location(URI.create(errorUri))
                    .build()
            }
        }

        return renderLoginScreen(authorizationServerId)
    }

    override fun postTokenJson(authorizationServerId: UUID, request: AccessTokenRequest): Response {
        return postToken(
            authorizationServerId,
            request.grantType?.value(),
            request.clientId,
            request.clientSecret,
            request.code,
            request.redirectUri,
            request.codeVerifier,
            request.refreshToken,
            request.scope
        )
    }

    override fun postToken(
        authorizationServerId: UUID,
        grantType: String?,
        clientId: String?,
        clientSecret: String?,
        code: String?,
        redirectUri: String?,
        codeVerifier: String?,
        refreshToken: String?,
        scope: String?
    ): Response {
        val authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId)

        if (grantType == null) {
            throw BadRequestException("grant_type is required")
        }

        // TODO: This stuff should be moved to a validator
        val accessTokenRequest = AccessTokenRequest(
            clientId = clientId,
            clientSecret = clientSecret,
            grantType = AccessTokenRequest.GrantTypeEnum.fromValue(grantType),
            code = code,
            redirectUri = redirectUri,
            codeVerifier = codeVerifier,
            scope = scope ?: "",
            refreshToken = refreshToken
        )

        val requestGrantType = accessTokenRequest.grantType ?: throw BadRequestException("grant_type is required")
        val requestClientId = accessTokenRequest.clientId ?: throw BadRequestException("client_id is required")

        return when (requestGrantType) {
            AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS -> {
                if (!applicationService.isApplicationSecretValid(
                        authorizationServerId,
                        UUID.fromString(requestClientId),
                        accessTokenRequest.clientSecret ?: ""
                    )
                ) {
                    throw BadRequestException()
                }

                val applicationSecret = applicationService.getApplicationSecret(requestClientId)
                val scopes = scopeService.filterScopesForAuthorizationServerId(
                    authorizationServerId,
                    accessTokenRequest.scope ?: ""
                ).filter { requestedScope -> applicationSecret.scopes?.contains(requestedScope) == true }

                val appId = applicationSecret.applicationId ?: throw BadRequestException("Application ID is null")
                val tokenExpiration = authorizationServer.clientCredentialsTokenExpiration ?: 3600L

                Response
                    .ok()
                    .entity(
                        AccessTokenResponseMapper.toAccessTokenResponse(
                            authorizationServerService.generateClientCredentialsAccessToken(
                                authorizationServerId,
                                appId,
                                appId.toString(),
                                scopes,
                                tokenExpiration
                            )
                        )
                    )
                    .build()
            }
            AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE -> {
                val clientCode = clientService.getClientCode(accessTokenRequest.code ?: "")
                    ?: throw BadRequestException()

                if (clientCode.redirectUri != accessTokenRequest.redirectUri) {
                    throw BadRequestException()
                }

                val codeUserId = clientCode.userId ?: throw BadRequestException("User ID is null")
                val codeClientId = clientCode.clientId ?: throw BadRequestException("Client ID is null")
                val codeScopes = clientCode.scopes ?: emptyList()
                val tokenExpiration = authorizationServer.authorizationCodeTokenExpiration ?: 3600L

                Response
                    .ok()
                    .entity(
                        AccessTokenResponseMapper.toAccessTokenResponse(
                            authorizationServerService.generateAuthorizationCodeFlowAccessToken(
                                authorizationServerId,
                                codeUserId,
                                codeUserId.toString(),
                                codeClientId,
                                codeScopes,
                                tokenExpiration,
                                clientCode.nonce
                            )
                        )
                    )
                    .build()
            }
            AccessTokenRequest.GrantTypeEnum.REFRESH_TOKEN -> {
                val requestRefreshToken = accessTokenRequest.refreshToken
                if (requestRefreshToken.isNullOrEmpty()) {
                    throw BadRequestException("refresh_token is required")
                }

                Response
                    .ok()
                    .entity(
                        AccessTokenResponseMapper.toAccessTokenResponse(
                            authorizationServerService.refreshAccessToken(
                                authorizationServerId,
                                requestRefreshToken,
                                requestClientId
                            )
                        )
                    )
                    .build()
            }
        }
    }

    protected abstract fun renderLoginScreen(authorizationServerId: UUID): Response

    protected open fun renderLoginScreen(authorizationServerId: UUID, errorMessage: String?): Response {
        // Default implementation that ignores the error message for backward compatibility
        return renderLoginScreen(authorizationServerId)
    }

    private fun normalizeUri(uri: URI): String {
        // Normalize URI for comparison:
        // - Convert scheme and host to lowercase
        // - Remove default ports (80 for HTTP, 443 for HTTPS)
        // - Normalize path (remove trailing slash if it's just "/")
        val scheme = uri.scheme?.lowercase() ?: ""
        val host = uri.host?.lowercase() ?: ""
        var port = uri.port
        val path = uri.path ?: ""
        val query = uri.query
        val fragment = uri.fragment

        // Remove default ports
        if ((port == 80 && scheme == "http") || (port == 443 && scheme == "https")) {
            port = -1
        }

        // Build normalized URI string
        return buildString {
            append(scheme)
            if (scheme.isNotEmpty()) {
                append("://")
            }
            append(host)
            if (port != -1) {
                append(":").append(port)
            }
            append(path)
            if (query != null) {
                append("?").append(query)
            }
            if (fragment != null) {
                append("#").append(fragment)
            }
        }
    }
}
