/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
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

package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.data.domain.GroupMember
import com.cartobucket.auth.api.dto.GroupMemberRequest
import com.cartobucket.auth.api.dto.GroupMemberResponse
import java.util.UUID

object GroupMemberMapper {

    @JvmStatic
    fun from(groupId: UUID, groupMemberRequest: GroupMemberRequest): GroupMember {
        val groupMember = GroupMember()
        groupMember.authorizationServerId = groupMemberRequest.authorizationServerId
        groupMember.groupId = groupId
        groupMember.memberId = groupMemberRequest.memberId
        groupMember.memberType = groupMemberRequest.memberType
        return groupMember
    }

    @JvmStatic
    fun toResponse(groupMember: GroupMember): GroupMemberResponse {
        return GroupMemberResponse().apply {
            id = groupMember.id.toString()
            authorizationServerId = groupMember.authorizationServerId
            groupId = groupMember.groupId
            memberId = groupMember.memberId
            memberType = groupMember.memberType
            createdOn = groupMember.createdOn
        }
    }
}
