package com.cartobucket.auth.api.server.routes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Pagination {
    public static com.cartobucket.auth.model.generated.Page getPage(String root, List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        var page = new com.cartobucket.auth.model.generated.Page();
        var authorizationServersParams = authorizationServerIds
                .stream()
                .map((UUID authorizationServerId) -> "&authorizationServerId=" + authorizationServerId)
                .collect(Collectors.joining());
        var next = "/" + root + "?limit" + limit + "&offset=" + (offset + limit);
        if (!authorizationServersParams.isEmpty()) {
            next += authorizationServersParams;
        }
        page.setNext(next);
        if (offset > 0) {
            var previous = "/" + root + "?limit" + limit + "&offset=" + (offset - limit);
            if (!authorizationServersParams.isEmpty()) {
                previous += authorizationServersParams;
            }
            page.setPrevious(previous);
        }
        return page;
    }
}
