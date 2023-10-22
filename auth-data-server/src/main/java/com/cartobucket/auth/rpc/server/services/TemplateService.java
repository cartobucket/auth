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

package com.cartobucket.auth.rpc.server.services;

import com.cartobucket.auth.rpc.server.entities.mappers.TemplateMapper;
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound;
import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.rpc.server.repositories.TemplateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class TemplateService implements com.cartobucket.auth.data.services.TemplateService {
    final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public List<Template> getTemplates(final List<UUID> authorizationServerIds) {
        if (!authorizationServerIds.isEmpty()) {
            return templateRepository
                    .findAllByAuthorizationServerIdIn(authorizationServerIds)
                    .stream()
                    .map(TemplateMapper::from)
                    .toList();
        } else {
            return templateRepository.findAll()
                    .stream()
                    .map(TemplateMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public Template createTemplate(final Template template) {
        template.setCreatedOn(OffsetDateTime.now());
        template.setUpdatedOn(OffsetDateTime.now());
        // TODO: This does not catch the authorizationServerId & templateType constraint.
        var _template = TemplateMapper.to(template);
        templateRepository.persist(_template);
        return TemplateMapper.from(_template);
    }

    @Override
    @Transactional
    public void deleteTemplate(final UUID templateId) throws TemplateNotFound {
        templateRepository.delete(
                templateRepository
                        .findByIdOptional(templateId)
                        .orElseThrow(TemplateNotFound::new)
        );
    }

    @Override
    public Template getTemplate(final UUID templateId) throws TemplateNotFound {
        return templateRepository
                .findByIdOptional(templateId)
                .map(TemplateMapper::from)
                .orElseThrow(TemplateNotFound::new);
    }

    @Override
    @Transactional
    public Template updateTemplate(final UUID templateId, final Template template) throws TemplateNotFound {
        var _template = templateRepository
                .findByIdOptional(templateId)
                .orElseThrow(TemplateNotFound::new);

        _template.setUpdatedOn(OffsetDateTime.now());
        _template.setTemplate(template.getTemplate());
        templateRepository.persist(_template);

        return TemplateMapper.from(_template);
    }
}
