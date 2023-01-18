package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.SigningKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SingingKeyRepository extends CrudRepository<SigningKey, UUID> {
    SigningKey findByAuthorizationServerId(UUID authorizationServerId);
    List<SigningKey> findAllByAuthorizationServerId(UUID authorizationServerId);

    SigningKey findByIdAndAuthorizationServerId(UUID kid, UUID id);
}
