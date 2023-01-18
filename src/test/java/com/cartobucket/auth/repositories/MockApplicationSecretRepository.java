package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.ApplicationSecret;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockApplicationSecretRepository implements ApplicationSecretRepository {
    final HashMap<UUID, List<ApplicationSecret>> applicationSecrets = new HashMap<>();
    final HashMap<String, ApplicationSecret> secretsHashes = new HashMap<>();

    @Override
    public List<ApplicationSecret> findByApplicationId(UUID applicationId) {
        return applicationSecrets.get(applicationId);
    }

    @Override
    public Optional<ApplicationSecret> findByApplicationIdAndId(UUID applicationId, UUID id) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<ApplicationSecret> findByApplicationSecretHash(String secretHash) {
        return Optional.ofNullable(secretsHashes.get(secretHash));
    }

    @Override
    public <S extends ApplicationSecret> S save(S entity) {
        List<ApplicationSecret> secrets = null;
        if (applicationSecrets.containsKey(entity.getApplicationId())) {
            secrets = applicationSecrets.get(entity.getApplicationId());
        } else {
            secrets = new ArrayList<>();
            applicationSecrets.put(entity.getApplicationId(), secrets);
        }
        entity.setId(UUID.randomUUID());
        secrets.add(entity);
        secretsHashes.put(entity.getApplicationSecretHash(), entity);
        return entity;
    }

    @Override
    public <S extends ApplicationSecret> Iterable<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<ApplicationSecret> findById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<ApplicationSecret> findAll() {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<ApplicationSecret> findAllById(Iterable<UUID> uuids) {
        throw new NotImplementedException();
    }

    @Override
    public long count() {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(ApplicationSecret entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends ApplicationSecret> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}
