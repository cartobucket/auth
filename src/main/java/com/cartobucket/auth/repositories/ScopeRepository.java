package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Scope;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ScopeRepository extends CrudRepository<Scope, UUID> {
    Scope findByAuthorizationServerIdAndName(UUID authorizationServerId, String name);
    List<Scope> findAllByAuthorizationServerId(UUID authorizationServerId);
}
