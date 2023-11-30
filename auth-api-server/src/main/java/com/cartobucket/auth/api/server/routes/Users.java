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
import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.generated.UsersApi;
import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UsersResponse;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.api.server.routes.mappers.UserMapper;
import com.cartobucket.auth.data.services.UserService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Users implements UsersApi {
    final UserService userService;

    public Users(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response createUser(UserRequest userRequest) {
        return Response
                .ok()
                .entity(
                        UserMapper.toResponse(
                                userService.createUser(UserMapper.from(userRequest))
                        )
                )
                .build();
    }

    @Override
    public Response deleteUser(UUID userId) {
        userService.deleteUser(userId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getUser(UUID userId) {
        return Response
                .ok()
                .entity(
                        UserMapper.toResponse(userService.getUser(userId))
                )
                .build();
    }

    @Override
    public Response listUsers(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        final var usersResponse = new UsersResponse();
        usersResponse.setUsers(
                userService
                        .getUsers(authorizationServerIds, new Page(limit, offset))
                        .stream()
                        .map((User user) -> UserMapper.toResponse(Pair.create(user, new Profile())))
                        .toList()
        );
        usersResponse.setPage(getPage("users", authorizationServerIds, limit, offset));
        return Response
                .ok()
                .entity(usersResponse)
                .build();
    }

    @Override
    public Response updateUser(UUID userId, UserRequest userRequest) {
        return Response
                .ok()
                .entity(
                        UserMapper.toResponse(
                                userService.updateUser(userId, UserMapper.from(userRequest))
                        )
                )
                .build();
    }
}
