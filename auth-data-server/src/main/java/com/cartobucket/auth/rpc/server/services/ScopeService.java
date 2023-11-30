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

package com.cartobucket.auth.rpc.server.services;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.exceptions.notfound.ScopeNotFound;
import com.cartobucket.auth.rpc.server.entities.EventType;
import com.cartobucket.auth.rpc.server.entities.mappers.ScopeMapper;
import com.cartobucket.auth.rpc.server.repositories.EventRepository;
import com.cartobucket.auth.rpc.server.repositories.ScopeRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class ScopeService implements com.cartobucket.auth.data.services.ScopeService {
    final EventRepository eventRepository;
    final ScopeRepository scopeRepository;

    public ScopeService(
            EventRepository eventRepository,
            ScopeRepository scopeRepository
    ) {
        this.eventRepository = eventRepository;
        this.scopeRepository = scopeRepository;
    }

    @Override
    public List<Scope> getScopes(List<UUID> authorizationServerIds, Page page) {
        if (!authorizationServerIds.isEmpty()) {
            return scopeRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .stream()
                    .map(ScopeMapper::from)
                    .toList();
        } else {
            return scopeRepository.findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .stream()
                    .map(ScopeMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public Scope createScope(final Scope scope) {
        scope.setCreatedOn(OffsetDateTime.now());
        scope.setUpdatedOn(OffsetDateTime.now());
        var _scope = ScopeMapper.to(scope);
        scopeRepository.persist(_scope);
        eventRepository.createScopeEvent(ScopeMapper.from(_scope), EventType.CREATE);
        return ScopeMapper.from(_scope);
    }

    @Override
    @Transactional
    public void deleteScope(UUID scopeId) throws ScopeNotFound {
        final var scope = scopeRepository
                .findByIdOptional(scopeId)
                .orElseThrow(ScopeNotFound::new);
        scopeRepository.delete(scope);
        eventRepository.createScopeEvent(ScopeMapper.from(scope), EventType.DELETE);
    }

    @Override
    public Scope getScope(UUID scopeId) throws ScopeNotFound {
        return scopeRepository
                .findByIdOptional(scopeId)
                .map(ScopeMapper::from)
                .orElseThrow(ScopeNotFound::new);
    }

    @Override
    public List<String> filterScopesForAuthorizationServerId(UUID authorizationServerId, String scopes) {
        final var authorizationServerScopes = scopeRepository
                .findAllByAuthorizationServerIdIn(List.of(authorizationServerId));

        return com.cartobucket.auth.data.services.ScopeService.filterScopesByList(
                scopes,
                authorizationServerScopes
                        .stream()
                        .map(ScopeMapper::from)
                        .map(Scope::getName)
                        .toList()
        );
    }
}
