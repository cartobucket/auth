package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.model.generated.UsersResponse;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.User;

import java.util.UUID;

public interface UserService {
    UsersResponse getUsers();

    UserResponse createUser(UserRequest userRequest);

    void deleteUser(UUID userId);

    UserResponse getUser(UUID userId);

    UserResponse updateUser(UUID userId, UserRequest userRequest);
}
