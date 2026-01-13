package com.cartobucket.auth.api.interfaces

import com.cartobucket.auth.api.dto.ClientRequest
import com.cartobucket.auth.api.dto.ClientResponse
import com.cartobucket.auth.api.dto.ClientsResponse
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

@Path("/clients")
@Tag(name = "Clients")
interface ClientsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new client")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Client created successfully",
            content = [Content(schema = Schema(implementation = ClientResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    fun createClient(@Valid @NotNull clientRequest: ClientRequest): Response

    @DELETE
    @Path("/{clientId}")
    @Operation(summary = "Delete a client")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Client deleted successfully"),
        APIResponse(responseCode = "404", description = "Client not found")
    )
    fun deleteClient(@PathParam("clientId") clientId: UUID): Response

    @GET
    @Path("/{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a client by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Client found",
            content = [Content(schema = Schema(implementation = ClientResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Client not found")
    )
    fun getClient(@PathParam("clientId") clientId: UUID): Response

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List clients")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of clients",
            content = [Content(schema = Schema(implementation = ClientsResponse::class))]
        )
    )
    fun listClients(
        @QueryParam("authorizationServerIds") authorizationServerIds: List<UUID>?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response

    @PUT
    @Path("/{clientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a client")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Client updated successfully",
            content = [Content(schema = Schema(implementation = ClientResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Client not found")
    )
    fun updateClient(
        @PathParam("clientId") clientId: UUID,
        @Valid @NotNull clientRequest: ClientRequest
    ): Response
}
