package com.cartobucket.auth.data.services.impls.mappers;

import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.rpc.ScopeResponse;
import com.cartobucket.auth.rpc.*;
import io.smallrye.mutiny.Uni;
import org.graalvm.collections.Pair;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ApplicationMapper {
    public static Application toApplication(ApplicationResponse applicationResponse) {
        var application = new Application();
        application.setId(UUID.fromString(applicationResponse.getId()));
        application.setName(applicationResponse.getName());
        application.setClientId(applicationResponse.getClientId());
        application.setAuthorizationServerId(UUID.fromString(applicationResponse.getAuthorizationServerId()));
        application.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        application.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return application;
    }

    public static Pair<Application, Profile> toApplicationWithProfile(ApplicationCreateResponse applicationCreateResponse, ProfileType profileType) {
        var application = new Application();
        application.setId(UUID.fromString(applicationCreateResponse.getId()));
        application.setName(applicationCreateResponse.getName());
        application.setClientId(applicationCreateResponse.getClientId());
        application.setAuthorizationServerId(UUID.fromString(applicationCreateResponse.getAuthorizationServerId()));
        application.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        application.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        var profile = new Profile();
        profile.setProfileType(profileType);
        profile.setProfile(Profile.fromProtoMap(applicationCreateResponse.getProfile().getFieldsMap()));
        profile.setResource(application.getId());
        profile.setAuthorizationServerId(application.getAuthorizationServerId());
        profile.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        profile.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        return Pair.create(application, profile);
    }

    public static ApplicationSecret toApplicationSecret(ApplicationSecretCreateResponse applicationSecret) {
        var _applicationSecret = new ApplicationSecret();
        _applicationSecret.setId(UUID.fromString(applicationSecret.getId()));
        _applicationSecret.setApplicationId(UUID.fromString(applicationSecret.getApplicationId()));
        _applicationSecret.setName(applicationSecret.getName());
        _applicationSecret.setScopes(Collections.singletonList(applicationSecret.getScopes()));
        _applicationSecret.setApplicationSecret(applicationSecret.getApplicationSecret());
        return _applicationSecret;
    }

    public static ApplicationSecret toApplicationSecret(ApplicationSecretResponse applicationSecretResponse) {
        var _applicationSecret = new ApplicationSecret();
        _applicationSecret.setId(UUID.fromString(applicationSecretResponse.getId()));
        _applicationSecret.setApplicationId(UUID.fromString(applicationSecretResponse.getApplicationId()));
        _applicationSecret.setName(applicationSecretResponse.getName());
        _applicationSecret.setScopes(Collections.singletonList(applicationSecretResponse.getScopes()));
        return _applicationSecret;
    }
}
