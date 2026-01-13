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

package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.GroupMember;
import com.cartobucket.auth.api.dto.GroupMemberRequest;
import com.cartobucket.auth.api.dto.GroupMemberResponse;

import java.util.UUID;

public class GroupMemberMapper {
    public static GroupMember from(UUID groupId, GroupMemberRequest groupMemberRequest) {
        var groupMember = new GroupMember();
        groupMember.setAuthorizationServerId(groupMemberRequest.getAuthorizationServerId());
        groupMember.setGroupId(groupId);
        groupMember.setMemberId(groupMemberRequest.getMemberId());
        groupMember.setMemberType(groupMemberRequest.getMemberType());
        return groupMember;
    }

    public static GroupMemberResponse toResponse(GroupMember groupMember) {
        var groupMemberResponse = new GroupMemberResponse();
        groupMemberResponse.setId(String.valueOf(groupMember.getId()));
        groupMemberResponse.setAuthorizationServerId(groupMember.getAuthorizationServerId());
        groupMemberResponse.setGroupId(groupMember.getGroupId());
        groupMemberResponse.setMemberId(groupMember.getMemberId());
        groupMemberResponse.setMemberType(groupMember.getMemberType());
        groupMemberResponse.setCreatedOn(groupMember.getCreatedOn());
        return groupMemberResponse;
    }
}
