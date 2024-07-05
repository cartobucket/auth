package com.cartobucket.auth.data.services.grpc.client.mappers;

import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.data.services.grpc.mappers.MetadataMapper;
import com.cartobucket.auth.data.services.grpc.mappers.ProfileMapper;
import com.cartobucket.auth.rpc.UserCreateResponse;
import com.cartobucket.auth.rpc.UserResponse;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class UserMapper {
    public static User to(UserResponse userResponse) {
        var user = new User();
        user.setId(java.util.UUID.fromString(userResponse.getId()));
        user.setAuthorizationServerId(java.util.UUID.fromString(userResponse.getAuthorizationServerId()));
        user.setEmail(userResponse.getEmail());
        user.setUsername(userResponse.getUsername());
        user.setMetadata(MetadataMapper.from(userResponse.getMetadata()));
        user.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        user.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        return user;
    }

    public static Pair<User, Profile> toUserAndProfile(UserCreateResponse userCreateResponse) {
        var user = new User();
        user.setId(java.util.UUID.fromString(userCreateResponse.getId()));
        user.setAuthorizationServerId(java.util.UUID.fromString(userCreateResponse.getAuthorizationServerId()));
        user.setEmail(userCreateResponse.getEmail());
        user.setUsername(userCreateResponse.getUsername());
        user.setMetadata(MetadataMapper.from(userCreateResponse.getMetadata()));
        user.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        user.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        var profile = new Profile();
        profile.setProfileType(ProfileType.User);
        profile.setProfile(ProfileMapper.fromProtoMap(userCreateResponse.getProfile().getFieldsMap()));
        profile.setResource(user.getId());
        profile.setAuthorizationServerId(user.getAuthorizationServerId());
        profile.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        profile.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        return Pair.create(user, profile);
    }

    public static Pair<User, Profile> toUserAndProfile(UserResponse userResponse) {
        var user = new User();
        user.setId(java.util.UUID.fromString(userResponse.getId()));
        user.setAuthorizationServerId(java.util.UUID.fromString(userResponse.getAuthorizationServerId()));
        user.setEmail(userResponse.getEmail());
        user.setUsername(userResponse.getUsername());
        user.setMetadata(MetadataMapper.from(userResponse.getMetadata()));
        user.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        user.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        var profile = new Profile();
        profile.setProfileType(ProfileType.User);
        profile.setProfile(ProfileMapper.fromProtoMap(userResponse.getProfile().getFieldsMap()));
        profile.setResource(user.getId());
        profile.setAuthorizationServerId(user.getAuthorizationServerId());
        profile.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        profile.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(userResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        return Pair.create(user, profile);
    }
}
