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

package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.User;
import com.revethq.iam.user.domain.ProfileType;
import com.cartobucket.auth.api.dto.UserRequest;
import com.cartobucket.auth.api.dto.UserResponse;

import java.util.Collections;
import java.util.Map;

public class UserMapper {
    public static Pair<User, Profile> from(UserRequest userRequest) {
        var user = new User();
        user.setAuthorizationServerId(userRequest.getAuthorizationServerId());
        user.setMetadata(MetadataMapper.from(userRequest.getMetadata()));
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        var profile = new Profile();
        profile.setProfileType(ProfileType.User);
        profile.setProfile((Map<String, Object>) userRequest.getProfile());
        profile.setAuthorizationServerId(userRequest.getAuthorizationServerId());
        return Pair.Companion.create(user, profile);
    }
    public static UserResponse toResponse(Pair<User, Profile> userProfilePair) {
        var user = userProfilePair.getLeft();
        var profile = userProfilePair.getRight();
        var userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setAuthorizationServerId(user.getAuthorizationServerId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setMetadata(MetadataMapper.to(user.getMetadata()));
        userResponse.setCreatedOn(user.getCreatedOn());
        userResponse.setUpdatedOn(user.getUpdatedOn());
        if (profile.getProfile() != null) {
            userResponse.setProfile(profile.getProfile());
        } else {
            userResponse.setProfile(Collections.emptyMap());
        }
        return userResponse;
    }
}
