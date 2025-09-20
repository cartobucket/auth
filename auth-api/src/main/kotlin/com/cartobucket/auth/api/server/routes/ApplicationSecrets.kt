// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.ApplicationSecretRequest
import com.cartobucket.auth.api.dto.ApplicationSecretsResponse
import com.cartobucket.auth.api.interfaces.ApplicationSecretsApi
import com.cartobucket.auth.api.server.routes.mappers.ApplicationMapper
import com.cartobucket.auth.data.services.ApplicationService
import jakarta.ws.rs.core.Response
import java.util.UUID

class ApplicationSecrets(
    private val applicationService: ApplicationService,
) : ApplicationSecretsApi {
    override fun createApplicationSecret(applicationSecretRequest: ApplicationSecretRequest): Response {
        val application = applicationService.getApplication(applicationSecretRequest.applicationId)
        return Response
            .ok()
            .entity(
                ApplicationMapper.toSecretResponse(
                    applicationService.createApplicationSecret(
                        ApplicationMapper.secretFrom(application.left!!, applicationSecretRequest),
                    ),
                ),
            ).build()
    }

    override fun deleteApplicationSecret(secretId: UUID): Response {
        applicationService.deleteApplicationSecret(secretId)
        return Response.ok().build()
    }

    override fun listApplicationSecrets(applicationIds: List<UUID>?): Response {
        // TODO: This should be able to be blank, this should also take a list of authorization server ids
        val secrets =
            applicationService
                .getApplicationSecrets(applicationIds ?: emptyList())
                .map { ApplicationMapper.toSecretResponse(it) }

        // Using empty page since the current API doesn't support pagination for secrets
        val page =
            com.cartobucket.auth.api.dto
                .Page()
        val applicationSecretsResponse =
            ApplicationSecretsResponse(
                applicationSecrets = secrets,
                page = page,
            )

        return Response
            .ok()
            .entity(applicationSecretsResponse)
            .build()
    }
}
