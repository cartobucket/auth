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

import com.cartobucket.auth.data.domain.Template

object TemplateMapper {
    fun from(template: com.cartobucket.auth.postgres.client.entities.Template): Template {
        val domainTemplate = Template()
        domainTemplate.template = template.template
        domainTemplate.id = template.id
        domainTemplate.templateType = template.templateType
        domainTemplate.authorizationServerId = template.authorizationServerId
        domainTemplate.metadata = template.metadata
        domainTemplate.updatedOn = template.updatedOn
        domainTemplate.createdOn = template.createdOn
        return domainTemplate
    }

    fun to(template: Template): com.cartobucket.auth.postgres.client.entities.Template {
        val entityTemplate =
            com.cartobucket.auth.postgres.client.entities
                .Template()
        entityTemplate.template = template.template
        entityTemplate.id = template.id
        entityTemplate.templateType = template.templateType
        entityTemplate.authorizationServerId = template.authorizationServerId
        entityTemplate.metadata = template.metadata
        entityTemplate.updatedOn = template.updatedOn
        entityTemplate.createdOn = template.createdOn
        return entityTemplate
    }
}
