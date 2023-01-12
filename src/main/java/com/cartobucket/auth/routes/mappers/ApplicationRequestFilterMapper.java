package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.ApplicationRequestFilter;

import java.util.List;
import java.util.UUID;

public class ApplicationRequestFilterMapper {
    public static ApplicationRequestFilter to(List<UUID> authorizationServerIds) {
        var filter = new ApplicationRequestFilter();
        filter.setAuthorizationServerIds(authorizationServerIds);
        return filter;
    }
}
