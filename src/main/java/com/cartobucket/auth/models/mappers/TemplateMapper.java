package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.TemplateRequest;
import com.cartobucket.auth.model.generated.TemplateResponse;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.models.Template;
import com.cartobucket.auth.models.User;

import java.util.UUID;

public class TemplateMapper {
    public static Template from(TemplateRequest templateRequest) {
        var template = new Template();
        template.setAuthorizationServerId(templateRequest.getAuthorizationServerId());
        template.setTemplate(templateRequest.getTemplate().getBytes());
        template.setTemplateType(templateRequest.getTemplateType());
        return template;
    }

    public static TemplateResponse toResponse(Template template) {
        var templateResponse = new TemplateResponse();
        templateResponse.setId(String.valueOf(template.getId()));
        templateResponse.setAuthorizationServerId(template.getAuthorizationServerId());
        templateResponse.setTemplate(new String(template.getTemplate()));
        templateResponse.setTemplateType(template.getTemplateType().toString());
        templateResponse.setCreatedOn(template.getCreatedOn());
        templateResponse.setUpdatedOn(template.getUpdatedOn());
        return templateResponse;
    }

}
