package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.AuthorizationServerRequest;
import com.cartobucket.auth.api.dto.AuthorizationServerResponse;
import com.cartobucket.auth.api.dto.AuthorizationServersResponse;
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

@Path("/authorizationServers")
@Tag(name = "Authorization Servers")
public interface AuthorizationServersApi {

    @Operation(summary = "Create a new authorization server")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Authorization server created successfully",
            content = @Content(schema = @Schema(implementation = AuthorizationServerResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createAuthorizationServer(@Valid @NotNull AuthorizationServerRequest authorizationServerRequest);

    @Operation(summary = "Delete an authorization server by ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Authorization server deleted successfully"),
        @APIResponse(responseCode = "404", description = "Authorization server not found")
    })
    @DELETE
    @Path("/{authorizationServerId}")
    Response deleteAuthorizationServer(@PathParam("authorizationServerId") UUID authorizationServerId);

    @Operation(summary = "Get an authorization server by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Authorization server found",
            content = @Content(schema = @Schema(implementation = AuthorizationServerResponse.class))),
        @APIResponse(responseCode = "404", description = "Authorization server not found")
    })
    @GET
    @Path("/{authorizationServerId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAuthorizationServer(@PathParam("authorizationServerId") UUID authorizationServerId);

    @Operation(summary = "List authorization servers with optional pagination")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Authorization servers retrieved successfully",
            content = @Content(schema = @Schema(implementation = AuthorizationServersResponse.class)))
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listAuthorizationServers(
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @Operation(summary = "Update an authorization server by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Authorization server updated successfully",
            content = @Content(schema = @Schema(implementation = AuthorizationServerResponse.class))),
        @APIResponse(responseCode = "404", description = "Authorization server not found")
    })
    @PUT
    @Path("/{authorizationServerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateAuthorizationServer(
        @PathParam("authorizationServerId") UUID authorizationServerId,
        @Valid @NotNull AuthorizationServerRequest authorizationServerRequest
    );
}