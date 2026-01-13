package com.cartobucket.auth.api.interfaces

import com.cartobucket.auth.api.dto.GroupMemberRequest
import com.cartobucket.auth.api.dto.GroupMemberResponse
import com.cartobucket.auth.api.dto.GroupMembersResponse
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

@Path("/groups/{groupId}/members")
@Tag(name = "GroupMembers")
interface GroupMembersApi {

    @Operation(summary = "Add a member to a group")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Group member added successfully",
            content = [Content(schema = Schema(implementation = GroupMemberResponse::class))]
        ),
        APIResponse(responseCode = "400", description = "Invalid request"),
        APIResponse(responseCode = "404", description = "Group not found")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createGroupMember(
        @PathParam("groupId") groupId: UUID,
        @Valid @NotNull groupMemberRequest: GroupMemberRequest
    ): Response

    @Operation(summary = "Remove a member from a group")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Group member removed successfully"),
        APIResponse(responseCode = "404", description = "Group member not found")
    )
    @DELETE
    @Path("/{memberId}")
    fun deleteGroupMember(
        @PathParam("groupId") groupId: UUID,
        @PathParam("memberId") memberId: UUID
    ): Response

    @Operation(summary = "Get a group member by ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Group member found",
            content = [Content(schema = Schema(implementation = GroupMemberResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Group member not found")
    )
    @GET
    @Path("/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getGroupMember(
        @PathParam("groupId") groupId: UUID,
        @PathParam("memberId") memberId: UUID
    ): Response

    @Operation(summary = "List members of a group")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Group members retrieved successfully",
            content = [Content(schema = Schema(implementation = GroupMembersResponse::class))]
        ),
        APIResponse(responseCode = "404", description = "Group not found")
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listGroupMembers(
        @PathParam("groupId") groupId: UUID,
        @QueryParam("limit") limit: Int?,
        @QueryParam("offset") offset: Int?
    ): Response
}
