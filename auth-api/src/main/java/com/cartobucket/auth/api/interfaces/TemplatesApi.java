package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.TemplateRequest;
import com.cartobucket.auth.api.dto.TemplateResponse;
import com.cartobucket.auth.api.dto.TemplatesResponse;
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

@Path("/templates")
@Tag(name = "Templates")
public interface TemplatesApi {

    @Operation(summary = "Create a new template")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Template created successfully",
            content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createTemplate(@Valid @NotNull TemplateRequest templateRequest);

    @Operation(summary = "Delete a template by ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Template deleted successfully"),
        @APIResponse(responseCode = "404", description = "Template not found")
    })
    @DELETE
    @Path("/{templateId}")
    Response deleteTemplate(@PathParam("templateId") UUID templateId);

    @Operation(summary = "Get a template by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Template found",
            content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
        @APIResponse(responseCode = "404", description = "Template not found")
    })
    @GET
    @Path("/{templateId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getTemplate(@PathParam("templateId") UUID templateId);

    @Operation(summary = "List templates with optional filtering")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Templates retrieved successfully",
            content = @Content(schema = @Schema(implementation = TemplatesResponse.class)))
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listTemplates(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @Operation(summary = "Update a template by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Template updated successfully",
            content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
        @APIResponse(responseCode = "404", description = "Template not found")
    })
    @PUT
    @Path("/{templateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateTemplate(
        @PathParam("templateId") UUID templateId,
        @Valid @NotNull TemplateRequest templateRequest
    );
}