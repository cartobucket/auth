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

package com.cartobucket.auth.rpc.server.rpc;


import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.data.services.UserService;
import com.cartobucket.auth.rpc.UserCreateRequest;
import com.cartobucket.auth.rpc.UserCreateResponse;
import com.cartobucket.auth.rpc.UserDeleteRequest;
import com.cartobucket.auth.rpc.UserGetRequest;
import com.cartobucket.auth.rpc.UserListRequest;
import com.cartobucket.auth.rpc.UserListResponse;
import com.cartobucket.auth.rpc.UserResponse;
import com.cartobucket.auth.rpc.UserSetPasswordRequest;
import com.cartobucket.auth.rpc.UserSetPasswordResponse;
import com.cartobucket.auth.rpc.UserUpdateRequest;
import com.cartobucket.auth.rpc.Users;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.graalvm.collections.Pair;

import java.util.UUID;

@GrpcService
public class UserRpcService implements Users {
    final UserService userService;

    public UserRpcService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Blocking
    public Uni<UserCreateResponse> createUser(UserCreateRequest request) {
        var user = new User();
        user.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());

        var profile = new Profile();
        profile.setProfile(Profile.fromProtoMap(request.getProfile().getFieldsMap()));

        var userAndProfile = userService.createUser(Pair.create(user, profile));
        user = userAndProfile.getLeft();
        profile = userAndProfile.getRight();
        profile.setProfile(Profile.fromProtoMap(request.getProfile().getFieldsMap()));

        if (!request.getPassword().isEmpty()) {
            userService.setPassword(user, request.getPassword());
        }

        return Uni
                .createFrom()
                .item(
                        UserCreateResponse
                                .newBuilder()
                                .setId(String.valueOf(user.getId()))
                                .setEmail(user.getEmail())
                                .setUsername(user.getUsername())
                                .setAuthorizationServerId(String.valueOf(user.getAuthorizationServerId()))
                                .setProfile(Profile.toProtoMap(profile.getProfile()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(user.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(user.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<UserListResponse> listUsers(UserListRequest request) {
        final var users = userService.getUsers(
                request
                        .getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList()
        );

        return Uni
                .createFrom()
                .item(
                        UserListResponse.newBuilder().addAllUsers(
                                        users
                                                .stream()
                                                .map(user ->
                                                        UserResponse
                                                                .newBuilder()
                                                                .setId(String.valueOf(user.getId()))
                                                                .setEmail(user.getEmail())
                                                                .setUsername(user.getUsername())
                                                                .setAuthorizationServerId(String.valueOf(user.getAuthorizationServerId()))
                                                                .setCreatedOn(Timestamp.newBuilder().setSeconds(user.getCreatedOn().toEpochSecond()).build())
                                                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(user.getUpdatedOn().toEpochSecond()).build())
                                                                .build()
                                                )
                                                .toList()
                                )
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<UserResponse> deleteUser(UserDeleteRequest request) {
        userService.deleteUser(UUID.fromString(request.getId()));
        return Uni.createFrom().item(UserResponse.newBuilder().setId(request.getId()).build());
    }

    @Override
    @Blocking
    public Uni<UserResponse> updateUser(UserUpdateRequest request) {
        var user = new User();
        user.setId(UUID.fromString(request.getId()));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        var profile = new Profile();
        profile.setResource(user.getId());
        profile.setProfile(Profile.fromProtoMap(request.getProfile().getFieldsMap()));
        var userAndProfiler = userService.updateUser(user.getId(), Pair.create(user, profile));

        user = userAndProfiler.getLeft();
        profile = userAndProfiler.getRight();

        return Uni
                .createFrom()
                .item(
                        UserResponse
                                .newBuilder()
                                .setId(String.valueOf(user.getId()))
                                .setEmail(user.getEmail())
                                .setUsername(user.getUsername())
                                .setAuthorizationServerId(String.valueOf(user.getAuthorizationServerId()))
                                .setProfile(Profile.toProtoMap(profile.getProfile()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(user.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(user.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<UserResponse> getUser(UserGetRequest request) {
        final var userAndProfile = userService.getUser(UUID.fromString(request.getId()));
        final var user = userAndProfile.getLeft();
        final var profile = userAndProfile.getRight();

        return Uni
                .createFrom()
                .item(
                        UserResponse
                                .newBuilder()
                                .setId(String.valueOf(user.getId()))
                                .setEmail(user.getEmail())
                                .setUsername(user.getUsername())
                                .setAuthorizationServerId(String.valueOf(user.getAuthorizationServerId()))
                                .setProfile(Profile.toProtoMap(profile.getProfile()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(user.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(user.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<UserSetPasswordResponse> setUserPassword(UserSetPasswordRequest request) {
        final var userAndProfile = userService.getUser(UUID.fromString(request.getId()));
        userService.setPassword(userAndProfile.getLeft(), request.getPassword());
        return Uni
                .createFrom()
                .item(UserSetPasswordResponse.newBuilder().build());
    }
}
