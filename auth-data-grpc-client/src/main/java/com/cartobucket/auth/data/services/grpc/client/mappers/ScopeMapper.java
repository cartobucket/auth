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

import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.grpc.mappers.MetadataMapper;
import com.cartobucket.auth.rpc.Metadata;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ScopeMapper {
    public static com.cartobucket.auth.data.domain.Scope toScope(com.cartobucket.auth.data.rpc.Scope scopeResponse) {
        var scope = new com.cartobucket.auth.data.domain.Scope();
        // TODO: This is obviously not right
        scope.setId(!scopeResponse.getId().equals("null") ? UUID.fromString(scopeResponse.getId()) : UUID.randomUUID());
        scope.setName(scopeResponse.getName());
        // TODO: This too is not correct.
        final var authorizationServer = new com.cartobucket.auth.data.domain.AuthorizationServer();
        authorizationServer.setId(!scopeResponse.getAuthorizationServerId().equals("null") ? UUID.fromString(scopeResponse.getAuthorizationServerId()) : UUID.randomUUID());
        scope.setAuthorizationServer(authorizationServer);
        scope.setMetadata(MetadataMapper.from(scopeResponse.getMetadata()));
        scope.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(scopeResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        scope.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(scopeResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return scope;
    }

    public static com.cartobucket.auth.data.domain.Scope to(String scope) {
        var scopeObj = new com.cartobucket.auth.data.domain.Scope();
        scopeObj.setName(scope);
        return scopeObj;
    }

    public static com.cartobucket.auth.data.rpc.Scope toResponse(Scope scope) {
        // TODO: Fix this too.
        return com.cartobucket.auth.data.rpc.Scope.newBuilder()
                .setId(scope.getId() != null ? scope.getId().toString() : String.valueOf(UUID.randomUUID()))
                .setName(scope.getName())
                .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer().getId()))
                .setMetadata(scope.getMetadata() != null ? MetadataMapper.to(scope.getMetadata()) : Metadata.newBuilder().build())
                .build();
    }
}
