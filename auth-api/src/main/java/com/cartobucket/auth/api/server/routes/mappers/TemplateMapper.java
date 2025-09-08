/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.api.dto.TemplateRequest;
import com.cartobucket.auth.api.dto.TemplateResponse;
import static com.cartobucket.auth.api.dto.TemplateTypeEnum.LOGIN;
import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;

public class TemplateMapper {
    public static Template from(TemplateRequest templateRequest) {
        var template = new Template();
        template.setAuthorizationServerId(templateRequest.getAuthorizationServerId());
        template.setTemplate(templateRequest.getTemplate().getBytes());
        template.setMetadata(MetadataMapper.from(templateRequest.getMetadata()));
        switch (templateRequest.getTemplateType()) {
            case "login" -> {
                template.setTemplateType(TemplateTypeEnum.LOGIN);
            }
            default -> throw new IllegalStateException("Unexpected value: " + templateRequest.getTemplateType());
        }
        return template;
    }

    public static TemplateResponse toResponse(Template template) {
        var templateResponse = new TemplateResponse();
        templateResponse.setId(String.valueOf(template.getId()));
        templateResponse.setAuthorizationServerId(template.getAuthorizationServerId());
        templateResponse.setTemplate(new String(template.getTemplate()));
        templateResponse.setMetadata(MetadataMapper.to(template.getMetadata()));
        templateResponse.setTemplateType(template.getTemplateType().toString());
        templateResponse.setCreatedOn(template.getCreatedOn());
        templateResponse.setUpdatedOn(template.getUpdatedOn());
        return templateResponse;
    }

}
