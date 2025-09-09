package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.SchemaRequest;
import com.cartobucket.auth.api.dto.SchemaResponse;
import com.cartobucket.auth.api.dto.SchemasResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Path("/schemas")
@Tag(name = "Schemas")
public interface SchemasApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createSchema(@Valid @NotNull SchemaRequest schemaRequest);

    @DELETE
    @Path("/{schemaId}")
    Response deleteSchema(@PathParam("schemaId") UUID schemaId);

    @GET
    @Path("/{schemaId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getSchema(@PathParam("schemaId") UUID schemaId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listSchemas(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{schemaId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateSchema(
        @PathParam("schemaId") UUID schemaId,
        @Valid @NotNull SchemaRequest schemaRequest
    );
}