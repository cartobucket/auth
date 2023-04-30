package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ApplicationsApi;
import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.repositories.ApplicationRepository;
import com.cartobucket.auth.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.routes.mappers.ApplicationRequestFilterMapper;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class Applications implements ApplicationsApi {
    final ApplicationService applicationService;

    final AuthorizationServerService authorizationServerService;

    public Applications(ApplicationService applicationService, ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository, ProfileRepository profileRepository, AuthorizationServerService authorizationServerService) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response createApplication(ApplicationRequest applicationRequest) {
        return Response.ok().entity(applicationService.createApplication(applicationRequest)).build();
    }

    @Override
    public Response createApplicationSecret(UUID applicationId, ApplicationSecretRequest applicationSecretRequest) {
        return Response.ok().entity(applicationService.createApplicationSecret(applicationId, applicationSecretRequest)).build();
    }

    @Override
    public Response deleteApplication(UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return Response.ok().build();
    }

    @Override
    public Response deleteApplicationSecret(UUID applicationId, UUID secretId) {
        applicationService.deleteApplicationSecret(applicationId, secretId);
        return Response.ok().build();
    }

    @Override
    public Response getApplication(UUID applicationId) {
        return Response.ok().entity(applicationService.getApplication(applicationId)).build();
    }

    @Override
    public Response listApplicationSecrets(UUID applicationId) {
        return Response.ok().entity(applicationService.getApplicationSecrets(applicationId)).build();
    }

    @Override
    public Response listApplications(List<UUID> authorizationServerIds) {
        return Response
                .ok()
                .entity(applicationService.getApplications(ApplicationRequestFilterMapper.to(authorizationServerIds)))
                .build();
    }
}
