package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ApplicationsApi;
import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.ApplicationSecret;
import com.cartobucket.auth.repositories.ApplicationRepository;
import com.cartobucket.auth.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/authorizationServer/applications/")
public class Applications implements ApplicationsApi {
    final ApplicationService applicationService;
    final ApplicationRepository applicationRepository;
    final ApplicationSecretRepository applicationSecretRepository;
    final ProfileRepository profileRepository;

    final AuthorizationServerService authorizationServerService;

    public Applications(ApplicationService applicationService, ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository, ProfileRepository profileRepository, AuthorizationServerService authorizationServerService) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.applicationSecretRepository = applicationSecretRepository;
        this.profileRepository = profileRepository;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public void applicationsApplicationIdDelete(UUID applicationId) {
        applicationRepository.deleteById(applicationId);
    }

    @Override
    public Application applicationsApplicationIdGet(UUID applicationId) {
        return toApplicationDto(applicationRepository.findById(applicationId).get());
    }

    @Override
    public List<ApplicationSecretsResponseInner> applicationsApplicationIdSecretsGet(UUID applicationId) {
        return applicationSecretRepository.findByApplicationId(applicationId)
                .stream()
                .map(Applications::toSecret)
                .toList();
    }

    @Override
    public ApplicationSecretResponse applicationsApplicationIdSecretsPost(UUID applicationId, ApplicationSecretRequest applicationSecretRequest) {
        var secret = applicationService.createApplicationSecret(applicationId, applicationSecretRequest.getName(), new ArrayList<>(), null);
        var response = new ApplicationSecretResponse();
        response.setClientSecret(secret.getClientSecret());
        return response;
    }

    @Override
    public void applicationsApplicationIdSecretsSecretIdDelete(UUID applicationId, UUID secretId) {
        applicationSecretRepository.deleteById(secretId);
    }

    @Override
    public List<Application> applicationsGet() {
        List<Application> applications = new ArrayList<>();
        for (var application : applicationRepository.findAll()) {
            applications.add(toApplicationDto(application));
        }

        return applications;
    }

    private Application toApplicationDto(com.cartobucket.auth.models.Application application) {
        var app = new Application();
        app.setId(application.getId().toString());
        app.setName(application.getName());
        app.setClientId(application.getClientId());
        var profile = profileRepository.findByResource(application.getId());
        app.setProfile(profile.getProfile());
        return app;
    }

    @Override
    public void applicationsPost(ApplicationRequest applicationRequest) {
        var application = new com.cartobucket.auth.models.Application();
        var clientId = UUID.randomUUID();
        var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
        application.setAuthorizationServerId(authorizationServer.getId());
        application.setName(applicationRequest.getName());
        if (applicationRequest.getClientId() != null) {
            application.setClientId(applicationRequest.getClientId());
        } else {
            application.setClientId(clientId.toString());
        }
        if (applicationRequest.getClientSecret() != null) {
            // Gotta deal with this
        }
        applicationRepository.save(application);
    }

    public static ApplicationSecretsResponseInner toSecret(ApplicationSecret secret){
        var _secret = new ApplicationSecretsResponseInner();
        _secret.setId(secret.getId().toString());
        _secret.setName(secret.getName());
        _secret.setExpiration(null);
        return _secret;
    }

}
