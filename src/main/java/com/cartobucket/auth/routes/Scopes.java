package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ScopesApi;
import com.cartobucket.auth.model.generated.ScopeRequest;
import com.cartobucket.auth.routes.mappers.ScopeRequestFilterMapper;
import com.cartobucket.auth.services.ScopeService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class Scopes implements ScopesApi {
    final ScopeService scopeService;

    public Scopes(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Override
    public Response scopesGet(List<UUID> authorizationServerId) {
        var requestFilter = ScopeRequestFilterMapper.to(authorizationServerId);
        return Response.ok().entity(scopeService.getScopes(requestFilter)).build();
    }

    @Override
    public Response scopesPost(ScopeRequest scopeRequest) {
        return Response.ok().entity(scopeService.createScope(scopeRequest)).build();
    }

    @Override
    public Response scopesScopeIdDelete(UUID scopeId) {
        scopeService.deleteScope(scopeId);
        return Response.ok().build();
    }

    @Override
    public Response scopesScopeIdGet(UUID scopeId) {
        return Response.ok().entity(scopeService.getScope(scopeId)).build();
    }
}
