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


import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.rpc.ApplicationCreateRequest;
import com.cartobucket.auth.rpc.ApplicationCreateResponse;
import com.cartobucket.auth.rpc.ApplicationDeleteRequest;
import com.cartobucket.auth.rpc.ApplicationGetRequest;
import com.cartobucket.auth.rpc.ApplicationListRequest;
import com.cartobucket.auth.rpc.ApplicationListResponse;
import com.cartobucket.auth.rpc.ApplicationResponse;
import com.cartobucket.auth.rpc.ApplicationUpdateRequest;
import com.cartobucket.auth.rpc.Applications;
import com.cartobucket.auth.rpc.server.entities.mappers.ProfileMapper;
import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@GrpcService
public class ApplicationRpcService implements Applications {
    final ApplicationService applicationService;

    public ApplicationRpcService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    @Blocking
    public Uni<ApplicationCreateResponse> createApplication(ApplicationCreateRequest request) {
        var application = new Application();
        application.setClientId(request.getClientId());
        application.setName(request.getName());
        application.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));

        var profile = new Profile();
        profile.setProfile(ProfileMapper.fromProtoMap(request.getProfile().getFieldsMap()));

        final var applicationAndProfile = applicationService.createApplication(application, profile);
        final var _application = applicationAndProfile.getLeft();
        final var _profile = applicationAndProfile.getRight();

        return Uni
                .createFrom()
                .item(
                        ApplicationCreateResponse
                                .newBuilder()
                                .setId(String.valueOf(_application.getId()))
                                .setAuthorizationServerId(String.valueOf(_application.getAuthorizationServerId()))
                                .setName(_application.getName())
                                .setClientId(_application.getClientId())
                                .setProfile(ProfileMapper.toProtoMap(_profile.getProfile()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(_application.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(_application.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ApplicationListResponse> listApplications(ApplicationListRequest request) {
        final var applications = applicationService.getApplications(
                request
                        .getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList()
        );
        return Uni
                .createFrom()
                .item(
                        ApplicationListResponse
                                .newBuilder()
                                .addAllApplications(applications
                                        .stream()
                                        .map(application -> ApplicationResponse
                                                .newBuilder()
                                                .setId(String.valueOf(application.getId()))
                                                .setAuthorizationServerId(String.valueOf(application.getAuthorizationServerId()))
                                                .setName(application.getName())
                                                .setClientId(application.getClientId())
                                                .setCreatedOn(Timestamp.newBuilder().setSeconds(application.getCreatedOn().toEpochSecond()).build())
                                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(application.getUpdatedOn().toEpochSecond()).build())
                                                .build()
                                        )
                                        .toList()
                                )
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ApplicationResponse> deleteApplication(ApplicationDeleteRequest request) {
        final var applicationId = UUID.fromString(request.getId());
        applicationService.deleteApplication(applicationId);
        return Uni.createFrom().item(ApplicationResponse.newBuilder().setId(request.getId()).build());
    }

    @Override
    @Blocking
    public Uni<ApplicationResponse> updateApplication(ApplicationUpdateRequest request) {
        final var applicationAndProfile = applicationService.getApplication(UUID.fromString(request.getId()));
        final var application = applicationAndProfile.getLeft();
        application.setName(request.getName());

        final var profile = applicationAndProfile.getRight();
        profile.setProfile(ProfileMapper.fromProtoMap(request.getProfile().getFieldsMap()));

        // TODO: Implement the update in the service.
        return Uni
                .createFrom()
                .item(
                        ApplicationResponse
                                .newBuilder()
                                .setId(String.valueOf(application.getId()))
                                .setAuthorizationServerId(String.valueOf(application.getAuthorizationServerId()))
                                .setName(application.getName())
                                .setClientId(application.getClientId())
                                .setProfile(ProfileMapper.toProtoMap(profile.getProfile()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(application.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(application.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ApplicationResponse> getApplication(ApplicationGetRequest request) {
        final var applicationAndProfile = applicationService.getApplication(UUID.fromString(request.getApplicationId()));
        final var application = applicationAndProfile.getLeft();
        final var profile = applicationAndProfile.getRight();
        return Uni
                .createFrom()
                .item(
                        ApplicationResponse
                                .newBuilder()
                                .setId(String.valueOf(application.getId()))
                                .setAuthorizationServerId(String.valueOf(application.getAuthorizationServerId()))
                                .setName(application.getName())
                                .setClientId(application.getClientId())
                                .setProfile(ProfileMapper.toProtoMap(profile.getProfile()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(application.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(application.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }
}
