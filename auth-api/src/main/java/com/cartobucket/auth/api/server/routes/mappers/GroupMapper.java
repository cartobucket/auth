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

import com.cartobucket.auth.data.domain.Group;
import com.cartobucket.auth.api.dto.GroupRequest;
import com.cartobucket.auth.api.dto.GroupResponse;

public class GroupMapper {
    public static Group from(GroupRequest groupRequest) {
        var group = new Group();
        group.setAuthorizationServerId(groupRequest.getAuthorizationServerId());
        group.setDisplayName(groupRequest.getDisplayName());
        group.setExternalId(groupRequest.getExternalId());
        group.setMetadata(MetadataMapper.from(groupRequest.getMetadata()));
        return group;
    }

    public static GroupResponse toResponse(Group group) {
        var groupResponse = new GroupResponse();
        groupResponse.setId(String.valueOf(group.getId()));
        groupResponse.setAuthorizationServerId(group.getAuthorizationServerId());
        groupResponse.setDisplayName(group.getDisplayName());
        groupResponse.setExternalId(group.getExternalId());
        groupResponse.setMetadata(MetadataMapper.to(group.getMetadata()));
        groupResponse.setCreatedOn(group.getCreatedOn());
        groupResponse.setUpdatedOn(group.getUpdatedOn());
        return groupResponse;
    }
}
