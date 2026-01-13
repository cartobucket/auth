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
import com.cartobucket.auth.api.interfaces.GroupsApi;
import com.cartobucket.auth.api.dto.GroupRequest;
import com.cartobucket.auth.api.dto.GroupsResponse;
import com.cartobucket.auth.api.server.routes.mappers.GroupMapper;
import com.cartobucket.auth.data.services.GroupService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Groups implements GroupsApi {
    final GroupService groupService;

    public Groups(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Response createGroup(GroupRequest groupRequest) {
        return Response
                .ok()
                .entity(
                        GroupMapper.toResponse(
                                groupService.createGroup(GroupMapper.from(groupRequest))
                        )
                )
                .build();
    }

    @Override
    public Response deleteGroup(UUID groupId) {
        groupService.deleteGroup(groupId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getGroup(UUID groupId) {
        return Response
                .ok()
                .entity(
                        GroupMapper.toResponse(groupService.getGroup(groupId))
                )
                .build();
    }

    @Override
    public Response listGroups(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var groupsResponse = new GroupsResponse();
        groupsResponse.setGroups(
                groupService
                        .getGroups(authorizationServerIds, new Page(limit, offset))
                        .stream()
                        .map(GroupMapper::toResponse)
                        .toList()
        );
        groupsResponse.setPage(getPage("groups", authorizationServerIds, limit, offset));
        return Response
                .ok()
                .entity(groupsResponse)
                .build();
    }

    @Override
    public Response updateGroup(UUID groupId, GroupRequest groupRequest) {
        return Response
                .ok()
                .entity(
                        GroupMapper.toResponse(
                                groupService.updateGroup(groupId, GroupMapper.from(groupRequest))
                        )
                )
                .build();
    }
}
