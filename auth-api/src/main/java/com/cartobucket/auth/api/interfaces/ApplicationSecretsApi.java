package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ApplicationSecretRequest;
import com.cartobucket.auth.api.dto.ApplicationSecretResponse;
import com.cartobucket.auth.api.dto.ApplicationSecretsResponse;
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

@Path("/applicationSecrets")
@Tag(name = "Application Secrets")
public interface ApplicationSecretsApi {

    @Operation(summary = "Create a new application secret")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Application secret created successfully",
            content = @Content(schema = @Schema(implementation = ApplicationSecretResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createApplicationSecret(@Valid @NotNull ApplicationSecretRequest applicationSecretRequest);

    @Operation(summary = "Delete an application secret by ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Application secret deleted successfully"),
        @APIResponse(responseCode = "404", description = "Application secret not found")
    })
    @DELETE
    @Path("/{secretId}/")
    Response deleteApplicationSecret(@PathParam("secretId") UUID secretId);

    @Operation(summary = "List application secrets with optional filtering by application IDs")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Application secrets retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApplicationSecretsResponse.class)))
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listApplicationSecrets(@QueryParam("applicationIds") List<UUID> applicationIds);
}