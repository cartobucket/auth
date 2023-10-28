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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound;
import com.cartobucket.auth.data.services.impls.mappers.MetadataMapper;
import com.cartobucket.auth.data.services.impls.mappers.TemplateMapper;
import com.cartobucket.auth.rpc.MutinyTemplatesGrpc;
import com.cartobucket.auth.rpc.TemplateCreateRequest;
import com.cartobucket.auth.rpc.TemplateDeleteRequest;
import com.cartobucket.auth.rpc.TemplateGetRequest;
import com.cartobucket.auth.rpc.TemplateListRequest;
import com.cartobucket.auth.rpc.TemplateUpdateRequest;
import com.google.protobuf.ByteString;
import io.quarkus.arc.DefaultBean;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class TemplateService implements com.cartobucket.auth.data.services.TemplateService {

    @Inject
    @GrpcClient("templates")
    MutinyTemplatesGrpc.MutinyTemplatesStub templatesClient;

    @Override
    public List<Template> getTemplates(List<UUID> authorizationServerIds) {
        return templatesClient
                .listTemplates(
                        TemplateListRequest
                                .newBuilder()
                                .addAllAuthorizationServerIds(authorizationServerIds.stream().map(UUID::toString).toList())
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getTemplatesList()
                .stream()
                .map(TemplateMapper::to)
                .toList();
    }

    @Override
    public Template createTemplate(Template template) {
        return TemplateMapper.to(
                templatesClient
                        .createTemplate(
                                TemplateCreateRequest
                                        .newBuilder()
                                        .setTemplate(ByteString.copyFrom(template.getTemplate()))
                                        .setMetadata(MetadataMapper.to(template.getMetadata()))
                                        .setTemplateType(
                                                switch (template.getTemplateType()) {
                                                    default -> TemplateCreateRequest.TEMPLATE_TYPE.LOGIN;
                                                }
                                        )
                                        .setAuthorizationServerId(String.valueOf(template.getAuthorizationServerId()))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public void deleteTemplate(UUID templateId) throws TemplateNotFound {
        templatesClient
                .deleteTemplate(
                        TemplateDeleteRequest
                                .newBuilder()
                                .setId(String.valueOf(templateId))
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public Template getTemplate(UUID templateId) throws TemplateNotFound {
        return TemplateMapper.to(
                templatesClient
                        .getTemplate(
                                TemplateGetRequest
                                        .newBuilder()
                                        .setId(String.valueOf(templateId))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public Template updateTemplate(UUID templateId, Template template) throws TemplateNotFound {
        return TemplateMapper.to(
                templatesClient
                        .updateTemplate(
                                TemplateUpdateRequest
                                        .newBuilder()
                                        .setId(String.valueOf(templateId))
                                        .setTemplate(ByteString.copyFrom(template.getTemplate()))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }
}
