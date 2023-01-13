package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Template;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TemplateRepository extends CrudRepository<Template, UUID> {
}
