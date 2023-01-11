package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ApplicationsApi;
import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.repositories.ApplicationRepository;
import com.cartobucket.auth.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class Applications implements ApplicationsApi {
    final ApplicationService applicationService;

    final AuthorizationServerService authorizationServerService;

    public Applications(ApplicationService applicationService, ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository, ProfileRepository profileRepository, AuthorizationServerService authorizationServerService) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response applicationsApplicationIdDelete(UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return Response.ok().build();
    }

    @Override
    public Response applicationsApplicationIdGet(UUID applicationId) {
        return Response.ok().entity(applicationService.getApplication(applicationId)).build();
    }

    @Override
    public Response applicationsApplicationIdSecretsGet(UUID applicationId) {
        return Response.ok().entity(applicationService.getApplicationSecrets(applicationId)).build();
    }

    @Override
    public Response applicationsApplicationIdSecretsPost(UUID applicationId, ApplicationSecretRequest applicationSecretRequest) {
        return Response.ok().entity(applicationService.createApplicationSecret(applicationId, applicationSecretRequest)).build();
    }

    @Override
    public Response applicationsApplicationIdSecretsSecretIdDelete(UUID applicationId, UUID secretId) {
        applicationService.deleteApplicationSecret(applicationId, secretId);
        return Response.ok().build();
    }

    @Override
    public Response applicationsGet() {
        return Response.ok().entity(applicationService.getApplications()).build();
    }

    @Override
    public Response applicationsPost(ApplicationRequest applicationRequest) {
        return Response.ok().entity(applicationService.createApplication(applicationRequest)).build();
    }

}
