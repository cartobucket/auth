package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ClientRequest;
import com.cartobucket.auth.api.dto.ClientResponse;
import com.cartobucket.auth.api.dto.ClientsResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Path("/clients")
public interface ClientsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createClient(@Valid @NotNull ClientRequest clientRequest);

    @DELETE
    @Path("/{clientId}")
    Response deleteClient(@PathParam("clientId") UUID clientId);

    @GET
    @Path("/{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getClient(@PathParam("clientId") UUID clientId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listClients(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{clientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateClient(
        @PathParam("clientId") UUID clientId,
        @Valid @NotNull ClientRequest clientRequest
    );
}