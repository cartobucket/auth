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

package com.revethq.auth.persistence.services

import com.revethq.auth.core.domain.Group
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.exceptions.notfound.GroupNotFound
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.mappers.GroupMapper
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.GroupMemberRepository
import com.revethq.auth.persistence.repositories.GroupRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class GroupService(
    private val eventRepository: EventRepository,
    private val groupRepository: GroupRepository,
    private val groupMemberRepository: GroupMemberRepository
) : com.revethq.auth.core.services.GroupService {

    @Transactional
    override fun getGroups(authorizationServerIds: List<UUID>, page: Page): List<Group> {
        val allEntities: List<com.revethq.auth.persistence.entities.Group> = if (authorizationServerIds.isNotEmpty()) {
            groupRepository.list("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
        } else {
            groupRepository.listAll(Sort.descending("createdOn"))
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { GroupMapper.from(it) }
    }

    @Transactional
    override fun createGroup(group: Group): Group {
        group.createdOn = OffsetDateTime.now()
        group.updatedOn = OffsetDateTime.now()
        val _group = GroupMapper.to(group)
        groupRepository.persist(_group)
        eventRepository.createGroupEvent(GroupMapper.from(_group), EventType.CREATE)
        return GroupMapper.from(_group)
    }

    @Transactional
    @Throws(GroupNotFound::class)
    override fun deleteGroup(groupId: UUID) {
        val group = groupRepository
            .findByIdOptional(groupId)
            .orElseThrow { GroupNotFound() }

        // Delete all group members first
        groupMemberRepository.deleteByGroupId(groupId)

        groupRepository.delete(group)
        eventRepository.createGroupEvent(GroupMapper.from(group), EventType.DELETE)
    }

    @Transactional
    @Throws(GroupNotFound::class)
    override fun getGroup(groupId: UUID): Group {
        return groupRepository
            .findByIdOptional(groupId)
            .map { GroupMapper.from(it) }
            .orElseThrow { GroupNotFound() }
    }

    @Transactional
    @Throws(GroupNotFound::class)
    override fun updateGroup(groupId: UUID, group: Group): Group {
        val _group = groupRepository
            .findByIdOptional(groupId)
            .orElseThrow { GroupNotFound() }

        _group.displayName = group.displayName
        _group.externalId = group.externalId
        _group.metadata = group.metadata
        _group.updatedOn = OffsetDateTime.now()

        groupRepository.persist(_group)
        eventRepository.createGroupEvent(GroupMapper.from(_group), EventType.UPDATE)
        return GroupMapper.from(_group)
    }
}
