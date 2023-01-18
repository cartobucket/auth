package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Application;
import com.cartobucket.auth.models.Scope;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockScopeRepository implements ScopeRepository {

    @Override
    public Scope findByAuthorizationServerIdAndName(UUID authorizationServerId, String name) {
        return null;
    }

    @Override
    public List<Scope> findAllByAuthorizationServerId(UUID authorizationServerId) {
        var testReadScope = new Scope();
        testReadScope.setName("test.read");

        var testWriteScope = new Scope();
        testWriteScope.setName("test.write");
        return List.of(testReadScope, testWriteScope);
    }

    @Override
    public <S extends Scope> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Scope> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Scope> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<Scope> findAll() {
        return null;
    }

    @Override
    public Iterable<Scope> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void delete(Scope entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Scope> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
