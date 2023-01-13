package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.TemplatesApi;
import com.cartobucket.auth.model.generated.TemplateRequest;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class Templates implements TemplatesApi {
    @Override
    public Response templatesGet(List<UUID> authorizationServerId) {
        return Response.ok().build();
    }

    @Override
    public Response templatesPost(TemplateRequest templateRequest) {
        return Response.ok().build();
    }

    @Override
    public Response templatesTemplateIdDelete(UUID templateId) {
        return Response.ok().build();
    }

    @Override
    public Response templatesTemplateIdGet(UUID templateId) {
        return Response.ok().build();
    }

    @Override
    public Response templatesTemplateIdPut(UUID templateId, TemplateRequest templateRequest) {
        return Response.ok().build();
    }
}
