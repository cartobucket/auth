package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.ScopeRequest;
import com.cartobucket.auth.model.generated.ScopeResponse;
import com.cartobucket.auth.models.Scope;

import java.util.UUID;

public class ScopeMapper {
    public static Scope to(ScopeRequest scopeRequest) {
        var scope = new Scope();
        scope.setName(scopeRequest.getName());
        scope.setAuthorizationServerId(scopeRequest.getAuthorizationServerId());
        return scope;
    }

    public static ScopeResponse toResponse(Scope scope) {
        var response = new ScopeResponse();
        response.setId(String.valueOf(scope.getId()));
        response.setAuthorizationServerId(scope.getAuthorizationServerId());
        response.setName(scope.getName());
        response.setCreatedOn(scope.getCreatedOn());
        response.setUpdatedOn(scope.getUpdatedOn());
        return response;
    }
}
