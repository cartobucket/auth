package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ScopeRequest;
import com.cartobucket.auth.api.dto.ScopeResponse;
import com.cartobucket.auth.api.dto.ScopesResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Path("/scopes")
public interface ScopesApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createScope(@Valid @NotNull ScopeRequest scopeRequest);

    @DELETE
    @Path("/{scopeId}")
    Response deleteScope(@PathParam("scopeId") UUID scopeId);

    @GET
    @Path("/{scopeId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getScope(@PathParam("scopeId") UUID scopeId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listScopes(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{scopeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateScope(
        @PathParam("scopeId") UUID scopeId,
        @Valid @NotNull ScopeRequest scopeRequest
    );
}