package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UserRequestFilter;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.model.generated.UsersResponse;

import java.util.UUID;

public interface UserService {
    UsersResponse getUsers(UserRequestFilter filter);

    UserResponse createUser(UserRequest userRequest);

    void deleteUser(UUID userId);

    UserResponse getUser(UUID userId);

    UserResponse updateUser(UUID userId, UserRequest userRequest);
}
