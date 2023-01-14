package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.TemplatesApi;
import com.cartobucket.auth.model.generated.TemplateRequest;
import com.cartobucket.auth.model.generated.TemplateRequestFilter;
import com.cartobucket.auth.routes.mappers.TemplateRequestFilterMapper;
import com.cartobucket.auth.services.TemplateService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class Templates implements TemplatesApi {
    final TemplateService templateService;

    public Templates(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    public Response templatesGet(List<UUID> authorizationServerId) {
        return Response
                .ok()
                .entity(templateService.getTemplates(TemplateRequestFilterMapper.to(authorizationServerId)))
                .build();
    }

    @Override
    public Response templatesPost(TemplateRequest templateRequest) {
        return Response
                .ok()
                .entity(templateService.createTemplate(templateRequest))
                .build();
    }

    @Override
    public Response templatesTemplateIdDelete(UUID templateId) {
        return Response.ok().build();
    }

    @Override
    public Response templatesTemplateIdGet(UUID templateId) {
        return Response.ok().entity(templateService.getTemplate(templateId)).build();
    }

    @Override
    public Response templatesTemplateIdPut(UUID templateId, TemplateRequest templateRequest) {
        return Response.ok().entity(templateService.updateTemplate(templateId, templateRequest)).build();
    }
}
