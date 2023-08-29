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

package com.cartobucket.auth.data.services.impls.mappers;

import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.rpc.ScopeResponse;
import com.cartobucket.auth.data.rpc.ScopeResponse;
import io.smallrye.mutiny.Uni;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ScopeMapper {
    public static Scope toScope(ScopeResponse scopeResponse) {
        var scope = new Scope();
        scope.setId(UUID.fromString(scopeResponse.getId()));
        scope.setName(scopeResponse.getName());
        scope.setAuthorizationServerId(UUID.fromString(scopeResponse.getAuthorizationServerId()));
        scope.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(scopeResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        scope.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(scopeResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return scope;
    }
}
