package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ClientRequest;
import com.cartobucket.auth.api.dto.ClientResponse;
import com.cartobucket.auth.api.dto.ClientsResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import java.util.List;
import java.util.UUID;

@Path("/clients")
@Tag(name = "Clients")
public interface ClientsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new client")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Client created successfully",
            content = @Content(schema = @Schema(implementation = ClientResponse.class))
        ),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    Response createClient(@Valid @NotNull ClientRequest clientRequest);

    @DELETE
    @Path("/{clientId}")
    @Operation(summary = "Delete a client")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Client deleted successfully"),
        @APIResponse(responseCode = "404", description = "Client not found")
    })
    Response deleteClient(@PathParam("clientId") UUID clientId);

    @GET
    @Path("/{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a client by ID")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Client found",
            content = @Content(schema = @Schema(implementation = ClientResponse.class))
        ),
        @APIResponse(responseCode = "404", description = "Client not found")
    })
    Response getClient(@PathParam("clientId") UUID clientId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List clients")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "List of clients",
            content = @Content(schema = @Schema(implementation = ClientsResponse.class))
        )
    })
    Response listClients(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{clientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a client")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Client updated successfully",
            content = @Content(schema = @Schema(implementation = ClientResponse.class))
        ),
        @APIResponse(responseCode = "404", description = "Client not found")
    })
    Response updateClient(
        @PathParam("clientId") UUID clientId,
        @Valid @NotNull ClientRequest clientRequest
    );
}