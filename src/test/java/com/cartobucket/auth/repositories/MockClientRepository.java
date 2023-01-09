package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Client;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockClientRepository implements ClientRepository {
    @Override
    public <S extends Client> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Client> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Client> findById(UUID uuid) {
        var client = new Client();
        try {
            client.setRedirectUris(List.of(new URI("https://test")));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(client);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<Client> findAll() {
        return null;
    }

    @Override
    public Iterable<Client> findAllById(Iterable<UUID> uuids) {
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
    public void delete(Client entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Client> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
