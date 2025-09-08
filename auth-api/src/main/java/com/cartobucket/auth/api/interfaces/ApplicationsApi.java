package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ApplicationRequest;
import com.cartobucket.auth.api.dto.ApplicationResponse;
import com.cartobucket.auth.api.dto.ApplicationsResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Path("/applications")
public interface ApplicationsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createApplication(@Valid @NotNull ApplicationRequest applicationRequest);

    @DELETE
    @Path("/{applicationId}")
    Response deleteApplication(@PathParam("applicationId") UUID applicationId);

    @GET
    @Path("/{applicationId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getApplication(@PathParam("applicationId") UUID applicationId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listApplications(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );
}