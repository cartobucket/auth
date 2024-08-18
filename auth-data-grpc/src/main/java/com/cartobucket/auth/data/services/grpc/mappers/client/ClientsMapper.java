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

import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.services.grpc.mappers.MetadataMapper;
import com.cartobucket.auth.data.services.grpc.mappers.ScopeMapper;
import com.cartobucket.auth.rpc.ClientCreateResponse;
import com.cartobucket.auth.rpc.ClientResponse;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ClientsMapper {
    public static Client to(ClientResponse clientResponse) {
        var client = new Client();
        client.setId(UUID.fromString(clientResponse.getId()));
        client.setAuthorizationServerId(UUID.fromString(clientResponse.getAuthorizationServerId()));
        client.setName(clientResponse.getName());
        client.setScopes(clientResponse.getScopesList().stream().map(ScopeMapper::toScope).toList());
        client.setRedirectUris(clientResponse.getRedirectUrisList().stream().map(URI::create).toList());
        client.setMetadata(MetadataMapper.from(clientResponse.getMetadata()));
        client.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(clientResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        client.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(clientResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return client;
    }

    public static Client to(ClientCreateResponse clientCreateResponse) {
        var client = new Client();
        client.setId(UUID.fromString(clientCreateResponse.getId()));
        client.setAuthorizationServerId(UUID.fromString(clientCreateResponse.getAuthorizationServerId()));
        client.setName(clientCreateResponse.getName());
        client.setScopes(clientCreateResponse.getScopesList().stream().map(ScopeMapper::toScope).toList());
        client.setRedirectUris(clientCreateResponse.getRedirectUrisList().stream().map(URI::create).toList());
        client.setMetadata(MetadataMapper.from(clientCreateResponse.getMetadata()));
        client.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(clientCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        client.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(clientCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return client;
    }
}
