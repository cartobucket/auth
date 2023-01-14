package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.Template;

import java.util.UUID;

public interface TemplateService {
    TemplatesResponse getTemplates(TemplateRequestFilter filter);

    TemplateResponse createTemplate(TemplateRequest templateRequest);

    void deleteTemplate(UUID templateId);

    TemplateResponse getTemplate(UUID templateId);

    Template getTemplateForAuthorizationServer(UUID authorizationServer);

    TemplateResponse updateTemplate(UUID templateId, TemplateRequest userRequest);
}
