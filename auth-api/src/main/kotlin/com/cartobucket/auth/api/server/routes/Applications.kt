// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.ApplicationRequest
import com.cartobucket.auth.api.dto.ApplicationsResponse
import com.cartobucket.auth.api.interfaces.ApplicationsApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.ApplicationMapper
import com.cartobucket.auth.api.server.routes.mappers.ProfileMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.services.ApplicationService
import com.cartobucket.auth.data.services.AuthorizationServerService
import jakarta.ws.rs.core.Response
import java.util.UUID

class Applications(
    private val applicationService: ApplicationService,
    private val authorizationServerService: AuthorizationServerService,
) : ApplicationsApi {
    override fun createApplication(applicationRequest: ApplicationRequest): Response {
        val application =
            applicationService.createApplication(
                ApplicationMapper.from(applicationRequest),
                ProfileMapper.toProfile(applicationRequest.profile as? Map<String, Any?> ?: emptyMap()),
            )
        return Response
            .ok()
            .entity(
                ApplicationMapper.toResponseWithProfile(
                    application.left!!,
                    application.right!!,
                ),
            ).build()
    }

    override fun deleteApplication(applicationId: UUID): Response {
        applicationService.deleteApplication(applicationId)
        return Response.ok().build()
    }

    override fun getApplication(applicationId: UUID): Response {
        val application = applicationService.getApplication(applicationId)
        return Response
            .ok()
            .entity(
                ApplicationMapper.toResponseWithProfile(
                    application.left!!,
                    application.right!!,
                ),
            ).build()
    }

    override fun listApplications(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val actualAuthorizationServerIds = authorizationServerIds ?: emptyList()

        val applications =
            applicationService
                .getApplications(actualAuthorizationServerIds, Page(actualLimit, actualOffset))
                .map { ApplicationMapper.toResponse(it) }
        val page = getPage("applications", actualAuthorizationServerIds, actualLimit, actualOffset)
        val applicationsResponse =
            ApplicationsResponse(
                applications = applications,
                page = page,
            )
        return Response.ok().entity(applicationsResponse).build()
    }
}
