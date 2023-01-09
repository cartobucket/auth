package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ApplicationsApi;
import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.ApplicationSecret;
import com.cartobucket.auth.repositories.ApplicationRepository;
import com.cartobucket.auth.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;

import java.util.UUID;

public class Applications implements ApplicationsApi {
    final ApplicationService applicationService;
//    final ApplicationRepository applicationRepository;
//    final ApplicationSecretRepository applicationSecretRepository;
//    final ProfileRepository profileRepository;

    final AuthorizationServerService authorizationServerService;

    public Applications(ApplicationService applicationService, ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository, ProfileRepository profileRepository, AuthorizationServerService authorizationServerService) {
        this.applicationService = applicationService;
//        this.applicationRepository = applicationRepository;
//        this.applicationSecretRepository = applicationSecretRepository;
//        this.profileRepository = profileRepository;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public void applicationsApplicationIdDelete(UUID applicationId) {
        applicationService.deleteApplication(applicationId);
    }

    @Override
    public ApplicationResponse applicationsApplicationIdGet(UUID applicationId) {
        return applicationService.getApplication(applicationId);
//        return ApplicationMapper.toResponse(applicationRepository.findById(applicationId).get());
    }

    @Override
    public ApplicationSecretsResponse applicationsApplicationIdSecretsGet(UUID applicationId) {
        return applicationService.getApplicationSecrets(applicationId);
//        return applicationSecretRepository.findByApplicationId(applicationId)
//                .stream()
//                .map(Applications::toSecret)
//                .toList();
    }

    @Override
    public ApplicationSecretResponse applicationsApplicationIdSecretsPost(UUID applicationId, ApplicationSecretRequest applicationSecretRequest) {
        return applicationService.createApplicationSecret(applicationId, applicationSecretRequest);
//        var secret = applicationService.createApplicationSecret(applicationId, applicationSecretRequest.getName(), new ArrayList<>(), null);
//        var response = new ApplicationSecretResponse();
//        response.setClientSecret(secret.getClientSecret());
//        return response;
    }

    @Override
    public void applicationsApplicationIdSecretsSecretIdDelete(UUID applicationId, UUID secretId) {
        applicationService.deleteApplicationSecret(applicationId, secretId);
    }

    @Override
    public ApplicationsResponse applicationsGet() {
        return applicationService.getApplications();
    }

    @Override
    public ApplicationResponse applicationsPost(ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
        //        var application = new com.cartobucket.auth.models.Application();
//        var clientId = UUID.randomUUID();
//        var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
//        application.setAuthorizationServerId(authorizationServer.getId());
//        application.setName(applicationRequest.getName());
//        if (applicationRequest.getClientId() != null) {
//            application.setClientId(applicationRequest.getClientId());
//        } else {
//            application.setClientId(clientId.toString());
//        }
//        if (applicationRequest.getClientSecret() != null) {
//            // Gotta deal with this
//        }
//        return ApplicationMapper.toResponse(applicationRepository.save(application));
    }

}
