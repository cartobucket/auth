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

package com.cartobucket.auth.postgres.client.entities.mappers;

import com.cartobucket.auth.data.domain.Application;

public class ApplicationMapper {
    public static Application from(com.cartobucket.auth.postgres.client.entities.Application application) {
        var _application = new Application();
        _application.setId(application.getId());
        _application.setAuthorizationServerId(application.getAuthorizationServerId());
        _application.setName(application.getName());
        _application.setClientId(application.getClientId());
        _application.setMetadata(application.getMetadata());
        _application.setScopes(application.getScopes().stream().map(ScopeMapper::fromNoAuthorizationServer).toList());
        _application.setUpdatedOn(application.getUpdatedOn());
        _application.setCreatedOn(application.getCreatedOn());
        return _application;
    }

    public static com.cartobucket.auth.postgres.client.entities.Application to(Application application) {
        var _application = new com.cartobucket.auth.postgres.client.entities.Application();
        _application.setId(application.getId());
        _application.setAuthorizationServerId(application.getAuthorizationServerId());
        _application.setName(application.getName());
        _application.setClientId(application.getClientId());
        _application.setUpdatedOn(application.getUpdatedOn());
        _application.setCreatedOn(application.getCreatedOn());
        _application.setMetadata(application.getMetadata());
        return _application;
    }

}
