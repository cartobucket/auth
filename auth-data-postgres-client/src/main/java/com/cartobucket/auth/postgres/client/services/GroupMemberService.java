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

package com.cartobucket.auth.postgres.client.services;

import com.cartobucket.auth.data.domain.GroupMember;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.exceptions.notfound.GroupMemberNotFound;
import com.cartobucket.auth.postgres.client.entities.EventType;
import com.cartobucket.auth.postgres.client.entities.mappers.GroupMemberMapper;
import com.cartobucket.auth.postgres.client.repositories.EventRepository;
import com.cartobucket.auth.postgres.client.repositories.GroupMemberRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GroupMemberService implements com.cartobucket.auth.data.services.GroupMemberService {

    final EventRepository eventRepository;
    final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(
            EventRepository eventRepository,
            GroupMemberRepository groupMemberRepository
    ) {
        this.eventRepository = eventRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Override
    @Transactional
    public List<GroupMember> getGroupMembers(List<UUID> authorizationServerIds, List<UUID> groupIds, Page page) {
        if (!groupIds.isEmpty()) {
            return groupMemberRepository
                    .find("groupId in ?1", Sort.descending("createdOn"), groupIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(GroupMemberMapper::from)
                    .toList();
        } else if (!authorizationServerIds.isEmpty()) {
            return groupMemberRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(GroupMemberMapper::from)
                    .toList();
        } else {
            return groupMemberRepository.findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(GroupMemberMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public GroupMember createGroupMember(GroupMember groupMember) {
        groupMember.setCreatedOn(OffsetDateTime.now());
        var _groupMember = GroupMemberMapper.to(groupMember);
        groupMemberRepository.persist(_groupMember);
        eventRepository.createGroupMemberEvent(GroupMemberMapper.from(_groupMember), EventType.CREATE);
        return GroupMemberMapper.from(_groupMember);
    }

    @Override
    @Transactional
    public void deleteGroupMember(UUID groupMemberId) throws GroupMemberNotFound {
        var groupMember = groupMemberRepository
                .findByIdOptional(groupMemberId)
                .orElseThrow(GroupMemberNotFound::new);

        groupMemberRepository.delete(groupMember);
        eventRepository.createGroupMemberEvent(GroupMemberMapper.from(groupMember), EventType.DELETE);
    }

    @Override
    @Transactional
    public GroupMember getGroupMember(UUID groupMemberId) throws GroupMemberNotFound {
        return groupMemberRepository
                .findByIdOptional(groupMemberId)
                .map(GroupMemberMapper::from)
                .orElseThrow(GroupMemberNotFound::new);
    }
}
