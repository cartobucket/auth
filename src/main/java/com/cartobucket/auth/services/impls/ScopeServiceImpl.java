package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.ScopeRequest;
import com.cartobucket.auth.model.generated.ScopeRequestFilter;
import com.cartobucket.auth.model.generated.ScopeResponse;
import com.cartobucket.auth.model.generated.ScopesResponse;
import com.cartobucket.auth.models.Scope;
import com.cartobucket.auth.models.mappers.ScopeMapper;
import com.cartobucket.auth.repositories.ScopeRepository;
import com.cartobucket.auth.services.ScopeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class ScopeServiceImpl implements ScopeService {
    final ScopeRepository scopeRepository;

    public ScopeServiceImpl(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public ScopesResponse getScopes(ScopeRequestFilter filter) {
        var scopes = StreamSupport.stream(scopeRepository.findAll().spliterator(), false);
        // TODO: This should happen in the DB.
        if (!filter.getAuthorizationServerIds().isEmpty()) {
            scopes = scopes.filter(scope -> filter.getAuthorizationServerIds().contains(scope.getAuthorizationServerId()));
        }
        var response = new ScopesResponse();
        response.setScopes(scopes.map(ScopeMapper::toResponse).toList());
        return response;
    }

    @Override
    @Transactional
    public ScopeResponse createScope(ScopeRequest scopeRequest) {
        var scope = ScopeMapper.to(scopeRequest);
        scope.setCreatedOn(OffsetDateTime.now());
        scope.setUpdatedOn(OffsetDateTime.now());
        scope = scopeRepository.save(scope);
        return ScopeMapper.toResponse(scope);
    }

    @Override
    @Transactional
    public void deleteScope(UUID scopeId) {
        var scope = scopeRepository
                .findById(scopeId)
                .orElseThrow(NotFoundException::new);
        scopeRepository.delete(scope);
    }

    @Override
    public ScopeResponse getScope(UUID scopeId) {
        var scope = scopeRepository
                .findById(scopeId)
                .orElseThrow(NotFoundException::new);
        return ScopeMapper.toResponse(scope);
    }

    @Override
    public List<String> filterScopesForAuthorizationServerId(UUID authorizationServerId, String scopes) {
        final var authorizationServerScopes = scopeRepository
                .findAllByAuthorizationServerId(authorizationServerId);

        return ScopeService.filterScopesByList(scopes, authorizationServerScopes.stream().map(Scope::getName).toList());
    }
}
