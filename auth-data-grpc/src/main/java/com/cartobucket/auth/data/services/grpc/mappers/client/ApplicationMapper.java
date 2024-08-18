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

package com.cartobucket.auth.data.services.grpc.mappers.client;

import com.cartobucket.auth.data.domain.*;
import com.cartobucket.auth.data.services.grpc.mappers.MetadataMapper;
import com.cartobucket.auth.data.services.grpc.mappers.ProfileMapper;
import com.cartobucket.auth.data.services.grpc.mappers.ScopeMapper;
import com.cartobucket.auth.rpc.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ApplicationMapper {
    public static Application toApplication(ApplicationResponse applicationResponse) {
        var application = new Application();
        application.setId(UUID.fromString(applicationResponse.getId()));
        application.setName(applicationResponse.getName());
        application.setClientId(applicationResponse.getClientId());
        application.setAuthorizationServerId(UUID.fromString(applicationResponse.getAuthorizationServerId()));
        application.setScopes(applicationResponse.getScopesList().stream().map(ScopeMapper::toScope).toList());
        application.setMetadata(MetadataMapper.from(applicationResponse.getMetadata()));
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
        application.setMetadata(MetadataMapper.from(applicationCreateResponse.getMetadata()));
        application.setScopes(applicationCreateResponse.getScopesList().stream().map(ScopeMapper::toScope).toList());
        application.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        application.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        var profile = new Profile();
        profile.setProfileType(profileType);
        profile.setProfile(ProfileMapper.fromProtoMap(applicationCreateResponse.getProfile().getFieldsMap()));
        profile.setResource(application.getId());
        profile.setAuthorizationServerId(application.getAuthorizationServerId());
        profile.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        profile.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));

        return Pair.create(application, profile);
    }

    public static ApplicationSecret toApplicationSecret(ApplicationSecretCreateResponse applicationSecret) {
        return new ApplicationSecret.Builder()
                .setId(UUID.fromString(applicationSecret.getId()))
                .setApplicationId(UUID.fromString(applicationSecret.getApplicationId()))
                .setAuthorizationServerId(UUID.fromString(applicationSecret.getAuthorizationServerId()))
                .setName(applicationSecret.getName())
                .setScopes(applicationSecret.getScopesList().stream().map(ScopeMapper::toScope).toList())
                .setApplicationSecret(applicationSecret.getApplicationSecret())
                .setExpiresIn(applicationSecret.getExpiresIn())
                .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationSecret.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                .build();
    }

    public static ApplicationSecret toApplicationSecret(ApplicationSecretResponse applicationSecretResponse) {
        return new ApplicationSecret.Builder()
                .setId(UUID.fromString(applicationSecretResponse.getId()))
                .setApplicationId(UUID.fromString(applicationSecretResponse.getApplicationId()))
                .setAuthorizationServerId(UUID.fromString(applicationSecretResponse.getAuthorizationServerId()))
                .setName(applicationSecretResponse.getName())
                .setScopes(applicationSecretResponse.getScopesList().stream().map(ScopeMapper::toScope).toList())
                .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationSecretResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                .setExpiresIn(applicationSecretResponse.getExpiresIn())
                .build();
    }
}
