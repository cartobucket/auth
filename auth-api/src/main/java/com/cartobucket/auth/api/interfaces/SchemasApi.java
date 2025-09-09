package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.SchemaRequest;
import com.cartobucket.auth.api.dto.SchemaResponse;
import com.cartobucket.auth.api.dto.SchemasResponse;
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

@Path("/schemas")
@Tag(name = "Schemas")
public interface SchemasApi {

    @Operation(summary = "Create a new schema")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Schema created successfully",
            content = @Content(schema = @Schema(implementation = SchemaResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createSchema(@Valid @NotNull SchemaRequest schemaRequest);

    @Operation(summary = "Delete a schema by ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Schema deleted successfully"),
        @APIResponse(responseCode = "404", description = "Schema not found")
    })
    @DELETE
    @Path("/{schemaId}")
    Response deleteSchema(@PathParam("schemaId") UUID schemaId);

    @Operation(summary = "Get a schema by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Schema found",
            content = @Content(schema = @Schema(implementation = SchemaResponse.class))),
        @APIResponse(responseCode = "404", description = "Schema not found")
    })
    @GET
    @Path("/{schemaId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getSchema(@PathParam("schemaId") UUID schemaId);

    @Operation(summary = "List schemas with optional filtering")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Schemas retrieved successfully",
            content = @Content(schema = @Schema(implementation = SchemasResponse.class)))
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listSchemas(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @Operation(summary = "Update a schema by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Schema updated successfully",
            content = @Content(schema = @Schema(implementation = SchemaResponse.class))),
        @APIResponse(responseCode = "404", description = "Schema not found")
    })
    @PUT
    @Path("/{schemaId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateSchema(
        @PathParam("schemaId") UUID schemaId,
        @Valid @NotNull SchemaRequest schemaRequest
    );
}