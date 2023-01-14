package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.TemplateRequest;
import com.cartobucket.auth.model.generated.TemplateRequestFilter;
import com.cartobucket.auth.model.generated.TemplateResponse;
import com.cartobucket.auth.model.generated.TemplatesResponse;
import com.cartobucket.auth.models.Template;
import com.cartobucket.auth.models.mappers.TemplateMapper;
import com.cartobucket.auth.repositories.TemplateRepository;
import com.cartobucket.auth.services.TemplateService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class TemplateServiceImpl implements TemplateService {
    final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public TemplatesResponse getTemplates(TemplateRequestFilter filter) {
        var templates = StreamSupport.stream(templateRepository.findAll().spliterator(), false);
        // TODO: This should happen in the DB.
        if (!filter.getAuthorizationServerIds().isEmpty()) {
            templates = templates.filter(template -> filter.getAuthorizationServerIds().contains(template.getAuthorizationServerId()));
        }
        var templatesResponse = new TemplatesResponse();
        templatesResponse.setTemplates(templates.map(TemplateMapper::toResponse).toList());
        return templatesResponse;
    }

    @Override
    public TemplateResponse createTemplate(TemplateRequest templateRequest) {
        var template = TemplateMapper.from(templateRequest);
        template.setCreatedOn(OffsetDateTime.now());
        template.setUpdatedOn(OffsetDateTime.now());
        template = templateRepository.save(template);
        return TemplateMapper.toResponse(template);
    }

    @Override
    public void deleteTemplate(UUID templateId) {
        var template = templateRepository.findById(templateId);
        if (template.isEmpty()) {
            throw new NotFoundException();
        }
        templateRepository.delete(template.get());
    }

    @Override
    public TemplateResponse getTemplate(UUID templateId) {
        var template = templateRepository.findById(templateId);
        if (template.isEmpty()) {
            throw new NotFoundException();
        }
        return TemplateMapper.toResponse(template.get());
    }

    @Override
    public Template getTemplateForAuthorizationServer(UUID authorizationServer) {
        var templates = templateRepository.findAllByAuthorizationServerId(authorizationServer);
        return templates.stream().findFirst().orElseThrow();
    }

    @Override
    public TemplateResponse updateTemplate(UUID templateId, TemplateRequest templateRequest) {
        final var tempalte = templateRepository.findById(templateId);
        if (tempalte.isEmpty()) {
            throw new NotFoundException();
        }

        var _template = TemplateMapper.from(templateRequest);
        _template.setId(tempalte.get().getId());
        _template.setAuthorizationServerId(tempalte.get().getAuthorizationServerId());
        _template.setUpdatedOn(OffsetDateTime.now());
        _template = templateRepository.save(_template);

        return TemplateMapper.toResponse(_template);
    }
}
