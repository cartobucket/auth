// (C)2024
package com.cartobucket.auth.api.interfaces

import com.cartobucket.auth.api.dto.ApplicationSecretRequest
import com.cartobucket.auth.api.dto.ApplicationSecretResponse
import com.cartobucket.auth.api.dto.ApplicationSecretsResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.util.UUID

@Path("/applicationSecrets")
@Tag(name = "Application Secrets")
interface ApplicationSecretsApi {
    @Operation(summary = "Create a new application secret")
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Application secret created successfully",
                content = [Content(schema = Schema(implementation = ApplicationSecretResponse::class))],
            ),
            APIResponse(responseCode = "400", description = "Invalid request"),
        ],
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createApplicationSecret(
        @Valid @NotNull applicationSecretRequest: ApplicationSecretRequest,
    ): Response

    @Operation(summary = "Delete an application secret by ID")
    @APIResponses(
        value = [
            APIResponse(responseCode = "204", description = "Application secret deleted successfully"),
            APIResponse(responseCode = "404", description = "Application secret not found"),
        ],
    )
    @DELETE
    @Path("/{secretId}/")
    fun deleteApplicationSecret(
        @PathParam("secretId") secretId: UUID,
    ): Response

    @Operation(summary = "List application secrets with optional filtering by application IDs")
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Application secrets retrieved successfully",
                content = [Content(schema = Schema(implementation = ApplicationSecretsResponse::class))],
            ),
        ],
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listApplicationSecrets(
        @QueryParam("applicationIds") applicationIds: List<UUID>?,
    ): Response
}
