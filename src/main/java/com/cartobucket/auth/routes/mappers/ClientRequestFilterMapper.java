package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.ClientRequestFilter;

import java.util.List;
import java.util.UUID;

public class ClientRequestFilterMapper {
    public static ClientRequestFilter to(List<UUID> authorizationServerIds) {
        var filter = new ClientRequestFilter();
        filter.setAuthorizationServerIds(authorizationServerIds);
        return filter;
    }
}
