package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.AuthorizationServerRequest;
import com.cartobucket.auth.api.dto.AuthorizationServerResponse;
import com.cartobucket.auth.api.dto.AuthorizationServersResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Path("/authorizationServers")
public interface AuthorizationServersApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createAuthorizationServer(@Valid @NotNull AuthorizationServerRequest authorizationServerRequest);

    @DELETE
    @Path("/{authorizationServerId}")
    Response deleteAuthorizationServer(@PathParam("authorizationServerId") UUID authorizationServerId);

    @GET
    @Path("/{authorizationServerId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAuthorizationServer(@PathParam("authorizationServerId") UUID authorizationServerId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listAuthorizationServers(
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{authorizationServerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateAuthorizationServer(
        @PathParam("authorizationServerId") UUID authorizationServerId,
        @Valid @NotNull AuthorizationServerRequest authorizationServerRequest
    );
}