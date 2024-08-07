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

package com.cartobucket.auth.api.server.routes;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.generated.TemplatesApi;
import com.cartobucket.auth.model.generated.TemplateRequest;
import com.cartobucket.auth.model.generated.TemplatesResponse;
import com.cartobucket.auth.api.server.routes.mappers.TemplateMapper;
import com.cartobucket.auth.data.services.TemplateService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Templates implements TemplatesApi {
    final TemplateService templateService;

    public Templates(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    public Response createTemplate(TemplateRequest templateRequest) {
        return Response
                .ok()
                .entity(
                        TemplateMapper.toResponse(
                                templateService.createTemplate(TemplateMapper.from(templateRequest))
                        )
                )
                .build();
    }

    @Override
    public Response deleteTemplate(UUID templateId) {
        templateService.deleteTemplate(templateId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getTemplate(UUID templateId) {
        return Response
                .ok()
                .entity(
                        TemplateMapper.toResponse(templateService.getTemplate(templateId))
                )
                .build();
    }

    @Override
    public Response listTemplates(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        // TODO: Probably makes senes to move to Kotlin and use default parameters
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var templatesResponse = new TemplatesResponse();
        templatesResponse.setTemplates(
                templateService.getTemplates(authorizationServerIds, new Page(limit, offset))
                        .stream()
                        .map(TemplateMapper::toResponse)
                        .toList()
        );
        templatesResponse.setPage(getPage("templates", authorizationServerIds, limit, offset));
        return Response
                .ok()
                .entity(templatesResponse)
                .build();
    }

    @Override
    public Response updateTemplate(UUID templateId, TemplateRequest templateRequest) {
        return Response
                .ok()
                .entity(
                        TemplateMapper.toResponse(
                                templateService.updateTemplate(templateId, TemplateMapper.from(templateRequest))
                        )
                )
                .build();
    }
}
