package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.exceptions.notfound.TemplateNotFound;
import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.Template;
import com.cartobucket.auth.models.mappers.TemplateMapper;
import com.cartobucket.auth.repositories.TemplateRepository;
import com.cartobucket.auth.services.TemplateService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.OffsetDateTime;
import java.util.UUID;

@ApplicationScoped
public class TemplateServiceImpl implements TemplateService {
    final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public TemplatesResponse getTemplates(TemplateRequestFilter filter) {
        var templatesResponse = new TemplatesResponse();
        templatesResponse.setTemplates(templateRepository.findAllByAuthorizationServerIdIn(filter.getAuthorizationServerIds()).stream().map(TemplateMapper::toResponse).toList());
        return templatesResponse;
    }

    @Override
    @Transactional
    public TemplateResponse createTemplate(TemplateRequest templateRequest) {
        var template = TemplateMapper.from(templateRequest);
        template.setCreatedOn(OffsetDateTime.now());
        template.setUpdatedOn(OffsetDateTime.now());
        template = templateRepository.save(template);
        return TemplateMapper.toResponse(template);
    }

    @Override
    @Transactional
    public void deleteTemplate(UUID templateId) {
        templateRepository.delete(
                templateRepository
                        .findById(templateId)
                        .orElseThrow(TemplateNotFound::new)
        );
    }

    @Override
    public TemplateResponse getTemplate(UUID templateId) {
        return TemplateMapper.toResponse(
                templateRepository
                        .findById(templateId)
                        .orElseThrow(TemplateNotFound::new)
        );
    }

    @Override
    public Template getTemplateForAuthorizationServer(UUID authorizationServer, TemplateTypeEnum templateType) {
        return templateRepository
                .findByAuthorizationServerIdAndTemplateType(authorizationServer, templateType)
                .orElseThrow(TemplateNotFound::new);
    }

    @Override
    @Transactional
    public TemplateResponse updateTemplate(UUID templateId, TemplateRequest templateRequest) {
        final var template = templateRepository
                .findById(templateId)
                .orElseThrow(NotFoundException::new);

        var _template = TemplateMapper.from(templateRequest);
        _template.setId(template.getId());
        _template.setAuthorizationServerId(template.getAuthorizationServerId());
        _template.setUpdatedOn(OffsetDateTime.now());
        _template = templateRepository.save(_template);

        return TemplateMapper.toResponse(_template);
    }
}
