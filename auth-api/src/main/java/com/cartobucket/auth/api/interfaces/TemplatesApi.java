package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.TemplateRequest;
import com.cartobucket.auth.api.dto.TemplateResponse;
import com.cartobucket.auth.api.dto.TemplatesResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Path("/templates")
public interface TemplatesApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createTemplate(@Valid @NotNull TemplateRequest templateRequest);

    @DELETE
    @Path("/{templateId}")
    Response deleteTemplate(@PathParam("templateId") UUID templateId);

    @GET
    @Path("/{templateId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getTemplate(@PathParam("templateId") UUID templateId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listTemplates(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{templateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateTemplate(
        @PathParam("templateId") UUID templateId,
        @Valid @NotNull TemplateRequest templateRequest
    );
}