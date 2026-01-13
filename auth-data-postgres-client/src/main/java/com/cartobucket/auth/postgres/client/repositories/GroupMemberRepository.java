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

package com.cartobucket.auth.postgres.client.repositories;

import com.cartobucket.auth.postgres.client.entities.GroupMember;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GroupMemberRepository implements PanacheRepositoryBase<GroupMember, UUID> {

    public List<GroupMember> findAllByGroupId(UUID groupId) {
        return list("groupId = ?1", groupId);
    }

    public List<GroupMember> findAllByGroupIdIn(List<UUID> groupIds) {
        return list("groupId in ?1", groupIds);
    }

    public List<GroupMember> findAllByAuthorizationServerIdIn(List<UUID> authorizationServerIds) {
        return list("authorizationServerId in ?1", authorizationServerIds);
    }

    public void deleteByGroupId(UUID groupId) {
        delete("groupId = ?1", groupId);
    }
}
