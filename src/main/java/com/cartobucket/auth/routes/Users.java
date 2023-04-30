package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.UsersApi;
import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.routes.mappers.UserRequestFilterMapper;
import com.cartobucket.auth.services.UserService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class Users implements UsersApi {
    final UserService userService;

    public Users(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response createUser(UserRequest userRequest) {
        return Response.ok().entity(userService.createUser(userRequest)).build();
    }

    @Override
    public Response deleteUser(UUID userId) {
        userService.deleteUser(userId);
        return Response.ok().build();
    }

    @Override
    public Response getUser(UUID userId) {
        return Response.ok().entity(userService.getUser(userId)).build();
    }

    @Override
    public Response listUsers(List<UUID> authorizationServerIds) {
        return Response
                .ok()
                .entity(userService.getUsers(UserRequestFilterMapper.to(authorizationServerIds)))
                .build();
    }

    @Override
    public Response updateUser(UUID userId, UserRequest userRequest) {
        return Response.ok().entity(userService.updateUser(userId, userRequest)).build();
    }
}
