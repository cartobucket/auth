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

import com.cartobucket.auth.data.domain.ApplicationSecret;


public class ApplicationSecretMapper {
    public static ApplicationSecret from(com.cartobucket.auth.rpc.server.entities.ApplicationSecret applicationSecret) {
        return new ApplicationSecret.Builder()
                .setApplicationId(applicationSecret.getApplicationId())
                .setApplicationSecret(applicationSecret.getApplicationSecret())
                .setApplicationSecretHash(applicationSecret.getApplicationSecretHash())
                .setId(applicationSecret.getId())
                .setScopes(applicationSecret.getScopes().stream().map(ScopeMapper::fromNoAuthorizationServer).toList())
                .setAuthorizationServerId(applicationSecret.getAuthorizationServerId())
                .setName(applicationSecret.getName())
                .setExpiresIn(applicationSecret.getExpiresIn())
                .setCreatedOn(applicationSecret.getCreatedOn())
                .build();
    }

    public static com.cartobucket.auth.rpc.server.entities.ApplicationSecret to(ApplicationSecret applicationSecret) {
        var _applicationSecret = new com.cartobucket.auth.rpc.server.entities.ApplicationSecret();
        _applicationSecret.setApplicationId(applicationSecret.getApplicationId());
        _applicationSecret.setApplicationSecret(applicationSecret.getApplicationSecret());
        _applicationSecret.setApplicationSecretHash(applicationSecret.getApplicationSecretHash());
        _applicationSecret.setId(applicationSecret.getId());
        _applicationSecret.setAuthorizationServerId(applicationSecret.getAuthorizationServerId());
        _applicationSecret.setName(applicationSecret.getName());
        _applicationSecret.setExpiresIn(applicationSecret.getExpiresIn());
        _applicationSecret.setCreatedOn(applicationSecret.getCreatedOn());
        return _applicationSecret;
    }

}
