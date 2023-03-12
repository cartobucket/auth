package com.cartobucket.auth.repositories;

import com.cartobucket.auth.model.generated.TemplateTypeEnum;
import com.cartobucket.auth.models.Template;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends CrudRepository<Template, UUID> {
    List<Template> findAllByAuthorizationServerId(UUID authorizationServer);

    List<Template> findAllByAuthorizationServerIdAndTemplateType(UUID authorizationServer, TemplateTypeEnum templateType);

    Optional<Template> findByAuthorizationServerIdAndTemplateType(UUID authorizationServer, TemplateTypeEnum templateType);

    List<Template> findAllByAuthorizationServerIdIn(List<UUID> authorizationServerIds);
}
