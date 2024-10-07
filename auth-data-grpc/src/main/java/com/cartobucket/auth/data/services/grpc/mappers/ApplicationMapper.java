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

package com.cartobucket.auth.data.services.grpc.mappers;

import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.rpc.ApplicationCreateResponse;
import com.cartobucket.auth.rpc.ApplicationResponse;
import com.cartobucket.auth.rpc.ApplicationSecretCreateResponse;
import com.cartobucket.auth.rpc.ApplicationSecretResponse;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ApplicationMapper {
    public static Application toApplication(ApplicationResponse applicationResponse) {
        return  new Application.Builder()
                .setId(UUID.fromString(applicationResponse.getId()))
                .setName(applicationResponse.getName())
                .setClientId(applicationResponse.getClientId())
                .setAuthorizationServerId(UUID.fromString(applicationResponse.getAuthorizationServerId()))
                .setScopes(applicationResponse.getScopesList().stream().map(ScopeMapper::toScope).toList())
                .setMetadata(MetadataMapper.from(applicationResponse.getMetadata()))
                .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                .build();
    }

    public static Pair<Application, Profile> toApplicationWithProfile(ApplicationCreateResponse applicationCreateResponse, ProfileType profileType) {
        var applicationId = UUID.fromString(applicationCreateResponse.getId());
        var authorizationServerId = UUID.fromString(applicationCreateResponse.getAuthorizationServerId());
        return Pair.create(
                new Application.Builder()
                        .setId(applicationId)
                        .setName(applicationCreateResponse.getName())
                        .setClientId(applicationCreateResponse.getClientId())
                        .setAuthorizationServerId(authorizationServerId)
                        .setScopes(applicationCreateResponse.getScopesList().stream().map(ScopeMapper::toScope).toList())
                        .setMetadata(MetadataMapper.from(applicationCreateResponse.getMetadata()))
                        .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                        .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                        .build(),
                new Profile.Builder()
                        .setProfileType(profileType)
                        .setProfile(ProfileMapper.fromProtoMap(applicationCreateResponse.getProfile().getFieldsMap()))
                        .setResource(applicationId)
                        .setAuthorizationServerId(authorizationServerId)
                        .setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")))
                        .setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(applicationCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")))
                        .build()
        );
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
