package com.cartobucket.auth.api.interfaces;

import com.cartobucket.auth.api.dto.UserRequest;
import com.cartobucket.auth.api.dto.UserResponse;
import com.cartobucket.auth.api.dto.UsersResponse;
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

@Path("/users")
@Tag(name = "Users")
public interface UsersApi {

    @Operation(summary = "Create a new user")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @APIResponse(responseCode = "400", description = "Invalid request")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createUser(@Valid @NotNull UserRequest userRequest);

    @Operation(summary = "Delete a user by ID")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "User deleted successfully"),
        @APIResponse(responseCode = "404", description = "User not found")
    })
    @DELETE
    @Path("/{userId}")
    Response deleteUser(@PathParam("userId") UUID userId);

    @Operation(summary = "Get a user by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @APIResponse(responseCode = "404", description = "User not found")
    })
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUser(@PathParam("userId") UUID userId);

    @Operation(summary = "List users with optional filtering")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = UsersResponse.class)))
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listUsers(
        @QueryParam("authorizationServerIds") List<UUID> authorizationServerIds,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset
    );

    @Operation(summary = "Update a user by ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @APIResponse(responseCode = "404", description = "User not found")
    })
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateUser(
        @PathParam("userId") UUID userId,
        @Valid @NotNull UserRequest userRequest
    );
}