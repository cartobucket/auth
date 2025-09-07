package com.cartobucket.auth.postgres.client.repositories;

import com.cartobucket.auth.postgres.client.entities.ApplicationSecret;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ApplicationSecretRepository implements PanacheRepositoryBase<ApplicationSecret, UUID> {
    public List<ApplicationSecret> findByApplicationIdIn(List<UUID> applicationId) {
        return list("applicationId in ?1", applicationId);
    }

    public Optional<ApplicationSecret> findByApplicationSecretHash(String secretHash) {
        return find("applicationSecretHash = ?1", secretHash).singleResultOptional();
    }
}
