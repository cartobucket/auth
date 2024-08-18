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

package com.cartobucket.auth.data.services.grpc.mappers.client;

import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;
import com.cartobucket.auth.data.services.grpc.mappers.MetadataMapper;
import com.cartobucket.auth.rpc.TemplateCreateResponse;
import com.cartobucket.auth.rpc.TemplateResponse;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class TemplateMapper {
    public static Template to(TemplateResponse templateResponse) {
        var template = new Template();
        template.setTemplate(templateResponse.getTemplate().toByteArray());
        template.setId(UUID.fromString(templateResponse.getId()));
        template.setTemplateType(TemplateTypeEnum.valueOf(templateResponse.getTemplateType().toString()));
        template.setAuthorizationServerId(UUID.fromString(templateResponse.getAuthorizationServerId()));
        template.setMetadata(MetadataMapper.from(templateResponse.getMetadata()));
        template.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(templateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        template.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(templateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return template;
    }

    public static Template to(TemplateCreateResponse templateCreateResponse) {
        var template = new Template();
        template.setTemplate(templateCreateResponse.getTemplate().toByteArray());
        template.setId(UUID.fromString(templateCreateResponse.getId()));
        template.setTemplateType(TemplateTypeEnum.valueOf(templateCreateResponse.getTemplateType().toString()));
        template.setAuthorizationServerId(UUID.fromString(templateCreateResponse.getAuthorizationServerId()));
        template.setMetadata(MetadataMapper.from(templateCreateResponse.getMetadata()));
        template.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(templateCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        template.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(templateCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return template;
    }
}
