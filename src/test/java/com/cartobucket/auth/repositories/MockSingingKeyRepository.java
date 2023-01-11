package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.SigningKey;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.impls.AuthorizationServerServiceImpl;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockSingingKeyRepository implements SingingKeyRepository {
    final AuthorizationServerService authorizationServerService;

    public MockSingingKeyRepository(AuthorizationServerService authorizationServerService) {
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public SigningKey findByAuthorizationServerId(UUID authorizationServerId) {
        throw new NotImplementedException();
    }

    @Override
    public List<SigningKey> findAllByAuthorizationServerId(UUID authorizationServerId) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        final var singingKey = AuthorizationServerServiceImpl.generateSigningKey(authorizationServer);
        singingKey.setId(UUID.randomUUID());
        return List.of(singingKey);
    }

    @Override
    public <S extends SigningKey> S save(S entity) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends SigningKey> Iterable<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<SigningKey> findById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<SigningKey> findAll() {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<SigningKey> findAllById(Iterable<UUID> uuids) {
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
    public void delete(SigningKey entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends SigningKey> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}
