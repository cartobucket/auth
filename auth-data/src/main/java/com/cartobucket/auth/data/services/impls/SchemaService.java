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

import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.exceptions.notfound.SchemaNotFound;
import com.cartobucket.auth.data.rpc.MutinySchemasGrpc;
import com.cartobucket.auth.data.rpc.SchemaCreateRequest;
import com.cartobucket.auth.data.rpc.SchemaDeleteRequest;
import com.cartobucket.auth.data.services.impls.mappers.MetadataMapper;
import com.cartobucket.auth.data.services.impls.mappers.SchemaMapper;
import io.quarkus.arc.DefaultBean;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class SchemaService implements com.cartobucket.auth.data.services.SchemaService {

    @Inject
    @GrpcClient("schemas")
    MutinySchemasGrpc.MutinySchemasStub schemasClient;

    @Override
    public Set<String> validateProfileAgainstSchema(Profile profile, Schema schema) {
        return null;
    }

    @Override
    public Schema createSchema(Schema schema) {
        return SchemaMapper.to(
                schemasClient
                        .createSchema(
                                SchemaCreateRequest
                                        .newBuilder()
                                        .setSchema(Profile.toProtoMap(schema.getSchema()))
                                        .setName(schema.getName())
                                        .setAuthorizationServerId(schema.getAuthorizationServerId().toString())
                                        .setMetadata(MetadataMapper.to(schema.getMetadata()))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public void deleteSchema(UUID schemaId) throws SchemaNotFound {
        schemasClient.deleteSchema(
                SchemaDeleteRequest
                        .newBuilder()
                        .setId(String.valueOf(schemaId))
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public Schema getSchema(UUID schemaId) throws SchemaNotFound {
        return SchemaMapper.to(
                schemasClient
                        .getSchema(
                                com.cartobucket.auth.data.rpc.SchemaGetRequest
                                        .newBuilder()
                                        .setId(String.valueOf(schemaId))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public List<Schema> getSchemas(List<UUID> authorizationServerIds) {
        return schemasClient.listSchemas(
                com.cartobucket.auth.data.rpc.SchemaListRequest
                        .newBuilder()
                        .addAllAuthorizationServerIds(
                                authorizationServerIds
                                        .stream()
                                        .map(String::valueOf)
                                        .toList()
                        )
                        .build()
        )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getSchemasList()
                .stream()
                .map(SchemaMapper::to)
                .toList();
    }

    @Override
    public Schema updateSchema(UUID schemaId, Schema schema) throws SchemaNotFound {
        return null;
    }
}
