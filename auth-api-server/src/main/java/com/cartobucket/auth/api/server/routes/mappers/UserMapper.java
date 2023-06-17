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

import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.domain.User;
import org.graalvm.collections.Pair;

import java.util.Collections;
import java.util.Map;

public class UserMapper {
    public static Pair<User, Profile> from(UserRequest userRequest) {
        var user = new User();
        user.setAuthorizationServerId(userRequest.getAuthorizationServerId());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        var profile = new Profile();
        profile.setProfileType(ProfileType.User);
        profile.setProfile((Map<String, Object>) userRequest.getProfile());
        return Pair.create(user, profile);
    }
    public static UserResponse toResponse(Pair<User, Profile> userProfilePair) {
        var user = userProfilePair.getLeft();
        var profile = userProfilePair.getRight();
        var userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setAuthorizationServerId(String.valueOf(user.getAuthorizationServerId()));
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
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
