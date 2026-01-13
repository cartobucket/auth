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

import com.cartobucket.auth.data.domain.Group;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.exceptions.notfound.GroupNotFound;
import com.cartobucket.auth.postgres.client.entities.EventType;
import com.cartobucket.auth.postgres.client.entities.mappers.GroupMapper;
import com.cartobucket.auth.postgres.client.repositories.EventRepository;
import com.cartobucket.auth.postgres.client.repositories.GroupMemberRepository;
import com.cartobucket.auth.postgres.client.repositories.GroupRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GroupService implements com.cartobucket.auth.data.services.GroupService {

    final EventRepository eventRepository;
    final GroupRepository groupRepository;
    final GroupMemberRepository groupMemberRepository;

    public GroupService(
            EventRepository eventRepository,
            GroupRepository groupRepository,
            GroupMemberRepository groupMemberRepository
    ) {
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Override
    @Transactional
    public List<Group> getGroups(List<UUID> authorizationServerIds, Page page) {
        if (!authorizationServerIds.isEmpty()) {
            return groupRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(GroupMapper::from)
                    .toList();
        } else {
            return groupRepository.findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(GroupMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public Group createGroup(Group group) {
        group.setCreatedOn(OffsetDateTime.now());
        group.setUpdatedOn(OffsetDateTime.now());
        var _group = GroupMapper.to(group);
        groupRepository.persist(_group);
        eventRepository.createGroupEvent(GroupMapper.from(_group), EventType.CREATE);
        return GroupMapper.from(_group);
    }

    @Override
    @Transactional
    public void deleteGroup(UUID groupId) throws GroupNotFound {
        var group = groupRepository
                .findByIdOptional(groupId)
                .orElseThrow(GroupNotFound::new);

        // Delete all group members first
        groupMemberRepository.deleteByGroupId(groupId);

        groupRepository.delete(group);
        eventRepository.createGroupEvent(GroupMapper.from(group), EventType.DELETE);
    }

    @Override
    @Transactional
    public Group getGroup(UUID groupId) throws GroupNotFound {
        return groupRepository
                .findByIdOptional(groupId)
                .map(GroupMapper::from)
                .orElseThrow(GroupNotFound::new);
    }

    @Override
    @Transactional
    public Group updateGroup(UUID groupId, Group group) throws GroupNotFound {
        var _group = groupRepository
                .findByIdOptional(groupId)
                .orElseThrow(GroupNotFound::new);

        _group.setDisplayName(group.getDisplayName());
        _group.setExternalId(group.getExternalId());
        _group.setMetadata(group.getMetadata());
        _group.setUpdatedOn(OffsetDateTime.now());

        groupRepository.persist(_group);
        eventRepository.createGroupEvent(GroupMapper.from(_group), EventType.UPDATE);
        return GroupMapper.from(_group);
    }
}
