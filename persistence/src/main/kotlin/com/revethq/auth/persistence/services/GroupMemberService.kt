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

import com.revethq.auth.core.domain.GroupMember
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.exceptions.notfound.GroupMemberNotFound
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.mappers.GroupMemberMapper
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.GroupMemberRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class GroupMemberService(
    private val eventRepository: EventRepository,
    private val groupMemberRepository: GroupMemberRepository
) : com.revethq.auth.core.services.GroupMemberService {

    @Transactional
    override fun getGroupMembers(authorizationServerIds: List<UUID>, groupIds: List<UUID>, page: Page): List<GroupMember> {
        val allEntities: List<com.revethq.auth.persistence.entities.GroupMember> = when {
            groupIds.isNotEmpty() -> {
                groupMemberRepository.list("groupId in ?1", Sort.descending("createdOn"), groupIds)
            }
            authorizationServerIds.isNotEmpty() -> {
                groupMemberRepository.list("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
            }
            else -> {
                groupMemberRepository.listAll(Sort.descending("createdOn"))
            }
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { GroupMemberMapper.from(it) }
    }

    @Transactional
    override fun createGroupMember(groupMember: GroupMember): GroupMember {
        groupMember.createdOn = OffsetDateTime.now()
        val _groupMember = GroupMemberMapper.to(groupMember)
        groupMemberRepository.persist(_groupMember)
        eventRepository.createGroupMemberEvent(GroupMemberMapper.from(_groupMember), EventType.CREATE)
        return GroupMemberMapper.from(_groupMember)
    }

    @Transactional
    @Throws(GroupMemberNotFound::class)
    override fun deleteGroupMember(groupMemberId: UUID) {
        val groupMember = groupMemberRepository
            .findByIdOptional(groupMemberId)
            .orElseThrow { GroupMemberNotFound() }

        groupMemberRepository.delete(groupMember)
        eventRepository.createGroupMemberEvent(GroupMemberMapper.from(groupMember), EventType.DELETE)
    }

    @Transactional
    @Throws(GroupMemberNotFound::class)
    override fun getGroupMember(groupMemberId: UUID): GroupMember {
        return groupMemberRepository
            .findByIdOptional(groupMemberId)
            .map { GroupMemberMapper.from(it) }
            .orElseThrow { GroupMemberNotFound() }
    }
}
