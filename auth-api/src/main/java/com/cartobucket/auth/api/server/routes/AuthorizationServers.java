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

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.api.interfaces.AuthorizationServersApi;
import com.cartobucket.auth.api.dto.AuthorizationServerRequest;
import com.cartobucket.auth.api.dto.AuthorizationServersResponse;
import com.cartobucket.auth.api.server.routes.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class AuthorizationServers implements AuthorizationServersApi {
    public final AuthorizationServerService authorizationServerService;

    public AuthorizationServers(AuthorizationServerService authorizationServerService) {
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response createAuthorizationServer(AuthorizationServerRequest authorizationServerRequest) {
        return Response
                .ok()
                .entity(
                        AuthorizationServerMapper.toResponse(
                                authorizationServerService.createAuthorizationServer(
                                        AuthorizationServerMapper.from(authorizationServerRequest)
                                )
                        )
                )
                .build();
    }

    @Override
    public Response deleteAuthorizationServer(UUID authorizationServerId) {
        authorizationServerService.deleteAuthorizationServer(authorizationServerId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getAuthorizationServer(UUID authorizationServerId) {
        return Response
                .ok()
                .entity(AuthorizationServerMapper.toResponse(
                        authorizationServerService.getAuthorizationServer(authorizationServerId)))
                .build();
    }

    @Override
    public Response listAuthorizationServers(Integer limit, Integer offset) {
        // TODO: Probably makes senes to move to Kotlin and use default parameters
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var authorizationServersResponse = new AuthorizationServersResponse();
        authorizationServersResponse.setAuthorizationServers(
                authorizationServerService.getAuthorizationServers(new Page(limit, offset))
                        .stream()
                        .map(AuthorizationServerMapper::toResponse)
                        .toList()
        );
        authorizationServersResponse.setPage(
                getPage("authorizationServers", Collections.emptyList(), limit, offset)
        );
        return Response
                .ok()
                .entity(authorizationServersResponse)
                .build();
    }

    @Override
    public Response updateAuthorizationServer(UUID authorizationServerId, AuthorizationServerRequest authorizationServerRequest) {
        return Response
                .ok()
                .entity(
                        AuthorizationServerMapper.toResponse(
                                authorizationServerService.updateAuthorizationServer(
                                        authorizationServerId,
                                        AuthorizationServerMapper.from(authorizationServerRequest)
                                )
                        )
                )
                .build();
    }
}
