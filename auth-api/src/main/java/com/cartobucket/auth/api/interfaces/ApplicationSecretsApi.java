package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.ApplicationSecretRequest;
import com.cartobucket.auth.api.dto.ApplicationSecretResponse;
import com.cartobucket.auth.api.dto.ApplicationSecretsResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Path("/applicationSecrets")
public interface ApplicationSecretsApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createApplicationSecret(@Valid @NotNull ApplicationSecretRequest applicationSecretRequest);

    @DELETE
    @Path("/{secretId}/")
    Response deleteApplicationSecret(@PathParam("secretId") UUID secretId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listApplicationSecrets(@QueryParam("applicationIds") List<UUID> applicationIds);
}