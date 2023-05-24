package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.User;

import java.util.UUID;

public class UserMapper {
    public static User from(UserRequest userRequest) {
        var user = new User();
        user.setAuthorizationServerId(userRequest.getAuthorizationServerId());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        return user;
    }
    public static UserResponse toResponse(User user) {
        var userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setAuthorizationServerId(String.valueOf(user.getAuthorizationServerId()));
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreatedOn(user.getCreatedOn());
        userResponse.setUpdatedOn(user.getUpdatedOn());
        return userResponse;
    }

    public static UserResponse toResponseWithProfile(User user, Profile profile) {
        var userResponse = toResponse(user);
        userResponse.setProfile(profile.getProfile());
        return userResponse;
    }
}
