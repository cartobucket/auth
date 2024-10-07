package com.cartobucket.auth.data.services.grpc.mappers;

import com.cartobucket.auth.data.domain.*;
import com.cartobucket.auth.rpc.UserCreateResponse;
import com.cartobucket.auth.rpc.UserResponse;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class UserMapper {
    public static User to(UserResponse userResponse) {
        return new User.Builder()
                .setId(UUID.fromString(userResponse.getId()))
                .setAuthorizationServerId(UUID.fromString(userResponse.getAuthorizationServerId()))
                .setEmail(userResponse.getEmail())
                .setUsername(userResponse.getUsername())
                .setMetadata(MetadataMapper.from(userResponse.getMetadata()))
                .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                .build();
    }

    public static Pair<User, Profile> toUserAndProfile(UserCreateResponse userCreateResponse) {
        var userId = UUID.fromString(userCreateResponse.getId());
        var authorizationServerId = UUID.fromString(userCreateResponse.getAuthorizationServerId());
        return Pair.create(
                new User.Builder()
                        .setId(userId)
                        .setAuthorizationServerId(authorizationServerId)
                        .setEmail(userCreateResponse.getEmail())
                        .setUsername(userCreateResponse.getUsername())
                        .setMetadata(MetadataMapper.from(userCreateResponse.getMetadata()))
                        .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                        .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                        .build(),
                new Profile.Builder()
                        .setProfileType(ProfileType.User)
                        .setProfile(ProfileMapper.fromProtoMap(userCreateResponse.getProfile().getFieldsMap()))
                        .setResource(userId)
                        .setAuthorizationServerId(authorizationServerId)
                        .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                        .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                        .build()
        );
    }

    public static Pair<User, Profile> toUserAndProfile(UserResponse userResponse) {
        var userId = UUID.fromString(userResponse.getId());
        var authorizationServerId = UUID.fromString(userResponse.getAuthorizationServerId());
        return Pair.create(
                new User.Builder()
                        .setId(userId)
                        .setAuthorizationServerId(authorizationServerId)
                        .setEmail(userResponse.getEmail())
                        .setUsername(userResponse.getUsername())
                        .setMetadata(MetadataMapper.from(userResponse.getMetadata()))
                        .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                        .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                        .build(),
                new Profile.Builder()
                        .setProfileType(ProfileType.User)
                        .setProfile(ProfileMapper.fromProtoMap(userResponse.getProfile().getFieldsMap()))
                        .setResource(userId)
                        .setAuthorizationServerId(authorizationServerId)
                        .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                        .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                        .build()
        );
    }
}
