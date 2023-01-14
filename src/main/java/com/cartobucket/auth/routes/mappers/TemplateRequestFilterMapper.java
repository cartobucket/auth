package com.cartobucket.auth.routes.mappers;

import com.cartobucket.auth.model.generated.TemplateRequestFilter;
import com.cartobucket.auth.model.generated.UserRequestFilter;

import java.util.List;
import java.util.UUID;

public class TemplateRequestFilterMapper {
    public static TemplateRequestFilter to(List<UUID> authorizationServerIds) {
        var filter = new TemplateRequestFilter();
        filter.setAuthorizationServerIds(authorizationServerIds);
        return filter;
    }
}
