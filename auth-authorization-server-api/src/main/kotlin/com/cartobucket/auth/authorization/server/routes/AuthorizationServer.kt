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

package com.cartobucket.auth.authorization.server.routes

import com.cartobucket.auth.authorization.server.dto.AccessTokenRequest
import com.cartobucket.auth.authorization.server.dto.WellKnown
import com.cartobucket.auth.authorization.server.interfaces.AuthorizationServerApi
import com.cartobucket.auth.authorization.server.routes.mappers.AccessTokenResponseMapper.toAccessTokenResponse
import com.cartobucket.auth.authorization.server.routes.mappers.JwksMapper.toJwksResponse
import com.cartobucket.auth.data.domain.ClientCode
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.services.*
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

abstract class AuthorizationServer(
    protected val authorizationServerService: AuthorizationServerService,
    protected val clientService: ClientService,
    protected val userService: UserService,
    protected val templateService: TemplateService,
    protected val applicationService: ApplicationService,
    protected val scopeService: ScopeService
) : AuthorizationServerApi {

    companion object {
        private val LOG = Logger.getLogger(AuthorizationServer::class.java)
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
        // If no username/password provided, show login screen
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return renderLoginScreen(authorizationServerId)
        }

        val user = try {
            userService.getUser(username!!).left
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
        
        if (!userService.validatePassword(user.id!!, password!!)) {
            LOG.error("Password was not correct")
            return renderLoginScreen(authorizationServerId, "Invalid username or password")
        }

        val client = try {
            clientService.getClient(clientId!!)
        } catch (e: com.cartobucket.auth.data.exceptions.notfound.ClientNotFound) {
            LOG.error("Client not found in POST: $clientId")
            return renderLoginScreen(authorizationServerId, "Invalid client_id")
        }

        if (client.authorizationServerId != authorizationServerId) {
            LOG.error("Client not authorized for server in POST: $clientId")
            return renderLoginScreen(authorizationServerId, "Invalid client_id")
        }

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
            this.userId = user.id!!
        }

        LOG.error("We are about to call into the client service now")
        val code = clientService.createClientCode(authorizationServerId, clientCode)

        var redirectUrl = "${redirectUri}?code=${URLEncoder.encode(code.code, StandardCharsets.UTF_8)}"

        code.state?.let {
            redirectUrl += "&state=${URLEncoder.encode(it, StandardCharsets.UTF_8)}"
        }

        code.nonce?.let {
            redirectUrl += "&nonce=${URLEncoder.encode(it, StandardCharsets.UTF_8)}"
        }

        if (!code.scopes.isNullOrEmpty()) {
            val scopeString = ScopeService.scopeListToScopeString(
                code.scopes!!.map { it.name!! }
            )
            redirectUrl += "&scope=${URLEncoder.encode(scopeString, StandardCharsets.UTF_8)}"
        } else {
            redirectUrl += "&scope=${URLEncoder.encode("openid", StandardCharsets.UTF_8)}"
        }

        return Response.status(302)
            .location(URI.create(redirectUrl))
            .build()
    }

    override fun getAuthorizationServerJwks(authorizationServerId: UUID): Response {
        return Response
            .ok()
            .entity(
                authorizationServerService.getJwksForAuthorizationServer(authorizationServerId)
                    .toJwksResponse()
            )
            .build()
    }

    @Path(".well-known/openid-configuration")
    @GET
    fun getOpenIdConfiguration(authorizationServerId: UUID): Response =
        getOpenIdConnectionWellKnown(authorizationServerId)

    override fun getOpenIdConnectionWellKnown(authorizationServerId: UUID): Response {
        val authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId)
        val wellKnown = WellKnown(
            issuer = "${authorizationServer.serverUrl}/${authorizationServer.id}/",
            authorizationEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/authorization/",
            tokenEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/token/",
            jwksUri = "${authorizationServer.serverUrl}/${authorizationServer.id}/jwks/",
            revocationEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/revocation/",
            userinfoEndpoint = "${authorizationServer.serverUrl}/${authorizationServer.id}/userinfo/",
            tokenEndpointAuthMethodsSupported = listOf(WellKnown.TokenEndpointAuthMethodsSupportedEnum.POST.value),
            idTokenSigningAlgValuesSupported = listOf(WellKnown.IdTokenSigningAlgValuesSupportedEnum.RS256.value),
            responseTypesSupported = listOf(
                WellKnown.ResponseTypesSupportedEnum.CODE.value,
                WellKnown.ResponseTypesSupportedEnum.CODE_ID_TOKEN.value,
                WellKnown.ResponseTypesSupportedEnum.TOKEN.value
            ),
            codeChallengeMethodsSupported = listOf(WellKnown.CodeChallengeMethodsSupportedEnum.S256.value),
            grantTypesSupported = listOf(
                AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS.value,
                AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE.value,
                AccessTokenRequest.GrantTypeEnum.REFRESH_TOKEN.value
            )
        )
        return Response.ok().entity(wellKnown).build()
    }

    override fun getUserInfo(authorizationServerId: UUID, authorization: String?): Response {
        val jwtClaims = authorizationServerService.validateJwtForAuthorizationServer(
            authorizationServerId,
            authorization!!
        )
        return Response
            .ok()
            .entity(
                userService.getUser(UUID.fromString(jwtClaims["sub"].toString())).right?.profile
            )
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
    ): Response = initiateAuthorization(
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

    private fun initiateAuthorization(
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
        // Validate client_id first
        if (clientId.isNullOrEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("""{"error":"invalid_request","error_description":"client_id is required"}""")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        val client = try {
            clientService.getClient(clientId)
        } catch (e: com.cartobucket.auth.data.exceptions.notfound.ClientNotFound) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("""{"error":"invalid_client","error_description":"Client authentication failed"}""")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        if (client.authorizationServerId != authorizationServerId) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("""{"error":"invalid_client","error_description":"Client not authorized for this server"}""")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        // Validate redirect_uri
        if (redirectUri.isNullOrEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("""{"error":"invalid_request","error_description":"redirect_uri is required"}""")
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
                .entity("""{"error":"invalid_request","error_description":"Invalid redirect_uri format"}""")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        if (!redirectUriAllowed) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("""{"error":"invalid_request","error_description":"redirect_uri not registered for this client"}""")
                .type(MediaType.APPLICATION_JSON)
                .build()
        }

        // Validate scopes
        if (!scope.isNullOrEmpty()) {
            val requestedScopes = ScopeService.scopeStringToScopeList(scope)
            val validScopes = scopeService.filterScopesForAuthorizationServerId(
                authorizationServerId,
                scope
            )

            val validScopeNames = validScopes.map { it.name!! }
            val invalidScopeNames = requestedScopes
                .map { it.name!! }
                .filter { scopeName -> !validScopeNames.contains(scopeName) }

            if (invalidScopeNames.isNotEmpty()) {
                val errorUri = redirectUri +
                    (if (redirectUri.contains("?")) "&" else "?") +
                    "error=invalid_scope" +
                    "&error_description=" + URLEncoder.encode(
                        "Invalid scopes: ${invalidScopeNames.joinToString(", ")}",
                        StandardCharsets.UTF_8
                    ) +
                    (state?.let { "&state=$it" } ?: "")
                
                return Response.status(Response.Status.FOUND)
                    .location(URI.create(errorUri))
                    .build()
            }
        }

        return renderLoginScreen(authorizationServerId)
    }

    @POST
    @Path("/token/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun issueToken(
        authorizationServerId: UUID,
        @FormParam("client_id") clientId: String?,
        @FormParam("client_secret") clientSecret: String?,
        @FormParam("grant_type") grantType: String?,
        @FormParam("code") code: String?,
        @FormParam("redirect_uri") redirectUri: String?,
        @FormParam("code_verifier") codeVerifier: String?,
        @FormParam("scope") scope: String?,
        @FormParam("refresh_token") refreshToken: String?
    ): Response = postToken(
        authorizationServerId,
        grantType,
        clientId,
        clientSecret,
        code,
        redirectUri,
        codeVerifier,
        refreshToken,
        scope
    )

    override fun postTokenJson(authorizationServerId: UUID, request: AccessTokenRequest?): Response =
        postToken(
            authorizationServerId,
            request?.grantType?.value,
            request?.clientId,
            request?.clientSecret,
            request?.code,
            request?.redirectUri,
            request?.codeVerifier,
            request?.refreshToken,
            request?.scope
        )

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
        val grantTypeEnum = AccessTokenRequest.GrantTypeEnum.fromValue(grantType!!)

        return when (grantTypeEnum) {
            AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS -> {
                if (!applicationService.isApplicationSecretValid(
                        authorizationServerId,
                        UUID.fromString(clientId!!),
                        clientSecret!!
                    )) {
                    throw BadRequestException()
                }

                val applicationSecret = applicationService.getApplicationSecret(clientId!!)
                val scopes = scopeService.filterScopesForAuthorizationServerId(
                    authorizationServerId,
                    scope ?: ""
                ).filter { requestedScope ->
                    applicationSecret.scopes?.contains(requestedScope) == true
                }

                Response.ok()
                    .entity(
                        authorizationServerService.generateClientCredentialsAccessToken(
                            authorizationServerId,
                            applicationSecret.applicationId!!,
                            applicationSecret.applicationId.toString(),
                            scopes,
                            authorizationServer.clientCredentialsTokenExpiration!!
                        ).toAccessTokenResponse()
                    )
                    .build()
            }

            AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE -> {
                val clientCode = clientService.getClientCode(code!!)
                    ?: throw BadRequestException()

                if (clientCode.redirectUri != redirectUri) {
                    throw BadRequestException()
                }

                Response.ok()
                    .entity(
                        authorizationServerService.generateAuthorizationCodeFlowAccessToken(
                            authorizationServerId,
                            clientCode.userId!!,
                            clientCode.userId.toString(),
                            clientCode.clientId!!,
                            clientCode.scopes ?: emptyList(),
                            authorizationServer.authorizationCodeTokenExpiration!!,
                            clientCode.nonce
                        ).toAccessTokenResponse()
                    )
                    .build()
            }

            AccessTokenRequest.GrantTypeEnum.REFRESH_TOKEN -> {
                if (refreshToken.isNullOrEmpty()) {
                    throw BadRequestException("refresh_token is required")
                }

                if (clientId.isNullOrEmpty()) {
                    throw BadRequestException("client_id is required")
                }

                Response.ok()
                    .entity(
                        authorizationServerService.refreshAccessToken(
                            authorizationServerId,
                            refreshToken!!,
                            clientId!!
                        ).toAccessTokenResponse()
                    )
                    .build()
            }
        }
    }

    protected abstract fun renderLoginScreen(authorizationServerId: UUID): Response

    protected open fun renderLoginScreen(authorizationServerId: UUID, errorMessage: String): Response =
        renderLoginScreen(authorizationServerId)

    private fun normalizeUri(uri: URI): String {
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
            query?.let { append("?").append(it) }
            fragment?.let { append("#").append(it) }
        }
    }
}