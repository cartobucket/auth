package com.cartobucket.auth.api.interfaces

import com.cartobucket.auth.api.dto.GroupRequest
import com.cartobucket.auth.api.dto.GroupResponse
import com.cartobucket.auth.api.dto.GroupsResponse
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

@Path("/groups")
@Tag(name = "Groups")
interface GroupsApi {

    @Operation(summary = "Create a new group")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Group created successfully",
            content = [Content(schema = Schema(implementation = GroupResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createGroup(@Valid @NotNull groupRequest: GroupRequest): Response

    @Operation(summary = "Delete a group by ID")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Group deleted successfully"),
        APIResponse(responseCode = "404", description = "Group not found")
    )
    @DELETE
    @Path("/{groupId}")
    fun deleteGroup(@PathParam("groupId") groupId: UUID): Response

    @Operation(summary = "Get a group by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Group found",
            content = [Content(schema = Schema(implementation = GroupResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Group not found")
    )
    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getGroup(@PathParam("groupId") groupId: UUID): Response

    @Operation(summary = "List groups with optional filtering")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Groups retrieved successfully",
            content = [Content(schema = Schema(implementation = GroupsResponse::class))]
        )
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listGroups(
        @QueryParam("authorizationServerIds") authorizationServerIds: List<UUID>?,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response

    @Operation(summary = "Update a group by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Group updated successfully",
            content = [Content(schema = Schema(implementation = GroupResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Group not found")
    )
    @PUT
    @Path("/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateGroup(
        @PathParam("groupId") groupId: UUID,
        @Valid @NotNull groupRequest: GroupRequest
    ): Response
}
