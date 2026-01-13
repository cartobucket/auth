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

import com.revethq.auth.core.api.dto.GroupRequest
import com.revethq.auth.core.api.dto.GroupsResponse
import com.revethq.auth.core.api.interfaces.GroupsApi
import com.revethq.auth.web.api.routes.Constants.LIMIT_DEFAULT
import com.revethq.auth.web.api.routes.Constants.OFFSET_DEFAULT
import com.revethq.auth.web.api.routes.Pagination.getPage
import com.revethq.auth.web.api.routes.mappers.GroupMapper
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.services.GroupService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class Groups @Inject constructor(
    private val groupService: GroupService
) : GroupsApi {

    override fun createGroup(groupRequest: GroupRequest): Response {
        return Response
            .ok()
            .entity(
                GroupMapper.toResponse(
                    groupService.createGroup(GroupMapper.from(groupRequest))
                )
            )
            .build()
    }

    override fun deleteGroup(groupId: UUID): Response {
        groupService.deleteGroup(groupId)
        return Response.noContent().build()
    }

    override fun getGroup(groupId: UUID): Response {
        return Response
            .ok()
            .entity(GroupMapper.toResponse(groupService.getGroup(groupId)))
            .build()
    }

    override fun listGroups(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val serverIds = authorizationServerIds ?: emptyList()

        val groupsResponse = GroupsResponse()
        groupsResponse.groups = groupService
            .getGroups(serverIds, Page(actualLimit, actualOffset))
            .map { GroupMapper.toResponse(it) }
        groupsResponse.page = getPage("groups", serverIds, actualLimit, actualOffset)
        return Response.ok().entity(groupsResponse).build()
    }

    override fun updateGroup(groupId: UUID, groupRequest: GroupRequest): Response {
        return Response
            .ok()
            .entity(
                GroupMapper.toResponse(
                    groupService.updateGroup(groupId, GroupMapper.from(groupRequest))
                )
            )
            .build()
    }
}
