package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ScopeRequest;
import com.cartobucket.auth.api.dto.ScopeResponse;
import com.cartobucket.auth.api.dto.ScopesResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Path("/scopes")
@Tag(name = "Scopes")
public interface ScopesApi {

    @Operation(summary = "Create a new scope")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Scope created successfully",
            content = @Content(schema = @Schema(implementation = ScopeResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createScope(@Valid @NotNull ScopeRequest scopeRequest);

    @Operation(summary = "Delete a scope by ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Scope deleted successfully"),
        @APIResponse(responseCode = "404", description = "Scope not found")
    })
    @DELETE
    @Path("/{scopeId}")
    Response deleteScope(@PathParam("scopeId") UUID scopeId);

    @Operation(summary = "Get a scope by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Scope found",
            content = @Content(schema = @Schema(implementation = ScopeResponse.class))),
        @APIResponse(responseCode = "404", description = "Scope not found")
    })
    @GET
    @Path("/{scopeId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getScope(@PathParam("scopeId") UUID scopeId);

    @Operation(summary = "List scopes with optional filtering")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Scopes retrieved successfully",
            content = @Content(schema = @Schema(implementation = ScopesResponse.class)))
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listScopes(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @Operation(summary = "Update a scope by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Scope updated successfully",
            content = @Content(schema = @Schema(implementation = ScopeResponse.class))),
        @APIResponse(responseCode = "404", description = "Scope not found")
    })
    @PUT
    @Path("/{scopeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateScope(
        @PathParam("scopeId") UUID scopeId,
        @Valid @NotNull ScopeRequest scopeRequest
    );
}