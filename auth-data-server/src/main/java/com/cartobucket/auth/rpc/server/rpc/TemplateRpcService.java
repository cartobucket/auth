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

package com.cartobucket.auth.rpc.server.rpc;


import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;
import com.cartobucket.auth.data.services.TemplateService;
import com.cartobucket.auth.rpc.TemplateCreateRequest;
import com.cartobucket.auth.rpc.TemplateCreateResponse;
import com.cartobucket.auth.rpc.TemplateDeleteRequest;
import com.cartobucket.auth.rpc.TemplateGetRequest;
import com.cartobucket.auth.rpc.TemplateListRequest;
import com.cartobucket.auth.rpc.TemplateListResponse;
import com.cartobucket.auth.rpc.TemplateResponse;
import com.cartobucket.auth.rpc.TemplateUpdateRequest;
import com.cartobucket.auth.rpc.Templates;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.util.UUID;

@GrpcService
public class TemplateRpcService implements Templates {
    final TemplateService templateService;

    public TemplateRpcService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    @Blocking
    public Uni<TemplateCreateResponse> createTemplate(TemplateCreateRequest request) {
        var template = new Template();
        template.setTemplateType(
                switch (request.getTemplateType()) {
                    default -> TemplateTypeEnum.LOGIN;
                }
        );
        template.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
        template.setTemplate(request.getTemplate().toByteArray());
        template = templateService.createTemplate(template);

        return Uni
                .createFrom()
                .item(
                        TemplateCreateResponse
                                .newBuilder()
                                .setId(String.valueOf(template.getId()))
                                .setAuthorizationServerId(String.valueOf(template.getAuthorizationServerId()))
                                .setTemplateType(switch (template.getTemplateType()){
                                            default -> TemplateCreateResponse.TEMPLATE_TYPE.LOGIN;
                                        }
                                )
                                .setTemplate(ByteString.copyFrom(template.getTemplate()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(template.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(template.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<TemplateListResponse> listTemplates(TemplateListRequest request) {
        final var templates = templateService.getTemplates(
                request
                        .getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList()
        );
        return Uni
                .createFrom()
                .item(
                        TemplateListResponse
                                .newBuilder()
                                .addAllTemplates(
                                        templates
                                                .stream()
                                                .map(template -> TemplateResponse
                                                        .newBuilder()
                                                        .setId(String.valueOf(template.getId()))
                                                        .setAuthorizationServerId(String.valueOf(template.getAuthorizationServerId()))
                                                        .setTemplateType(TemplateResponse.TEMPLATE_TYPE.valueOf(template.getTemplateType().name()))
                                                        .setTemplate(ByteString.copyFrom(template.getTemplate()))
                                                        .setCreatedOn(Timestamp.newBuilder().setSeconds(template.getCreatedOn().toEpochSecond()).build())
                                                        .setUpdatedOn(Timestamp.newBuilder().setSeconds(template.getUpdatedOn().toEpochSecond()).build())
                                                        .build())
                                                .toList()
                                )
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<TemplateResponse> deleteTemplate(TemplateDeleteRequest request) {
        templateService.deleteTemplate(UUID.fromString(request.getId()));
        return Uni
                .createFrom()
                .item(
                        TemplateResponse
                                .newBuilder()
                                .setId(request.getId())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<TemplateResponse> updateTemplate(TemplateUpdateRequest request) {
        var template = new Template();
        template.setTemplate(request.getTemplate().toByteArray());
        template = templateService.updateTemplate(template.getId(), template);

        return Uni
                .createFrom()
                .item(
                        TemplateResponse
                                .newBuilder()
                                .setId(String.valueOf(template.getId()))
                                .setAuthorizationServerId(String.valueOf(template.getAuthorizationServerId()))
                                .setTemplateType(TemplateResponse.TEMPLATE_TYPE.valueOf(template.getTemplateType().name()))
                                .setTemplate(ByteString.copyFrom(template.getTemplate()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(template.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(template.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );    }

    @Override
    @Blocking
    public Uni<TemplateResponse> getTemplate(TemplateGetRequest request) {
        final var template = templateService.getTemplate(UUID.fromString(request.getId()));

        return Uni
                .createFrom()
                .item(
                        TemplateResponse
                                .newBuilder()
                                .setId(String.valueOf(template.getId()))
                                .setAuthorizationServerId(String.valueOf(template.getAuthorizationServerId()))
                                .setTemplateType(TemplateResponse.TEMPLATE_TYPE.valueOf(template.getTemplateType().name()))
                                .setTemplate(ByteString.copyFrom(template.getTemplate()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(template.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(template.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }
}
