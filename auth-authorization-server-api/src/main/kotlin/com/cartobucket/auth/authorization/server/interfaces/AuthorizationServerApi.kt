// (C)2024
package com.cartobucket.auth.authorization.server.interfaces

import com.cartobucket.auth.authorization.server.dto.AccessTokenRequest
import com.cartobucket.auth.authorization.server.dto.AccessTokenResponse
import com.cartobucket.auth.authorization.server.dto.JWKS
import com.cartobucket.auth.authorization.server.dto.WellKnown
import com.cartobucket.auth.authorization.server.validators.ValidAuthorizationServer
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.util.UUID

@Path("/{authorizationServerId}")
@Tag(name = "Authorization Server")
interface AuthorizationServerApi {
    @GET
    @Path("/.well-known/openid-connect/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Well Known Endpoint",
        description = "Returns the OpenID Connect Well-Known configuration",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Returns a well-known configuration object",
                content = [Content(schema = Schema(implementation = WellKnown::class))],
            ),
        ],
    )
    fun getOpenIdConnectionWellKnown(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
    ): Response

    @GET
    @Path("/authorization/")
    @Produces(MediaType.TEXT_HTML)
    @Operation(
        summary = "Authorization Endpoint",
        description = "OAuth2/OIDC authorization endpoint",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Returns authorization form",
            ),
            APIResponse(
                responseCode = "302",
                description = "Redirect to client with authorization code",
            ),
        ],
    )
    fun getAuthorization(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
        @QueryParam("client_id")
        @NotNull
        @Parameter(description = "Client identifier", required = true)
        clientId: String?,
        @QueryParam("response_type")
        @NotNull
        @Parameter(description = "Response type", required = true)
        responseType: String?,
        @QueryParam("redirect_uri")
        @Parameter(description = "Redirect URI")
        redirectUri: String?,
        @QueryParam("scope")
        @Parameter(description = "Requested scopes")
        scope: String?,
        @QueryParam("state")
        @Parameter(description = "State parameter")
        state: String?,
        @QueryParam("nonce")
        @Parameter(description = "Nonce for ID token")
        nonce: String?,
        @QueryParam("code_challenge")
        @Parameter(description = "PKCE code challenge")
        codeChallenge: String?,
        @QueryParam("code_challenge_method")
        @Parameter(description = "PKCE code challenge method")
        codeChallengeMethod: String?,
    ): Response

    @POST
    @Path("/authorization/")
    @Consumes("multipart/form-data", MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    @Operation(
        summary = "Process Authorization",
        description = "Process authorization with credentials",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Rerender the form when the user does not provide valid credentials",
            ),
            APIResponse(
                responseCode = "302",
                description = "Redirect the user back to the redirect uri on success",
            ),
        ],
    )
    fun createAuthorizationCode(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
        @QueryParam("client_id")
        @NotNull
        @Parameter(description = "Client identifier", required = true)
        clientId: String?,
        @QueryParam("response_type")
        @NotNull
        @Parameter(description = "Response type", required = true)
        responseType: String?,
        @QueryParam("code_challenge")
        @Parameter(description = "PKCE code challenge")
        codeChallenge: String?,
        @QueryParam("code_challenge_method")
        @Parameter(description = "PKCE code challenge method")
        codeChallengeMethod: String?,
        @QueryParam("redirect_uri")
        @Parameter(description = "Redirect URI")
        redirectUri: String?,
        @QueryParam("scope")
        @Parameter(description = "Requested scopes")
        scope: String?,
        @QueryParam("state")
        @Parameter(description = "State parameter")
        state: String?,
        @QueryParam("nonce")
        @Parameter(description = "Nonce for ID token")
        nonce: String?,
        @FormParam("username")
        @Parameter(description = "Username")
        username: String?,
        @FormParam("password")
        @Parameter(description = "Password")
        password: String?,
    ): Response

    @POST
    @Path("/token/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Token Endpoint (Form Data)",
        description = "OAuth2/OIDC token endpoint with form data",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Returns access token",
                content = [Content(schema = Schema(implementation = AccessTokenResponse::class))],
            ),
            APIResponse(
                responseCode = "400",
                description = "Invalid request",
            ),
            APIResponse(
                responseCode = "401",
                description = "Invalid client credentials",
            ),
        ],
    )
    fun postToken(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
        @FormParam("grant_type")
        @Parameter(description = "Grant type", required = true)
        grantType: String?,
        @FormParam("client_id")
        @Parameter(description = "Client identifier", required = true)
        clientId: String?,
        @FormParam("client_secret")
        @Parameter(description = "Client secret")
        clientSecret: String?,
        @FormParam("code")
        @Parameter(description = "Authorization code")
        code: String?,
        @FormParam("redirect_uri")
        @Parameter(description = "Redirect URI")
        redirectUri: String?,
        @FormParam("code_verifier")
        @Parameter(description = "PKCE code verifier")
        codeVerifier: String?,
        @FormParam("refresh_token")
        @Parameter(description = "Refresh token")
        refreshToken: String?,
        @FormParam("scope")
        @Parameter(description = "Requested scopes")
        scope: String?,
    ): Response

    @POST
    @Path("/token/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Token Endpoint (JSON)",
        description = "OAuth2/OIDC token endpoint with JSON request body",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Returns access token",
                content = [Content(schema = Schema(implementation = AccessTokenResponse::class))],
            ),
            APIResponse(
                responseCode = "400",
                description = "Invalid request",
            ),
            APIResponse(
                responseCode = "401",
                description = "Invalid client credentials",
            ),
        ],
    )
    fun postTokenJson(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
        @Parameter(description = "Access token request", required = true)
        request: AccessTokenRequest?,
    ): Response

    @GET
    @Path("/jwks/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "JWKS Endpoint",
        description = "JSON Web Key Set endpoint",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Returns JWKS",
                content = [Content(schema = Schema(implementation = JWKS::class))],
            ),
        ],
    )
    fun getAuthorizationServerJwks(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
    ): Response

    @GET
    @Path("/userinfo/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "User Info",
        description = "OpenID Connect UserInfo endpoint",
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Returns user information",
            ),
            APIResponse(
                responseCode = "401",
                description = "Unauthorized",
            ),
        ],
    )
    fun getUserInfo(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        authorizationServerId: UUID,
        @HeaderParam("Authorization")
        @NotNull
        @Parameter(description = "Bearer token", required = true)
        authorization: String?,
    ): Response
}
