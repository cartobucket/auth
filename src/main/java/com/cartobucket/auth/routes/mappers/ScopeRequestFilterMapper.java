package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.ScopeRequestFilter;
import com.cartobucket.auth.model.generated.TemplateRequestFilter;

import java.util.List;
import java.util.UUID;

public class ScopeRequestFilterMapper {
    public static ScopeRequestFilter to(List<UUID> authorizationServerIds) {
        var filter = new ScopeRequestFilter();
        filter.setAuthorizationServerIds(authorizationServerIds);
        return filter;
    }
}
