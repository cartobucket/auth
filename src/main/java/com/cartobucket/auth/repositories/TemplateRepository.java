package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Template;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TemplateRepository extends CrudRepository<Template, UUID> {
    List<Template> findAllByAuthorizationServerId(UUID authorizationServer);
}
