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

import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.exceptions.badrequests.ApplicationSecretNoApplicationBadData;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationSecretNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.services.impls.mappers.ApplicationMapper;
import com.cartobucket.auth.data.services.impls.mappers.ProfileMapper;
import com.cartobucket.auth.rpc.ApplicationCreateRequest;
import com.cartobucket.auth.rpc.ApplicationDeleteRequest;
import com.cartobucket.auth.rpc.ApplicationGetRequest;
import com.cartobucket.auth.rpc.ApplicationListRequest;
import com.cartobucket.auth.rpc.ApplicationSecretCreateRequest;
import com.cartobucket.auth.rpc.ApplicationSecretDeleteRequest;
import com.cartobucket.auth.rpc.ApplicationSecretListRequest;
import com.cartobucket.auth.rpc.MutinyApplicationSecretsGrpc;
import com.cartobucket.auth.rpc.MutinyApplicationsGrpc;
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
public class ApplicationService implements com.cartobucket.auth.data.services.ApplicationService {
    @Inject
    @GrpcClient("applications")
    MutinyApplicationsGrpc.MutinyApplicationsStub applicationClient;

    @Inject
    @GrpcClient("applicationSecrets")
    MutinyApplicationSecretsGrpc.MutinyApplicationSecretsStub applicationSecretsClient;

    @Override
    public void deleteApplication(UUID applicationId) throws ApplicationNotFound {
        applicationClient.deleteApplication(
                ApplicationDeleteRequest
                        .newBuilder()
                        .setId(String.valueOf(applicationId))
                        .build()
        );
    }

    @Override
    public Pair<Application, Profile> getApplication(UUID applicationId) throws ApplicationNotFound, ProfileNotFound {
        var application = applicationClient.getApplication(
                ApplicationGetRequest
                        .newBuilder()
                        .setId(String.valueOf(applicationId))
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
        return Pair.create(
                ApplicationMapper.toApplication(application),
                ProfileMapper.toProfile(application.getProfile(), ProfileType.Application)
        );
    }

    @Override
    public Pair<Application, Profile> createApplication(Application application, Profile profile) {
        return ApplicationMapper.toApplicationWithProfile (
                applicationClient.createApplication(
                        ApplicationCreateRequest
                                .newBuilder()
                                .setName(application.getName())
                                .setProfile(Profile.toProtoMap(profile.getProfile()))
                                .setAuthorizationServerId(String.valueOf(application.getAuthorizationServerId()))
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS)),
                ProfileType.Application
        );
    }

    @Override
    public List<ApplicationSecret> getApplicationSecrets(UUID applicationId) throws ApplicationNotFound {
        return applicationSecretsClient.listApplicationSecrets(
                ApplicationSecretListRequest
                        .newBuilder()
                        .setApplicationId(String.valueOf(applicationId))
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getApplicationSecretsList()
                .stream()
                .map(ApplicationMapper::toApplicationSecret)
                .toList();
    }

    @Override
    public ApplicationSecret createApplicationSecret(UUID applicationId, ApplicationSecret applicationSecret) throws ApplicationNotFound {
        return ApplicationMapper.toApplicationSecret(
                applicationSecretsClient.createApplicationSecret(
                        ApplicationSecretCreateRequest
                                .newBuilder()
                                .setApplicationId(String.valueOf(applicationId))
                                .setAuthorizationServerId(String.valueOf(applicationSecret.getAuthorizationServerId()))
                                .setName(applicationSecret.getName())
                                .build()
                )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public void deleteApplicationSecret(UUID applicationId, UUID secretId) throws ApplicationSecretNoApplicationBadData, ApplicationSecretNotFound, ApplicationNotFound {
        applicationSecretsClient.deleteApplicationSecret(
                ApplicationSecretDeleteRequest
                        .newBuilder()
                        .setApplicationId(String.valueOf(applicationId))
                        .setId(String.valueOf(secretId) )
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public List<Application> getApplications(List<UUID> authorizationServerIds) {
        var applicationListRequest = ApplicationListRequest
                .newBuilder()
                .addAllAuthorizationServerIds(
                        authorizationServerIds
                                .stream()
                                .map(String::valueOf)
                                .toList()
                )
                .build();

        return applicationClient
                .listApplications(applicationListRequest)
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getApplicationsList()
                .stream()
                .map(ApplicationMapper::toApplication)
                .toList();
    }
}
