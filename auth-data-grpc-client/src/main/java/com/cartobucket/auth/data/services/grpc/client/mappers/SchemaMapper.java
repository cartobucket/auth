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

package com.cartobucket.auth.data.services.grpc.client.mappers;

import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.rpc.SchemaResponse;
import com.cartobucket.auth.data.services.grpc.mappers.MetadataMapper;
import com.cartobucket.auth.data.services.grpc.mappers.ProfileMapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class SchemaMapper {
    public static Schema to(SchemaResponse schemaResponse) {
        var schema = new Schema();
        schema.setId(UUID.fromString(schemaResponse.getId()));
        schema.setName(schemaResponse.getName());
        schema.setSchema(ProfileMapper.fromProtoMap(schemaResponse.getSchema().getFieldsMap()));
        schema.setAuthorizationServerId(UUID.fromString(schemaResponse.getAuthorizationServerId()));
        schema.setMetadata(MetadataMapper.from(schemaResponse.getMetadata()));
        schema.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(schemaResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        schema.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(schemaResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return schema;
    }
}
