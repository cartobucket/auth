package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ApplicationRequest;
import com.cartobucket.auth.api.dto.ApplicationResponse;
import com.cartobucket.auth.api.dto.ApplicationsResponse;
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

@Path("/applications")
@Tag(name = "Applications")
public interface ApplicationsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new application")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Application created successfully",
            content = @Content(schema = @Schema(implementation = ApplicationResponse.class))
        ),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    Response createApplication(@Valid @NotNull ApplicationRequest applicationRequest);

    @DELETE
    @Path("/{applicationId}")
    @Operation(summary = "Delete an application")
    @APIResponses(value = {
        @APIResponse(responseCode = "204", description = "Application deleted successfully"),
        @APIResponse(responseCode = "404", description = "Application not found")
    })
    Response deleteApplication(@PathParam("applicationId") UUID applicationId);

    @GET
    @Path("/{applicationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get an application by ID")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Application found",
            content = @Content(schema = @Schema(implementation = ApplicationResponse.class))
        ),
        @APIResponse(responseCode = "404", description = "Application not found")
    })
    Response getApplication(@PathParam("applicationId") UUID applicationId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List applications")
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "List of applications",
            content = @Content(schema = @Schema(implementation = ApplicationsResponse.class))
        )
    })
    Response listApplications(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );
}