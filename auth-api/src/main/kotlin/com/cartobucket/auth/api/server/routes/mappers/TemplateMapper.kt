// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.TemplateRequest
import com.cartobucket.auth.api.dto.TemplateResponse
import com.cartobucket.auth.data.domain.Template
import com.cartobucket.auth.data.domain.TemplateTypeEnum

object TemplateMapper {
    fun from(templateRequest: TemplateRequest): Template {
        val template = Template()
        template.authorizationServerId = templateRequest.authorizationServerId
        template.template = templateRequest.template?.toByteArray()
        template.metadata = MetadataMapper.from(templateRequest.metadata)
        template.templateType =
            when (templateRequest.templateType) {
                "login" -> TemplateTypeEnum.LOGIN
                else -> throw IllegalStateException("Unexpected value: ${templateRequest.templateType}")
            }
        return template
    }

    fun toResponse(template: Template): TemplateResponse =
        TemplateResponse(
            id = template.id.toString(),
            authorizationServerId = template.authorizationServerId,
            template = template.template?.let { String(it) },
            metadata = MetadataMapper.to(template.metadata),
            templateType = template.templateType?.toString(),
            createdOn = template.createdOn,
            updatedOn = template.updatedOn,
        )
}
