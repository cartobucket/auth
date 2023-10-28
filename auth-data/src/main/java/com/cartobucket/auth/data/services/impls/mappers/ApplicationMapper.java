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

package com.cartobucket.auth.data.services.impls.mappers;

import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.rpc.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;

public class ApplicationMapper {
    public static Application toApplication(ApplicationResponse applicationResponse) {
        var application = new Application();
        application.setId(UUID.fromString(applicationResponse.getId()));
        application.setName(applicationResponse.getName());
        application.setClientId(applicationResponse.getClientId());
        application.setAuthorizationServerId(UUID.fromString(applicationResponse.getAuthorizationServerId()));
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
        _applicationSecret.setAuthorizationServerId(UUID.fromString(applicationSecret.getAuthorizationServerId()));
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
