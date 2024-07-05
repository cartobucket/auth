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

import com.cartobucket.auth.model.generated.SchemaRequest;
import com.cartobucket.auth.model.generated.SchemaResponse;
import com.cartobucket.auth.data.domain.Schema;

import java.util.Map;

public class SchemaMapper {
    public static SchemaResponse toResponse(Schema schema) {
        var schemaResponse = new SchemaResponse();
        schemaResponse.setSchema(schema.getSchema());
        schemaResponse.setSchemaVersion(schema.getJsonSchemaVersion());
        schemaResponse.setId(schema.getId());
        schemaResponse.setName(schema.getName());
        schemaResponse.setAuthorizationServer(schema.getAuthorizationServerId());
        schemaResponse.setMetadata(MetadataMapper.to(schema.getMetadata()));
        return schemaResponse;
    }

    public static Schema to(SchemaRequest schemaRequest) {
        var schema = new Schema();
        schema.setSchema((Map<String, Object>) schemaRequest.getSchema());
        schema.setJsonSchemaVersion(schemaRequest.getSchemaVersion());
        schema.setName(schemaRequest.getName());
        schema.setAuthorizationServerId(schemaRequest.getAuthorizationServer());
        schema.setMetadata(MetadataMapper.from(schemaRequest.getMetadata()));
        return schema;
    }
}
