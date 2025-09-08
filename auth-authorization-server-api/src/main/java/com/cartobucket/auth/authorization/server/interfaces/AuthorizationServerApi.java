package com.cartobucket.auth.authorization.server.interfaces;

import com.cartobucket.auth.authorization.server.dto.*;
import com.cartobucket.auth.authorization.server.validators.ValidAuthorizationServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;

@Path("/{authorizationServerId}")
@Tag(name = "AuthorizationServer", description = "Authorization Server endpoints")
public interface AuthorizationServerApi {
    
    @GET
    @Path("/.well-known/openid-connect/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Well Known Endpoint",
        description = "Returns the OpenID Connect Well-Known configuration"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns a well-known configuration object",
            content = @Content(schema = @Schema(implementation = WellKnown.class))
        )
    })
    Response getOpenIdConnectionWellKnown(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId
    );
    
    @GET
    @Path("/authorization/")
    @Produces(MediaType.TEXT_HTML)
    @Operation(
        summary = "Authorization Endpoint",
        description = "OAuth2/OIDC authorization endpoint"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns authorization form"
        ),
        @ApiResponse(
            responseCode = "302",
            description = "Redirect to client with authorization code"
        )
    })
    Response getAuthorization(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId,
        
        @QueryParam("client_id")
        @NotNull
        @Parameter(description = "Client identifier", required = true)
        String clientId,
        
        @QueryParam("response_type")
        @NotNull
        @Parameter(description = "Response type", required = true, schema = @Schema(allowableValues = {"code", "token"}))
        String responseType,
        
        @QueryParam("redirect_uri")
        @Parameter(description = "Redirect URI")
        String redirectUri,
        
        @QueryParam("scope")
        @Parameter(description = "Requested scopes")
        String scope,
        
        @QueryParam("state")
        @Parameter(description = "State parameter")
        String state,
        
        @QueryParam("nonce")
        @Parameter(description = "Nonce for ID token")
        String nonce,
        
        @QueryParam("code_challenge")
        @Parameter(description = "PKCE code challenge")
        String codeChallenge,
        
        @QueryParam("code_challenge_method")
        @Parameter(description = "PKCE code challenge method", schema = @Schema(allowableValues = {"plain", "S256"}))
        String codeChallengeMethod
    );
    
    @POST
    @Path("/authorization/")
    @Consumes({"multipart/form-data", MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_HTML)
    @Operation(
        summary = "Process Authorization",
        description = "Process authorization with credentials"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rerender the form when the user does not provide valid credentials"
        ),
        @ApiResponse(
            responseCode = "302",
            description = "Redirect the user back to the redirect uri on success"
        )
    })
    Response createAuthorizationCode(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId,
        
        @QueryParam("client_id")
        @NotNull
        @Parameter(description = "Client identifier", required = true)
        String clientId,
        
        @QueryParam("response_type")
        @NotNull
        @Parameter(description = "Response type", required = true)
        String responseType,
        
        @QueryParam("code_challenge")
        @Parameter(description = "PKCE code challenge")
        String codeChallenge,
        
        @QueryParam("code_challenge_method")
        @Parameter(description = "PKCE code challenge method")
        String codeChallengeMethod,
        
        @QueryParam("redirect_uri")
        @Parameter(description = "Redirect URI")
        String redirectUri,
        
        @QueryParam("scope")
        @Parameter(description = "Requested scopes")
        String scope,
        
        @QueryParam("state")
        @Parameter(description = "State parameter")
        String state,
        
        @QueryParam("nonce")
        @Parameter(description = "Nonce for ID token")
        String nonce,
        
        @FormParam("username")
        @Parameter(description = "Username")
        String username,
        
        @FormParam("password")
        @Parameter(description = "Password")
        String password
    );
    
    @POST
    @Path("/token/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Token Endpoint (Form Data)",
        description = "OAuth2/OIDC token endpoint with form data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns access token",
            content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid client credentials"
        )
    })
    Response postToken(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId,
        
        @FormParam("grant_type")
        @Parameter(description = "Grant type", required = true, schema = @Schema(allowableValues = {"authorization_code", "client_credentials", "refresh_token"}))
        String grantType,
        
        @FormParam("client_id")
        @Parameter(description = "Client identifier", required = true)
        String clientId,
        
        @FormParam("client_secret")
        @Parameter(description = "Client secret")
        String clientSecret,
        
        @FormParam("code")
        @Parameter(description = "Authorization code")
        String code,
        
        @FormParam("redirect_uri")
        @Parameter(description = "Redirect URI")
        String redirectUri,
        
        @FormParam("code_verifier")
        @Parameter(description = "PKCE code verifier")
        String codeVerifier,
        
        @FormParam("refresh_token")
        @Parameter(description = "Refresh token")
        String refreshToken,
        
        @FormParam("scope")
        @Parameter(description = "Requested scopes")
        String scope
    );
    
    @POST
    @Path("/token/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Token Endpoint (JSON)",
        description = "OAuth2/OIDC token endpoint with JSON request body"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns access token",
            content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid client credentials"
        )
    })
    Response postTokenJson(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId,
        
        @Parameter(description = "Access token request", required = true)
        AccessTokenRequest request
    );
    
    @GET
    @Path("/jwks/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "JWKS Endpoint",
        description = "JSON Web Key Set endpoint"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns JWKS",
            content = @Content(schema = @Schema(implementation = JWKS.class))
        )
    })
    Response getAuthorizationServerJwks(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId
    );
    
    @GET
    @Path("/userinfo/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "User Info",
        description = "OpenID Connect UserInfo endpoint"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Returns user information"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized"
        )
    })
    Response getUserInfo(
        @ValidAuthorizationServer
        @PathParam("authorizationServerId")
        @Parameter(description = "Authorization Server ID", required = true)
        UUID authorizationServerId,
        
        @HeaderParam("Authorization")
        @NotNull
        @Parameter(description = "Bearer token", required = true)
        String authorization
    );
}