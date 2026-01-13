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

package com.revethq.auth.persistence.entities.mappers

import com.revethq.auth.core.domain.Template
import com.revethq.auth.persistence.entities.Template as PostgresTemplate

object TemplateMapper {
    @JvmStatic
    fun from(template: PostgresTemplate): Template {
        return Template(
            template = template.template,
            id = template.id,
            templateType = template.templateType,
            authorizationServerId = template.authorizationServerId,
            metadata = template.metadata,
            updatedOn = template.updatedOn,
            createdOn = template.createdOn
        )
    }

    @JvmStatic
    fun to(template: Template): PostgresTemplate {
        return PostgresTemplate().apply {
            this.template = template.template
            id = template.id
            templateType = template.templateType
            authorizationServerId = template.authorizationServerId
            metadata = template.metadata
            updatedOn = template.updatedOn
            createdOn = template.createdOn
        }
    }
}
