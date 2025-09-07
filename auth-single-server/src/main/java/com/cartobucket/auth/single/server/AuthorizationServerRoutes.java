package com.cartobucket.auth.single.server;

import com.cartobucket.auth.authorization.server.routes.AuthorizationServer;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound;
import com.cartobucket.auth.data.services.*;
import io.quarkus.qute.Qute;
import jakarta.ws.rs.core.Response;

import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

public class AuthorizationServerRoutes extends AuthorizationServer {
    private final TemplateService templateService;

    public AuthorizationServerRoutes(AuthorizationServerService authorizationServerService, ClientService clientService, UserService userService, TemplateService templateService, ApplicationService applicationService, ScopeService scopeService) {
        super(authorizationServerService, clientService, userService, templateService, applicationService, scopeService);
        this.templateService = templateService;
    }

    @Override
    protected Response renderLoginScreen(UUID authorizationServerId) {
        return renderLoginScreen(authorizationServerId, null);
    }
    
    @Override
    protected Response renderLoginScreen(UUID authorizationServerId, String errorMessage) {
        final var template = this.templateService
                .getTemplates(Collections.singletonList(authorizationServerId), new Page(1, 0))
                .stream()
                .filter(t -> t.getTemplateType() == TemplateTypeEnum.LOGIN)
                .findFirst()
                .orElseThrow(TemplateNotFound::new);

        var quteTemplate = Qute.fmt(new String(Base64.getDecoder().decode(template.getTemplate())));
        
        // Always set error data (null if no error)
        quteTemplate = quteTemplate.data("error", errorMessage);
        
        return Response
                .ok()
                .entity(quteTemplate.render())
                .build();
    }
}
