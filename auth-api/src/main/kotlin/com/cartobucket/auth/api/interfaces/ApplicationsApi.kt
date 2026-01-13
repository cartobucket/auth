package com.cartobucket.auth.api.interfaces

import com.cartobucket.auth.api.dto.ApplicationRequest
import com.cartobucket.auth.api.dto.ApplicationResponse
import com.cartobucket.auth.api.dto.ApplicationsResponse
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

@Path("/applications")
@Tag(name = "Applications")
interface ApplicationsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new application")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Application created successfully",
            content = [Content(schema = Schema(implementation = ApplicationResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    fun createApplication(@Valid @NotNull applicationRequest: ApplicationRequest): Response

    @DELETE
    @Path("/{applicationId}")
    @Operation(summary = "Delete an application")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Application deleted successfully"),
        APIResponse(responseCode = "404", description = "Application not found")
    )
    fun deleteApplication(@PathParam("applicationId") applicationId: UUID): Response

    @GET
    @Path("/{applicationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get an application by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Application found",
            content = [Content(schema = Schema(implementation = ApplicationResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Application not found")
    )
    fun getApplication(@PathParam("applicationId") applicationId: UUID): Response

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List applications")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of applications",
            content = [Content(schema = Schema(implementation = ApplicationsResponse::class))]
        )
    )
    fun listApplications(
        @QueryParam("authorizationServerIds") authorizationServerIds: List<UUID>?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response
}
