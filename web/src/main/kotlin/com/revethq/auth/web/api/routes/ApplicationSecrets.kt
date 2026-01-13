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
package com.revethq.auth.web.api.routes

import com.revethq.auth.core.api.dto.ApplicationSecretRequest
import com.revethq.auth.core.api.dto.ApplicationSecretsResponse
import com.revethq.auth.core.api.interfaces.ApplicationSecretsApi
import com.revethq.auth.web.api.routes.mappers.ApplicationMapper
import com.revethq.auth.core.services.ApplicationService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class ApplicationSecrets @Inject constructor(
    private val applicationService: ApplicationService
) : ApplicationSecretsApi {

    override fun createApplicationSecret(applicationSecretRequest: ApplicationSecretRequest): Response {
        val applicationId = applicationSecretRequest.applicationId
            ?: throw IllegalArgumentException("applicationId is required")
        val application = applicationService.getApplication(applicationId)
        val app = application.left ?: throw IllegalArgumentException("Application not found")
        return Response
            .ok()
            .entity(
                ApplicationMapper.toSecretResponse(
                    applicationService.createApplicationSecret(
                        ApplicationMapper.secretFrom(app, applicationSecretRequest)
                    )
                )
            )
            .build()
    }

    override fun deleteApplicationSecret(secretId: UUID): Response {
        applicationService.deleteApplicationSecret(secretId)
        return Response.ok().build()
    }

    override fun listApplicationSecrets(applicationIds: List<UUID>?): Response {
        val applicationSecretsResponse = ApplicationSecretsResponse()
        // TODO: This should be able to be blank, this should also take a list of authorization server ids
        applicationSecretsResponse.applicationSecrets = applicationService
            .getApplicationSecrets(applicationIds ?: emptyList())
            .map { ApplicationMapper.toSecretResponse(it) }

        return Response.ok().entity(applicationSecretsResponse).build()
    }
}
