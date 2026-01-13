package com.revethq.auth.core.api.interfaces

import com.revethq.auth.core.api.dto.ScopeRequest
import com.revethq.auth.core.api.dto.ScopeResponse
import com.revethq.auth.core.api.dto.ScopesResponse
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

@Path("/scopes")
@Tag(name = "Scopes")
interface ScopesApi {

    @Operation(summary = "Create a new scope")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Scope created successfully",
            content = [Content(schema = Schema(implementation = ScopeResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createScope(@Valid @NotNull scopeRequest: ScopeRequest): Response

    @Operation(summary = "Delete a scope by ID")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Scope deleted successfully"),
        APIResponse(responseCode = "404", description = "Scope not found")
    )
    @DELETE
    @Path("/{scopeId}")
    fun deleteScope(@PathParam("scopeId") scopeId: UUID): Response

    @Operation(summary = "Get a scope by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Scope found",
            content = [Content(schema = Schema(implementation = ScopeResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Scope not found")
    )
    @GET
    @Path("/{scopeId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getScope(@PathParam("scopeId") scopeId: UUID): Response

    @Operation(summary = "List scopes with optional filtering")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Scopes retrieved successfully",
            content = [Content(schema = Schema(implementation = ScopesResponse::class))]
        )
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listScopes(
        @QueryParam("authorizationServerIds") authorizationServerIds: List<UUID>?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response

    @Operation(summary = "Update a scope by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Scope updated successfully",
            content = [Content(schema = Schema(implementation = ScopeResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Scope not found")
    )
    @PUT
    @Path("/{scopeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateScope(
        @PathParam("scopeId") scopeId: UUID,
        @Valid @NotNull scopeRequest: ScopeRequest
    ): Response
}
