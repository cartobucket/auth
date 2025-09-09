package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.UserRequest;
import com.cartobucket.auth.api.dto.UserResponse;
import com.cartobucket.auth.api.dto.UsersResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Tag(name = "Users")
public interface UsersApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createUser(@Valid @NotNull UserRequest userRequest);

    @DELETE
    @Path("/{userId}")
    Response deleteUser(@PathParam("userId") UUID userId);

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUser(@PathParam("userId") UUID userId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listUsers(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateUser(
        @PathParam("userId") UUID userId,
        @Valid @NotNull UserRequest userRequest
    );
}