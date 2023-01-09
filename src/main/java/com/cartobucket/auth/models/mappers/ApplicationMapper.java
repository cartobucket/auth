package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.ApplicationRequest;
import com.cartobucket.auth.model.generated.ApplicationResponse;
import com.cartobucket.auth.models.Application;

public class ApplicationMapper {
    public static Application from(ApplicationRequest applicationRequest) {
        var application = new Application();
        application.setName(applicationRequest.getName());
        application.setAuthorizationServerId(applicationRequest.getAuthorizationServerId());
        if (applicationRequest.getClientId() != null) {
            application.setClientId(applicationRequest.getClientId());
        }
        return application;
    }
    public static ApplicationResponse toResponse(Application application) {
        var applicationResponse = new ApplicationResponse();
        applicationResponse.setId(String.valueOf(application.getId()));
        applicationResponse.setName(application.getName());
        applicationResponse.setClientId(application.getClientId());
        applicationResponse.setAuthorizationServerId(application.getAuthorizationServerId());
        applicationResponse.setCreatedOn(application.getCreatedOn());
        applicationResponse.setUpdatedOn(application.getUpdatedOn());
        return applicationResponse;
    }
}
