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
import com.cartobucket.auth.generated.SchemasApi;
import com.cartobucket.auth.model.generated.SchemaRequest;
import com.cartobucket.auth.model.generated.SchemasResponse;
import com.cartobucket.auth.api.server.routes.mappers.SchemaMapper;
import com.cartobucket.auth.data.services.SchemaService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Schemas implements SchemasApi {
    final SchemaService schemaService;

    public Schemas(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Override
    public Response createSchemaRequest(SchemaRequest schemaRequest) {
        return Response
                .ok()
                .entity(
                        SchemaMapper.toResponse(
                                schemaService.createSchema(SchemaMapper.to(schemaRequest))
                        )
                )
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
                .entity(SchemaMapper.toResponse(schemaService.getSchema(schemaId)))
                .build();
    }

    @Override
    public Response getSchemaRequests(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        final var schemasResponse = new SchemasResponse();
        schemasResponse.setSchemas(
                schemaService.getSchemas(authorizationServerIds, new Page(limit, offset))
                        .stream()
                        .map(SchemaMapper::toResponse)
                        .toList()
        );
        schemasResponse.setPage(
                getPage("schemas", authorizationServerIds, limit, offset)
        );
        return Response
                .ok()
                .entity(schemasResponse)
                .build();
    }

    @Override
    public Response updateSchemaRequest(UUID schemaId, SchemaRequest schemaRequest) {
        return Response
                .ok()
                .entity(
                        SchemaMapper.toResponse(
                                schemaService.updateSchema(schemaId, SchemaMapper.to(schemaRequest))
                        )
                )
                .build();
    }
}
