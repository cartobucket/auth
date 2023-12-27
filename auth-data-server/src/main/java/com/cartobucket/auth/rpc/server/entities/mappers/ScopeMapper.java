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

import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.rpc.ScopeResponse;
import com.cartobucket.auth.rpc.server.rpc.mappers.MetadataMapper;

import java.util.UUID;

public class ScopeMapper {
    public static Scope from(com.cartobucket.auth.rpc.server.entities.Scope scope) {
        var _scope = new Scope();
        _scope.setId(scope.getId());
        _scope.setCreatedOn(scope.getCreatedOn());
        _scope.setUpdatedOn(scope.getUpdatedOn());
        _scope.setName(scope.getName());
        _scope.setAuthorizationServerId(scope.getAuthorizationServerId());
        _scope.setMetadata(scope.getMetadata());
        return _scope;
    }

    public static com.cartobucket.auth.rpc.server.entities.Scope to(Scope scope) {
        var _scope = new com.cartobucket.auth.rpc.server.entities.Scope();
        _scope.setId(scope.getId());
        _scope.setCreatedOn(scope.getCreatedOn());
        _scope.setUpdatedOn(scope.getUpdatedOn());
        _scope.setName(scope.getName());
        _scope.setAuthorizationServerId(scope.getAuthorizationServerId());
        _scope.setMetadata(scope.getMetadata());
        return _scope;
    }

    public static Scope toScope(String scopeString) {
        var scope = new Scope();
        scope.setName(scopeString);
        return scope;
    }

    public static Scope fromResponse(ScopeResponse scope) {
        var _scope = new Scope();
        _scope.setId(UUID.fromString(scope.getId()));
//        _scope.setCreatedOn(scope.getCreatedOn());
//        _scope.setUpdatedOn(scope.getUpdatedOn());
        _scope.setName(scope.getName());
        _scope.setAuthorizationServerId(UUID.fromString(scope.getAuthorizationServerId()));
        _scope.setMetadata(MetadataMapper.toMetadata(scope.getMetadata()));
        return _scope;
    }

    public static ScopeResponse toResponse(Scope scope) {
        var _scope = ScopeResponse
                .newBuilder()
                .setId(String.valueOf(scope.getId()))
//                .setCreatedOn(scope.getCreatedOn())
//                .setUpdatedOn(scope.getUpdatedOn())
                .setName(scope.getName())
                .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServerId()))
                .setMetadata(MetadataMapper.from(scope.getMetadata()))
                .build();
        return _scope;
    }
}
