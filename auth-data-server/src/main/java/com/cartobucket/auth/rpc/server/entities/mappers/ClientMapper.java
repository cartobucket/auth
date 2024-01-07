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

package com.cartobucket.auth.rpc.server.entities.mappers;

import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.domain.Scope;

public class ClientMapper {
    public static Client from(com.cartobucket.auth.rpc.server.entities.Client client) {
        var _client = new Client();
        _client.setName(client.getName());
        _client.setScopes(client.getScopes().stream().map(ScopeMapper::from).toList());
        _client.setId(client.getId());
        _client.setRedirectUris(client.getRedirectUris());
        _client.setAuthorizationServerId(client.getAuthorizationServerId());
        _client.setMetadata(client.getMetadata());
        _client.setUpdatedOn(client.getUpdatedOn());
        _client.setCreatedOn(client.getCreatedOn());
        return _client;
    }

    public static com.cartobucket.auth.rpc.server.entities.Client to(Client client) {
        var _client = new com.cartobucket.auth.rpc.server.entities.Client();
        _client.setName(client.getName());
        _client.setScopes(client.getScopes().stream().map(ScopeMapper::to).toList());
        _client.setId(client.getId());
        // TODO: This should come from the client, this is just an oversight, please fix.
        _client.setClientId(String.valueOf(client.getId()));
        _client.setRedirectUris(client.getRedirectUris());
        _client.setAuthorizationServerId(client.getAuthorizationServerId());
        _client.setMetadata(client.getMetadata());
        _client.setUpdatedOn(client.getUpdatedOn());
        _client.setCreatedOn(client.getCreatedOn());
        return _client;
    }

}
