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

package com.cartobucket.auth.postgres.client.services

import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.exceptions.notfound.ScopeNotFound
import com.cartobucket.auth.postgres.client.entities.EventType
import com.cartobucket.auth.postgres.client.entities.mappers.ScopeMapper
import com.cartobucket.auth.postgres.client.repositories.EventRepository
import com.cartobucket.auth.postgres.client.repositories.ScopeRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.OffsetDateTime
import java.util.UUID

private typealias ScopeEntity = com.cartobucket.auth.postgres.client.entities.Scope
private typealias ScopeQuery = io.quarkus.hibernate.orm.panache.PanacheQuery<ScopeEntity>

@ApplicationScoped
class ScopeService(
    private val eventRepository: EventRepository,
    private val scopeRepository: ScopeRepository,
) : com.cartobucket.auth.data.services.ScopeService {
    @Override
    @Transactional
    override fun getScopes(
        authorizationServerIds: List<UUID>,
        page: Page,
    ): List<Scope> =
        if (authorizationServerIds.isNotEmpty()) {
            val scopeQuery: ScopeQuery =
                scopeRepository
                    .find(
                        "authorizationServer.id in ?1",
                        Sort.descending("createdOn"),
                        authorizationServerIds,
                    )
            val rangedQuery: ScopeQuery =
                scopeQuery
                    .range(
                        page.offset(),
                        page.limit,
                    )
            val entityList: List<ScopeEntity> = rangedQuery.list()
            entityList.map { scope -> ScopeMapper.from(scope) }
        } else {
            val scopeQuery: ScopeQuery =
                scopeRepository
                    .findAll(
                        Sort.descending("createdOn"),
                    )
            val rangedQuery: ScopeQuery =
                scopeQuery
                    .range(
                        page.offset(),
                        page.limit,
                    )
            val entityList: List<ScopeEntity> = rangedQuery.list()
            entityList.map { scope -> ScopeMapper.from(scope) }
        }

    @Override
    @Transactional
    override fun createScope(scope: Scope): Scope {
        scope.createdOn = OffsetDateTime.now()
        scope.updatedOn = OffsetDateTime.now()
        val entityScope = ScopeMapper.to(scope)
        scopeRepository.persist(entityScope)
        eventRepository.createScopeEvent(ScopeMapper.from(entityScope), EventType.CREATE)
        return ScopeMapper.from(entityScope)
    }

    @Override
    @Transactional
    @Throws(ScopeNotFound::class)
    override fun deleteScope(scopeId: UUID) {
        val scope =
            scopeRepository
                .findByIdOptional(scopeId)
                .orElseThrow { ScopeNotFound() }
        scopeRepository.delete(scope)
        eventRepository.createScopeEvent(ScopeMapper.from(scope), EventType.DELETE)
    }

    @Override
    @Transactional
    @Throws(ScopeNotFound::class)
    override fun getScope(scopeId: UUID): Scope =
        scopeRepository
            .findByIdOptional(scopeId)
            .map { ScopeMapper.from(it) }
            .orElseThrow { ScopeNotFound() }

    @Override
    @Transactional
    override fun filterScopesForAuthorizationServerId(
        authorizationServerId: UUID,
        scopes: String,
    ): List<Scope> {
        val authorizationServerScopes =
            scopeRepository
                .findAllByAuthorizationServerIdIn(listOf(authorizationServerId))

        // Parse the requested scope names
        val requestedScopeNames = scopes.split("\\s+".toRegex())

        // Filter the authorization server scopes by the requested names and return full Scope entities with IDs
        return authorizationServerScopes
            .filter { scope -> requestedScopeNames.contains(scope.name) }
            .map { ScopeMapper.from(it) }
    }

    @Override
    @Transactional
    override fun getScopesForResourceId(id: UUID): List<Scope> = emptyList()

    @Override
    @Transactional
    override fun getScopes(
        authorizationServerIds: List<UUID>,
        scopeIds: List<UUID>,
    ): List<Scope> =
        if (authorizationServerIds.isNotEmpty() && scopeIds.isNotEmpty()) {
            val entityList =
                scopeRepository
                    .find("authorizationServer.id in ?1 and id in ?2", Sort.descending("createdOn"), authorizationServerIds, scopeIds)
                    .list<com.cartobucket.auth.postgres.client.entities.Scope>()
            entityList.map { scope -> ScopeMapper.from(scope) }
        } else {
            emptyList()
        }
}
