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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound;
import com.cartobucket.auth.data.services.impls.mappers.UserMapper;
import com.cartobucket.auth.rpc.MutinyUsersGrpc;
import com.cartobucket.auth.rpc.UserCreateRequest;
import com.cartobucket.auth.rpc.UserDeleteRequest;
import com.cartobucket.auth.rpc.UserSetPasswordRequest;
import com.cartobucket.auth.rpc.UserUpdateRequest;
import com.cartobucket.auth.rpc.UserValidatePasswordRequest;
import io.quarkus.arc.DefaultBean;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.graalvm.collections.Pair;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class UserService implements com.cartobucket.auth.data.services.UserService {
    @Inject
    @GrpcClient("users")
    MutinyUsersGrpc.MutinyUsersStub usersClient;

    @Override
    public List<User> getUsers(List<UUID> authorizationServerIds) {
        return usersClient.listUsers(
                        com.cartobucket.auth.rpc.UserListRequest
                                .newBuilder()
                                .addAllAuthorizationServerIds(
                                        authorizationServerIds
                                                .stream()
                                                .map(String::valueOf)
                                                .toList()
                                )
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getUsersList()
                .stream()
                .map(UserMapper::to)
                .toList();
    }

    @Override
    public Pair<User, Profile> createUser(Pair<User, Profile> userProfilePair) {
        return UserMapper.toUserAndProfile(
                usersClient.createUser(
                        UserCreateRequest
                                .newBuilder()
                                .setUsername(userProfilePair.getLeft().getUsername())
                                .setPassword(userProfilePair.getLeft().getPassword())
                                .setProfile(Profile.toProtoMap(userProfilePair.getRight().getProfile()))
                                .setAuthorizationServerId(String.valueOf(userProfilePair.getLeft().getAuthorizationServerId()))
                                .setEmail(userProfilePair.getLeft().getEmail())
                                .build()
                )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public void deleteUser(UUID userId) {
        usersClient
                .deleteUser(
                        UserDeleteRequest
                                .newBuilder()
                                .setId(String.valueOf(userId))
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public Pair<User, Profile> getUser(UUID userId) throws UserNotFound, ProfileNotFound {
        return UserMapper.toUserAndProfile(
                usersClient
                        .getUser(
                                com.cartobucket.auth.rpc.UserGetRequest
                                        .newBuilder()
                                        .setId(String.valueOf(userId))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public Pair<User, Profile> getUser(String username) throws UserNotFound, ProfileNotFound {
        return null;
    }

    @Override
    public Pair<User, Profile> updateUser(UUID userId, Pair<User, Profile> userProfilePair) throws UserNotFound, ProfileNotFound {
        if (userProfilePair.getLeft().getPassword() != null && !userProfilePair.getLeft().getPassword().isEmpty()) {
            usersClient.setUserPassword(
                    UserSetPasswordRequest
                            .newBuilder()
                            .setId(String.valueOf(userId))
                            .setPassword(userProfilePair.getLeft().getPassword())
                            .build()
                    )
                    .await()
                    .atMost(Duration.of(3, ChronoUnit.SECONDS));
        }

        return UserMapper.toUserAndProfile(
                usersClient
                        .updateUser(
                                UserUpdateRequest
                                        .newBuilder()
                                        .setId(String.valueOf(userId))
                                        .setUsername(userProfilePair.getLeft().getUsername())
                                        .setProfile(Profile.toProtoMap(userProfilePair.getRight().getProfile()))
                                        .setEmail(userProfilePair.getLeft().getEmail())
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public void setPassword(User user, String password) {
        usersClient.setUserPassword(
                        com.cartobucket.auth.rpc.UserSetPasswordRequest
                                .newBuilder()
                                .setId(String.valueOf(user.getId()))
                                .setPassword(password)
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public boolean validatePassword(UUID userId, String password) {
        return usersClient
                .validateUserPassword(
                        UserValidatePasswordRequest
                                .newBuilder()
                                .setId(String.valueOf(userId))
                                .setPassword(password)
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getIsValid();
    }
}
