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

package com.cartobucket.auth.postgres.client.services

import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Template
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound
import com.cartobucket.auth.postgres.client.repositories.EventRepository
import com.cartobucket.auth.postgres.client.repositories.TemplateRepository
import com.cartobucket.auth.postgres.client.entities.EventType
import com.cartobucket.auth.postgres.client.entities.mappers.TemplateMapper
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class TemplateService(
    private val eventRepository: EventRepository,
    private val templateRepository: TemplateRepository
) : com.cartobucket.auth.data.services.TemplateService {

    @Transactional
    override fun getTemplates(authorizationServerIds: List<UUID>, page: Page): List<Template> {
        val allEntities: List<com.cartobucket.auth.postgres.client.entities.Template> = if (authorizationServerIds.isNotEmpty()) {
            templateRepository.list("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
        } else {
            templateRepository.listAll(Sort.descending("createdOn"))
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { TemplateMapper.from(it) }
    }

    @Transactional
    override fun createTemplate(template: Template): Template {
        template.createdOn = OffsetDateTime.now()
        template.updatedOn = OffsetDateTime.now()
        // TODO: This does not catch the authorizationServerId & templateType constraint.
        val _template = TemplateMapper.to(template)
        templateRepository.persist(_template)
        eventRepository.createTemplateEvent(TemplateMapper.from(_template), EventType.CREATE)
        return TemplateMapper.from(_template)
    }

    @Transactional
    @Throws(TemplateNotFound::class)
    override fun deleteTemplate(templateId: UUID) {
        val template = templateRepository
            .findByIdOptional(templateId)
            .orElseThrow { TemplateNotFound() }
        templateRepository.delete(template)
        eventRepository.createTemplateEvent(TemplateMapper.from(template), EventType.DELETE)
    }

    @Transactional
    @Throws(TemplateNotFound::class)
    override fun getTemplate(templateId: UUID): Template {
        return templateRepository
            .findByIdOptional(templateId)
            .map { TemplateMapper.from(it) }
            .orElseThrow { TemplateNotFound() }
    }

    @Transactional
    @Throws(TemplateNotFound::class)
    override fun updateTemplate(templateId: UUID, template: Template): Template {
        val _template = templateRepository
            .findByIdOptional(templateId)
            .orElseThrow { TemplateNotFound() }

        _template.updatedOn = OffsetDateTime.now()
        _template.template = template.template
        templateRepository.persist(_template)
        eventRepository.createTemplateEvent(TemplateMapper.from(_template), EventType.UPDATE)

        return TemplateMapper.from(_template)
    }
}
