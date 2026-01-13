package com.revethq.auth.core.api.interfaces

import com.revethq.auth.core.api.dto.AuthorizationServerRequest
import com.revethq.auth.core.api.dto.AuthorizationServerResponse
import com.revethq.auth.core.api.dto.AuthorizationServersResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.util.UUID

@Path("/authorizationServers")
@Tag(name = "Authorization Servers")
interface AuthorizationServersApi {

    @Operation(summary = "Create a new authorization server")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Authorization server created successfully",
            content = [Content(schema = Schema(implementation = AuthorizationServerResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createAuthorizationServer(@Valid @NotNull authorizationServerRequest: AuthorizationServerRequest): Response

    @Operation(summary = "Delete an authorization server by ID")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Authorization server deleted successfully"),
        APIResponse(responseCode = "404", description = "Authorization server not found")
    )
    @DELETE
    @Path("/{authorizationServerId}")
    fun deleteAuthorizationServer(@PathParam("authorizationServerId") authorizationServerId: UUID): Response

    @Operation(summary = "Get an authorization server by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Authorization server found",
            content = [Content(schema = Schema(implementation = AuthorizationServerResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Authorization server not found")
    )
    @GET
    @Path("/{authorizationServerId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAuthorizationServer(@PathParam("authorizationServerId") authorizationServerId: UUID): Response

    @Operation(summary = "List authorization servers with optional pagination")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Authorization servers retrieved successfully",
            content = [Content(schema = Schema(implementation = AuthorizationServersResponse::class))]
        )
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listAuthorizationServers(
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response

    @Operation(summary = "Update an authorization server by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Authorization server updated successfully",
            content = [Content(schema = Schema(implementation = AuthorizationServerResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Authorization server not found")
    )
    @PUT
    @Path("/{authorizationServerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateAuthorizationServer(
        @PathParam("authorizationServerId") authorizationServerId: UUID,
        @Valid @NotNull authorizationServerRequest: AuthorizationServerRequest
    ): Response
}
