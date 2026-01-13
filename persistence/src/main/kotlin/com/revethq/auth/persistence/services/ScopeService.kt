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

import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.exceptions.notfound.ScopeNotFound
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.ScopeRepository
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.mappers.ScopeMapper
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class ScopeService(
    private val eventRepository: EventRepository,
    private val scopeRepository: ScopeRepository
) : com.revethq.auth.core.services.ScopeService {

    @Transactional
    override fun getScopes(authorizationServerIds: List<UUID>, page: Page): List<Scope> {
        val allEntities: List<com.revethq.auth.persistence.entities.Scope> = if (authorizationServerIds.isNotEmpty()) {
            scopeRepository.list("authorizationServer.id in ?1", Sort.descending("createdOn"), authorizationServerIds)
        } else {
            scopeRepository.listAll(Sort.descending("createdOn"))
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { ScopeMapper.from(it) }
    }

    @Transactional
    override fun createScope(scope: Scope): Scope {
        scope.createdOn = OffsetDateTime.now()
        scope.updatedOn = OffsetDateTime.now()
        val _scope = ScopeMapper.to(scope)
        scopeRepository.persist(_scope)
        eventRepository.createScopeEvent(ScopeMapper.from(_scope), EventType.CREATE)
        return ScopeMapper.from(_scope)
    }

    @Transactional
    @Throws(ScopeNotFound::class)
    override fun deleteScope(scopeId: UUID) {
        val scope = scopeRepository
            .findByIdOptional(scopeId)
            .orElseThrow { ScopeNotFound() }
        scopeRepository.delete(scope)
        eventRepository.createScopeEvent(ScopeMapper.from(scope), EventType.DELETE)
    }

    @Transactional
    @Throws(ScopeNotFound::class)
    override fun getScope(scopeId: UUID): Scope {
        return scopeRepository
            .findByIdOptional(scopeId)
            .map { ScopeMapper.from(it) }
            .orElseThrow { ScopeNotFound() }
    }

    @Transactional
    override fun filterScopesForAuthorizationServerId(authorizationServerId: UUID, scopes: String): List<Scope> {
        val authorizationServerScopes = scopeRepository
            .findAllByAuthorizationServerIdIn(listOf(authorizationServerId))

        // Parse the requested scope names
        val requestedScopeNames = scopes.split("\\s+".toRegex())

        // Filter the authorization server scopes by the requested names and return full Scope entities with IDs
        return authorizationServerScopes
            .filter { scope -> requestedScopeNames.contains(scope.name) }
            .map { ScopeMapper.from(it) }
    }

    @Transactional
    override fun getScopesForResourceId(id: UUID): List<Scope> {
        return emptyList()
    }
}
