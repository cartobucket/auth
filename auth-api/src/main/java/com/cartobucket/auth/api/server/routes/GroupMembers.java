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

package com.cartobucket.auth.api.server.routes;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.api.interfaces.GroupMembersApi;
import com.cartobucket.auth.api.dto.GroupMemberRequest;
import com.cartobucket.auth.api.dto.GroupMembersResponse;
import com.cartobucket.auth.api.server.routes.mappers.GroupMemberMapper;
import com.cartobucket.auth.data.services.GroupMemberService;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;

public class GroupMembers implements GroupMembersApi {
    final GroupMemberService groupMemberService;

    public GroupMembers(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @Override
    public Response createGroupMember(UUID groupId, GroupMemberRequest groupMemberRequest) {
        return Response
                .ok()
                .entity(
                        GroupMemberMapper.toResponse(
                                groupMemberService.createGroupMember(GroupMemberMapper.from(groupId, groupMemberRequest))
                        )
                )
                .build();
    }

    @Override
    public Response deleteGroupMember(UUID groupId, UUID memberId) {
        groupMemberService.deleteGroupMember(memberId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getGroupMember(UUID groupId, UUID memberId) {
        return Response
                .ok()
                .entity(
                        GroupMemberMapper.toResponse(groupMemberService.getGroupMember(memberId))
                )
                .build();
    }

    @Override
    public Response listGroupMembers(UUID groupId, Integer limit, Integer offset) {
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var groupMembersResponse = new GroupMembersResponse();
        groupMembersResponse.setGroupMembers(
                groupMemberService
                        .getGroupMembers(Collections.emptyList(), Collections.singletonList(groupId), new Page(limit, offset))
                        .stream()
                        .map(GroupMemberMapper::toResponse)
                        .toList()
        );
        groupMembersResponse.setPage(getGroupMembersPage(groupId, limit, offset));
        return Response
                .ok()
                .entity(groupMembersResponse)
                .build();
    }

    private com.cartobucket.auth.api.dto.Page getGroupMembersPage(UUID groupId, Integer limit, Integer offset) {
        var page = new com.cartobucket.auth.api.dto.Page();
        var next = "/groups/" + groupId + "/members?limit=" + limit + "&offset=" + (offset + limit);
        page.setNext(next);
        if (offset > 0) {
            var previous = "/groups/" + groupId + "/members?limit=" + limit + "&offset=" + (offset - limit);
            page.setPrevious(previous);
        }
        return page;
    }
}
