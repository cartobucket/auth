// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.ApplicationRequest
import com.cartobucket.auth.api.dto.ApplicationResponse
import com.cartobucket.auth.api.dto.ApplicationSecretRequest
import com.cartobucket.auth.api.dto.ApplicationSecretResponse
import com.cartobucket.auth.data.domain.Application
import com.cartobucket.auth.data.domain.ApplicationSecret
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.Scope

object ApplicationMapper {
    fun from(applicationRequest: ApplicationRequest): Application {
        val application = Application()
        application.name = applicationRequest.name
        application.authorizationServerId = applicationRequest.authorizationServerId
        applicationRequest.clientId?.let { application.clientId = it }
        application.scopes =
            applicationRequest.scopes?.map { scopeId ->
                val scope = Scope()
                scope.id = scopeId
                scope
            }
        application.metadata = MetadataMapper.from(applicationRequest.metadata)
        return application
    }

    fun toResponse(application: Application): ApplicationResponse =
        ApplicationResponse(
            id = application.id?.toString(),
            name = application.name,
            clientId = application.clientId,
            authorizationServerId = application.authorizationServerId!!, // Required field
            metadata = MetadataMapper.to(application.metadata),
            scopes = application.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList(),
            createdOn = application.createdOn,
            updatedOn = application.updatedOn,
        )

    fun toResponseWithProfile(
        application: Application,
        profile: Profile,
    ): ApplicationResponse =
        ApplicationResponse(
            id = application.id?.toString(),
            name = application.name,
            clientId = application.clientId,
            authorizationServerId = application.authorizationServerId!!, // Required field
            scopes = application.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList(),
            metadata = MetadataMapper.to(application.metadata),
            createdOn = application.createdOn,
            updatedOn = application.updatedOn,
            profile = profile.profile,
        )

    fun secretFrom(
        application: Application,
        applicationSecretRequest: ApplicationSecretRequest,
    ): ApplicationSecret {
        val applicationSecret = ApplicationSecret()
        applicationSecret.applicationId = application.id
        applicationSecret.name = applicationSecretRequest.name
        applicationSecret.authorizationServerId = application.authorizationServerId
        applicationSecret.scopes = applicationSecretRequest.scopes.map { Scope(it) }
        applicationSecret.expiresIn = applicationSecretRequest.expiresIn
        return applicationSecret
    }

    fun toSecretResponse(applicationSecret: ApplicationSecret): ApplicationSecretResponse =
        ApplicationSecretResponse(
            clientSecret = applicationSecret.applicationSecret,
            id = applicationSecret.id,
            name = applicationSecret.name,
            applicationId = applicationSecret.applicationId,
            authorizationServerId = applicationSecret.authorizationServerId,
            scopes = applicationSecret.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList(),
            createdOn = applicationSecret.createdOn!!, // Required field
            expiresIn = applicationSecret.expiresIn,
        )
}
