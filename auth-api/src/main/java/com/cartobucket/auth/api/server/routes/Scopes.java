/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.api.server.routes;

import com.cartobucket.auth.api.server.routes.mappers.ScopeMapper;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.generated.ScopesApi;
import com.cartobucket.auth.model.generated.ScopeRequest;
import com.cartobucket.auth.model.generated.ScopesResponse;
import com.cartobucket.auth.data.services.ScopeService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Scopes implements ScopesApi {
    final ScopeService scopeService;

    public Scopes(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Override
    public Response createScope(ScopeRequest scopeRequest) {
        return Response
                .ok()
                .entity(
                        ScopeMapper.toResponse(
                                scopeService.createScope(ScopeMapper.to(scopeRequest))
                        )
                )
                .build();
    }

    @Override
    public Response deleteScope(UUID scopeId) {
        scopeService.deleteScope(scopeId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getScope(UUID scopeId) {
        return Response
                .ok()
                .entity(
                        ScopeMapper.toResponse(scopeService.getScope(scopeId))
                )
                .build();
    }

    @Override
    public Response listScopes(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        // TODO: Probably makes senes to move to Kotlin and use default parameters
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var scopesResponse = new ScopesResponse();
        scopesResponse.setScopes(
                scopeService.getScopes(authorizationServerIds, new Page(limit, offset))
                        .stream()
                        .map(ScopeMapper::toResponse)
                        .toList()
        );
        scopesResponse.setPage(getPage("scopes", authorizationServerIds, limit, offset));
        return Response
                .ok()
                .entity(scopesResponse)
                .build();
    }
}
