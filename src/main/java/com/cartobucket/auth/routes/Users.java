package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.UsersApi;
import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.model.generated.UsersResponse;
import com.cartobucket.auth.services.UserService;

import java.util.UUID;

public class Users implements UsersApi {
    final UserService userService;

    public Users(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UsersResponse usersGet() {
        return userService.getUsers();

    }

    @Override
    public UserResponse usersPost(UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @Override
    public void usersUserIdDelete(UUID userId) {
        userService.deleteUser(userId);
    }

    @Override
    public UserResponse usersUserIdGet(UUID userId) {
        return userService.getUser(userId);
    }

    @Override
    public UserResponse usersUserIdPut(UUID userId, UserRequest userRequest) {
        return userService.updateUser(userId, userRequest);
    }
}
