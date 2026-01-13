/*
 * Copyright 2023 Bryce Groff (Revet)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.revethq.auth.web.api.routes

import com.revethq.auth.core.api.dto.GroupMemberRequest
import com.revethq.auth.core.api.dto.GroupMembersResponse
import com.revethq.auth.core.api.dto.Page as PageDto
import com.revethq.auth.core.api.interfaces.GroupMembersApi
import com.revethq.auth.web.api.routes.Constants.LIMIT_DEFAULT
import com.revethq.auth.web.api.routes.Constants.OFFSET_DEFAULT
import com.revethq.auth.web.api.routes.mappers.GroupMemberMapper
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.services.GroupMemberService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class GroupMembers @Inject constructor(
    private val groupMemberService: GroupMemberService
) : GroupMembersApi {

    override fun createGroupMember(groupId: UUID, groupMemberRequest: GroupMemberRequest): Response {
        return Response
            .ok()
            .entity(
                GroupMemberMapper.toResponse(
                    groupMemberService.createGroupMember(GroupMemberMapper.from(groupId, groupMemberRequest))
                )
            )
            .build()
    }

    override fun deleteGroupMember(groupId: UUID, memberId: UUID): Response {
        groupMemberService.deleteGroupMember(memberId)
        return Response.noContent().build()
    }

    override fun getGroupMember(groupId: UUID, memberId: UUID): Response {
        return Response
            .ok()
            .entity(GroupMemberMapper.toResponse(groupMemberService.getGroupMember(memberId)))
            .build()
    }

    override fun listGroupMembers(groupId: UUID, limit: Int?, offset: Int?): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT

        val groupMembersResponse = GroupMembersResponse()
        groupMembersResponse.groupMembers = groupMemberService
            .getGroupMembers(emptyList(), listOf(groupId), Page(actualLimit, actualOffset))
            .map { GroupMemberMapper.toResponse(it) }
        groupMembersResponse.page = getGroupMembersPage(groupId, actualLimit, actualOffset)
        return Response.ok().entity(groupMembersResponse).build()
    }

    private fun getGroupMembersPage(groupId: UUID, limit: Int, offset: Int): PageDto {
        val page = PageDto()
        val next = "/groups/$groupId/members?limit=$limit&offset=${offset + limit}"
        page.next = next
        if (offset > 0) {
            val previous = "/groups/$groupId/members?limit=$limit&offset=${offset - limit}"
            page.previous = previous
        }
        return page
    }
}
