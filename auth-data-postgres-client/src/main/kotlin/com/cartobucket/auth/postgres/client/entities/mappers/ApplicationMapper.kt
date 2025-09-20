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

package com.cartobucket.auth.postgres.client.entities.mappers

import com.cartobucket.auth.data.domain.Application

object ApplicationMapper {
    fun from(application: com.cartobucket.auth.postgres.client.entities.Application): Application {
        val domainApplication = Application()
        domainApplication.id = application.id
        domainApplication.authorizationServerId = application.authorizationServerId
        domainApplication.name = application.name
        domainApplication.clientId = application.clientId
        domainApplication.metadata = application.metadata
        domainApplication.scopes = application.scopes?.map { ScopeMapper.fromNoAuthorizationServer(it) }
        domainApplication.updatedOn = application.updatedOn
        domainApplication.createdOn = application.createdOn
        return domainApplication
    }

    fun to(application: Application): com.cartobucket.auth.postgres.client.entities.Application {
        val entityApplication =
            com.cartobucket.auth.postgres.client.entities
                .Application()
        entityApplication.id = application.id
        entityApplication.authorizationServerId = application.authorizationServerId
        entityApplication.name = application.name
        entityApplication.clientId = application.clientId
        entityApplication.updatedOn = application.updatedOn
        entityApplication.createdOn = application.createdOn
        entityApplication.metadata = application.metadata
        return entityApplication
    }
}
