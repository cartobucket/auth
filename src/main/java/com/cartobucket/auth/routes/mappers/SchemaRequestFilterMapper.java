package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.SchemaRequestFilter;

import java.util.List;
import java.util.UUID;

public class SchemaRequestFilterMapper {
    public static SchemaRequestFilter to(List<UUID> authorizationServerIds) {
        var filter = new SchemaRequestFilter();
        filter.setAuthorizationServerIds(authorizationServerIds);
        return filter;
    }
}
