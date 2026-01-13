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

import com.revethq.auth.core.api.dto.ApplicationRequest
import com.revethq.auth.core.api.dto.ApplicationsResponse
import com.revethq.auth.core.api.interfaces.ApplicationsApi
import com.revethq.auth.web.api.routes.Constants.LIMIT_DEFAULT
import com.revethq.auth.web.api.routes.Constants.OFFSET_DEFAULT
import com.revethq.auth.web.api.routes.Pagination.getPage
import com.revethq.auth.web.api.routes.mappers.ApplicationMapper
import com.revethq.auth.web.api.routes.mappers.ProfileMapper
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.services.ApplicationService
import com.revethq.auth.core.services.AuthorizationServerService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class Applications @Inject constructor(
    private val applicationService: ApplicationService,
    private val authorizationServerService: AuthorizationServerService
) : ApplicationsApi {

    override fun createApplication(applicationRequest: ApplicationRequest): Response {
        @Suppress("UNCHECKED_CAST")
        val profileMap = applicationRequest.profile as? Map<String, Any> ?: emptyMap()
        val application = applicationService.createApplication(
            ApplicationMapper.from(applicationRequest),
            ProfileMapper.toProfile(profileMap)
        )
        val app = application.left ?: throw IllegalArgumentException("Application not found")
        val profile = application.right ?: throw IllegalArgumentException("Profile not found")
        return Response
            .ok()
            .entity(
                ApplicationMapper.toResponseWithProfile(app, profile)
            )
            .build()
    }

    override fun deleteApplication(applicationId: UUID): Response {
        applicationService.deleteApplication(applicationId)
        return Response.ok().build()
    }

    override fun getApplication(applicationId: UUID): Response {
        val application = applicationService.getApplication(applicationId)
        val app = application.left ?: throw IllegalArgumentException("Application not found")
        val profile = application.right ?: throw IllegalArgumentException("Profile not found")
        return Response
            .ok()
            .entity(
                ApplicationMapper.toResponseWithProfile(app, profile)
            )
            .build()
    }

    override fun listApplications(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val serverIds = authorizationServerIds ?: emptyList()

        val applicationsResponse = ApplicationsResponse()
        applicationsResponse.applications = applicationService
            .getApplications(serverIds, Page(actualLimit, actualOffset))
            .map { ApplicationMapper.toResponse(it) }
        applicationsResponse.page = getPage("applications", serverIds, actualLimit, actualOffset)
        return Response.ok().entity(applicationsResponse).build()
    }
}
