package com.cartobucket.auth.api.interfaces

import com.cartobucket.auth.api.dto.TemplateRequest
import com.cartobucket.auth.api.dto.TemplateResponse
import com.cartobucket.auth.api.dto.TemplatesResponse
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

@Path("/templates")
@Tag(name = "Templates")
interface TemplatesApi {

    @Operation(summary = "Create a new template")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Template created successfully",
            content = [Content(schema = Schema(implementation = TemplateResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createTemplate(@Valid @NotNull templateRequest: TemplateRequest): Response

    @Operation(summary = "Delete a template by ID")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Template deleted successfully"),
        APIResponse(responseCode = "404", description = "Template not found")
    )
    @DELETE
    @Path("/{templateId}")
    fun deleteTemplate(@PathParam("templateId") templateId: UUID): Response

    @Operation(summary = "Get a template by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Template found",
            content = [Content(schema = Schema(implementation = TemplateResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Template not found")
    )
    @GET
    @Path("/{templateId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTemplate(@PathParam("templateId") templateId: UUID): Response

    @Operation(summary = "List templates with optional filtering")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Templates retrieved successfully",
            content = [Content(schema = Schema(implementation = TemplatesResponse::class))]
        )
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listTemplates(
        @QueryParam("authorizationServerIds") authorizationServerIds: List<UUID>?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response

    @Operation(summary = "Update a template by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Template updated successfully",
            content = [Content(schema = Schema(implementation = TemplateResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Template not found")
    )
    @PUT
    @Path("/{templateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateTemplate(
        @PathParam("templateId") templateId: UUID,
        @Valid @NotNull templateRequest: TemplateRequest
    ): Response
}
