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

package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.SchemasApi;
import com.cartobucket.auth.model.generated.SchemaRequest;
import com.cartobucket.auth.routes.mappers.SchemaRequestFilterMapper;
import com.cartobucket.auth.services.SchemaService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class Schemas implements SchemasApi {
    final SchemaService schemaService;

    public Schemas(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Override
    public Response createSchemaRequest(SchemaRequest schemaRequest) {
        return Response
                .ok()
                .entity(schemaService.createSchema(schemaRequest))
                .build();
    }

    @Override
    public Response deleteSchemaRequest(UUID schemaId) {
        schemaService.deleteSchema(schemaId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getSchemaRequest(UUID schemaId) {
        return Response
                .ok()
                .entity(schemaService.getSchema(schemaId))
                .build();
    }

    @Override
    public Response getSchemaRequests(List<UUID> authorizationServers) {
        final var requestFilter = SchemaRequestFilterMapper.to(authorizationServers);
        return Response
                .ok()
                .entity(schemaService.getSchemas(requestFilter))
                .build();
    }

    @Override
    public Response updateSchemaRequest(UUID schemaId, SchemaRequest schemaRequest) {
        return Response
                .ok()
                .entity(schemaService.updateSchema(schemaId, schemaRequest))
                .build();
    }
}
