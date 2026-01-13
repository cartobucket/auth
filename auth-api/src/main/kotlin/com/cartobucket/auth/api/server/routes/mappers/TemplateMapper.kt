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

package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.TemplateRequest
import com.cartobucket.auth.api.dto.TemplateResponse
import com.cartobucket.auth.data.domain.Template
import com.cartobucket.auth.data.domain.TemplateTypeEnum

object TemplateMapper {

    @JvmStatic
    fun from(templateRequest: TemplateRequest): Template {
        val template = Template()
        template.authorizationServerId = templateRequest.authorizationServerId
        template.template = templateRequest.template?.toByteArray()
        template.metadata = MetadataMapper.from(templateRequest.metadata)
        template.templateType = when (templateRequest.templateType) {
            "login" -> TemplateTypeEnum.LOGIN
            else -> throw IllegalStateException("Unexpected value: ${templateRequest.templateType}")
        }
        return template
    }

    @JvmStatic
    fun toResponse(template: Template): TemplateResponse {
        return TemplateResponse().apply {
            id = template.id.toString()
            authorizationServerId = template.authorizationServerId
            this.template = template.template?.let { String(it) }
            metadata = MetadataMapper.to(template.metadata)
            templateType = template.templateType.toString()
            createdOn = template.createdOn
            updatedOn = template.updatedOn
        }
    }
}
