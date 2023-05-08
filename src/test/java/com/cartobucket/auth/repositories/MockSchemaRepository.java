package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Schema;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockSchemaRepository implements SchemaRepository{
    @Override
    public <S extends Schema> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Schema> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Schema> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<Schema> findAll() {
        return null;
    }

    @Override
    public Iterable<Schema> findAllById(Iterable<UUID> uuids) {
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
    public void delete(Schema entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Schema> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
