package com.cartobucket.auth.services;



import com.cartobucket.auth.model.generated.ScopeRequest;
import com.cartobucket.auth.model.generated.ScopeRequestFilter;
import com.cartobucket.auth.model.generated.ScopeResponse;
import com.cartobucket.auth.model.generated.ScopesResponse;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.stream;

public interface ScopeService {
    ScopesResponse getScopes(ScopeRequestFilter filter);

    ScopeResponse createScope(ScopeRequest scopeRequest);

    void deleteScope(UUID scopeId);

    ScopeResponse getScope(UUID scopeId);

    // Takes in a list of scopes and filters those scopes based on what scopes are associated with the AS.
    List<String> filterScopesForAuthorizationServerId(UUID authorizationServerId, String scopes);

    static List<String> scopeStringToScopeList(String scopes) {
        return stream(scopes.split("\\p{Zs}+"))
                .toList();
    }

    static String scopeListToScopeString(List<String> scopes) {
        return String.join(" ", scopes);
    }
}
