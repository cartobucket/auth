// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.TemplateRequest
import com.cartobucket.auth.api.dto.TemplatesResponse
import com.cartobucket.auth.api.interfaces.TemplatesApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.TemplateMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.services.TemplateService
import jakarta.ws.rs.core.Response
import java.util.UUID

class Templates(
    private val templateService: TemplateService,
) : TemplatesApi {
    override fun createTemplate(templateRequest: TemplateRequest): Response =
        Response
            .ok()
            .entity(
                TemplateMapper.toResponse(
                    templateService.createTemplate(TemplateMapper.from(templateRequest)),
                ),
            ).build()

    override fun deleteTemplate(templateId: UUID): Response {
        templateService.deleteTemplate(templateId)
        return Response.noContent().build()
    }

    override fun getTemplate(templateId: UUID): Response =
        Response
            .ok()
            .entity(
                TemplateMapper.toResponse(templateService.getTemplate(templateId)),
            ).build()

    override fun listTemplates(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val actualAuthorizationServerIds = authorizationServerIds ?: emptyList()

        val templates =
            templateService
                .getTemplates(actualAuthorizationServerIds, Page(actualLimit, actualOffset))
                .map { TemplateMapper.toResponse(it) }
        val page = getPage("templates", actualAuthorizationServerIds, actualLimit, actualOffset)
        val templatesResponse =
            TemplatesResponse(
                templates = templates,
                page = page,
            )
        return Response
            .ok()
            .entity(templatesResponse)
            .build()
    }

    override fun updateTemplate(
        templateId: UUID,
        templateRequest: TemplateRequest,
    ): Response =
        Response
            .ok()
            .entity(
                TemplateMapper.toResponse(
                    templateService.updateTemplate(templateId, TemplateMapper.from(templateRequest)),
                ),
            ).build()
}
