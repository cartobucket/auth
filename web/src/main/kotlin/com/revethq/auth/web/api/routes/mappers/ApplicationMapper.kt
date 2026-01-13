/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.web.api.routes.mappers

import com.revethq.auth.core.domain.Application
import com.revethq.auth.core.domain.ApplicationSecret
import com.revethq.auth.core.domain.Profile
import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.api.dto.ApplicationRequest
import com.revethq.auth.core.api.dto.ApplicationResponse
import com.revethq.auth.core.api.dto.ApplicationSecretRequest
import com.revethq.auth.core.api.dto.ApplicationSecretResponse

object ApplicationMapper {

    @JvmStatic
    fun from(applicationRequest: ApplicationRequest): Application {
        val application = Application()
        application.name = applicationRequest.name
        application.authorizationServerId = applicationRequest.authorizationServerId
        if (applicationRequest.clientId != null) {
            application.clientId = applicationRequest.clientId
        }
        application.scopes = applicationRequest.scopes?.map { scopeId ->
            Scope().apply { id = scopeId }
        } ?: emptyList()
        application.metadata = MetadataMapper.from(applicationRequest.metadata)
        return application
    }

    @JvmStatic
    fun toResponse(application: Application): ApplicationResponse {
        return ApplicationResponse().apply {
            id = application.id.toString()
            name = application.name
            clientId = application.clientId
            authorizationServerId = application.authorizationServerId
            metadata = MetadataMapper.to(application.metadata)
            scopes = application.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList()
            createdOn = application.createdOn
            updatedOn = application.updatedOn
        }
    }

    @JvmStatic
    fun toResponseWithProfile(application: Application, profile: Profile): ApplicationResponse {
        return ApplicationResponse().apply {
            id = application.id.toString()
            name = application.name
            clientId = application.clientId
            authorizationServerId = application.authorizationServerId
            scopes = application.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList()
            metadata = MetadataMapper.to(application.metadata)
            createdOn = application.createdOn
            updatedOn = application.updatedOn
            this.profile = profile.profile
        }
    }

    @JvmStatic
    fun secretFrom(application: Application, applicationSecretRequest: ApplicationSecretRequest): ApplicationSecret {
        return ApplicationSecret().apply {
            applicationId = application.id
            name = applicationSecretRequest.name
            authorizationServerId = application.authorizationServerId
            scopes = applicationSecretRequest.scopes?.map { Scope(it) } ?: emptyList()
            expiresIn = applicationSecretRequest.expiresIn
        }
    }

    @JvmStatic
    fun toSecretResponse(applicationSecret: ApplicationSecret): ApplicationSecretResponse {
        return ApplicationSecretResponse().apply {
            clientSecret = applicationSecret.applicationSecret
            id = applicationSecret.id
            name = applicationSecret.name
            applicationId = applicationSecret.applicationId
            authorizationServerId = applicationSecret.authorizationServerId
            scopes = applicationSecret.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList()
            createdOn = applicationSecret.createdOn
            expiresIn = applicationSecret.expiresIn
        }
    }
}
