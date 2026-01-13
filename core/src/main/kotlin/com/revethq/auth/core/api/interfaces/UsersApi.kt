package com.revethq.auth.core.api.interfaces

import com.revethq.auth.core.api.dto.UserRequest
import com.revethq.auth.core.api.dto.UserResponse
import com.revethq.auth.core.api.dto.UsersResponse
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

@Path("/users")
@Tag(name = "Users")
interface UsersApi {

    @Operation(summary = "Create a new user")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "User created successfully",
            content = [Content(schema = Schema(implementation = UserResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(@Valid @NotNull userRequest: UserRequest): Response

    @Operation(summary = "Delete a user by ID")
    @APIResponses(
        APIResponse(responseCode = "204", description = "User deleted successfully"),
        APIResponse(responseCode = "404", description = "User not found")
    )
    @DELETE
    @Path("/{userId}")
    fun deleteUser(@PathParam("userId") userId: UUID): Response

    @Operation(summary = "Get a user by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "User found",
            content = [Content(schema = Schema(implementation = UserResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "User not found")
    )
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getUser(@PathParam("userId") userId: UUID): Response

    @Operation(summary = "List users with optional filtering")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = [Content(schema = Schema(implementation = UsersResponse::class))]
        )
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listUsers(
        @QueryParam("authorizationServerIds") authorizationServerIds: List<UUID>?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response

    @Operation(summary = "Update a user by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = [Content(schema = Schema(implementation = UserResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "User not found")
    )
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateUser(
        @PathParam("userId") userId: UUID,
        @Valid @NotNull userRequest: UserRequest
    ): Response
}
