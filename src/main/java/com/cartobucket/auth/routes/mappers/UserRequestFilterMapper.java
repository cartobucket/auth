package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.UserRequestFilter;

import java.util.List;
import java.util.UUID;

public class UserRequestFilterMapper {
    public static UserRequestFilter to(List<UUID> authorizationServerIds) {
        var filter = new UserRequestFilter();
        filter.setAuthorizationServerIds(authorizationServerIds);
        return filter;
    }
}
