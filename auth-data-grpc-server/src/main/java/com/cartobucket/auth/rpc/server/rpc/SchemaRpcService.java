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

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.rpc.SchemaCreateRequest;
import com.cartobucket.auth.data.rpc.SchemaDeleteRequest;
import com.cartobucket.auth.data.rpc.SchemaGetRequest;
import com.cartobucket.auth.data.rpc.SchemaListRequest;
import com.cartobucket.auth.data.rpc.SchemaResponse;
import com.cartobucket.auth.data.rpc.Schemas;
import com.cartobucket.auth.data.rpc.SchemasListResponse;
import com.cartobucket.auth.rpc.server.rpc.mappers.MetadataMapper;
import com.cartobucket.auth.postgres.client.services.SchemaService;
import com.google.protobuf.Timestamp;
import com.networknt.schema.SpecVersion;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.util.UUID;

@GrpcService
public class SchemaRpcService implements Schemas {
    final SchemaService schemaService;

    public SchemaRpcService(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Override
    @Blocking
    public Uni<SchemaResponse> createSchema(SchemaCreateRequest request) {
        var schema = new Schema();
        schema.setName(request.getName());
        schema.setSchema(Profile.fromProtoMap(request.getSchema().getFieldsMap()));
        schema.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
        schema.setJsonSchemaVersion(String.valueOf(SpecVersion.VersionFlag.V202012));
        schema.setMetadata(MetadataMapper.toMetadata(request.getMetadata()));
        schema = schemaService.createSchema(schema);
        return Uni.createFrom().item(
                SchemaResponse.newBuilder()
                        .setId(String.valueOf(schema.getId()))
                        .setName(schema.getName())
                        .setSchema(Profile.toProtoMap(schema.getSchema()))
                        .setAuthorizationServerId(String.valueOf(schema.getAuthorizationServerId()))
                        .setMetadata(request.getMetadata())
                        .setCreatedOn(Timestamp.newBuilder().setSeconds(schema.getCreatedOn().toEpochSecond()).build())
                        .setUpdatedOn(Timestamp.newBuilder().setSeconds(schema.getUpdatedOn().toEpochSecond()).build())
                        .build()
                );
    }

    @Override
    @Blocking
    public Uni<SchemasListResponse> listSchemas(SchemaListRequest request) {
        return Uni
                .createFrom()
                .item(
                        SchemasListResponse
                                .newBuilder()
                                .addAllSchemas(schemaService
                                        .getSchemas(
                                                request
                                                        .getAuthorizationServerIdsList()
                                                        .stream()
                                                        .map(UUID::fromString)
                                                        .toList(),
                                                new Page(
                                                        Long.valueOf(request.getLimit()).intValue(),
                                                        Long.valueOf(request.getOffset()).intValue()
                                                )
                                        )
                                        .stream()
                                        .map(schema -> SchemaResponse
                                                .newBuilder()
                                                .setId(String.valueOf(schema.getId()))
                                                .setName(schema.getName())
                                                .setSchema(Profile.toProtoMap(schema.getSchema()))
                                                .setAuthorizationServerId(String.valueOf(schema.getAuthorizationServerId()))
                                                .setMetadata(MetadataMapper.from(schema.getMetadata()))
                                                .setCreatedOn(Timestamp.newBuilder().setSeconds(schema.getCreatedOn().toEpochSecond()).build())
                                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(schema.getUpdatedOn().toEpochSecond()).build())
                                                .build()
                                        )
                                        .toList()
                                )
                                .build());
    }

    @Override
    @Blocking
    public Uni<SchemaResponse> deleteSchema(SchemaDeleteRequest request) {
        schemaService.deleteSchema(UUID.fromString(request.getId()));
        return Uni.createFrom().item(SchemaResponse.newBuilder().build());
    }

    @Override
    @Blocking
    public Uni<SchemaResponse> getSchema(SchemaGetRequest request) {
        final var schema = schemaService.getSchema(UUID.fromString(request.getId()));
        return Uni.createFrom().item(
                SchemaResponse.newBuilder()
                        .setId(String.valueOf(schema.getId()))
                        .setName(schema.getName())
                        .setSchema(Profile.toProtoMap(schema.getSchema()))
                        .setAuthorizationServerId(String.valueOf(schema.getAuthorizationServerId()))
                        .setMetadata(MetadataMapper.from(schema.getMetadata()))
                        .setCreatedOn(Timestamp.newBuilder().setSeconds(schema.getCreatedOn().toEpochSecond()).build())
                        .setUpdatedOn(Timestamp.newBuilder().setSeconds(schema.getUpdatedOn().toEpochSecond()).build())
                        .build());
    }
}
