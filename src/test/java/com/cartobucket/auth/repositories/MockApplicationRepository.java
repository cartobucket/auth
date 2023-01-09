package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Application;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockApplicationRepository implements ApplicationRepository {
    public static final String VALID_CLIENT_ID = UUID.randomUUID().toString();
    public static final UUID VALID_APPLICATION_ID = UUID.randomUUID();

    @Override
    public Application findByClientId(String clientId) {
        if (VALID_CLIENT_ID.equals(clientId)) {
            var application = new Application();
            application.setName("Test Application");
            application.setClientId(VALID_CLIENT_ID);
            application.setId(VALID_APPLICATION_ID);
            application.setAuthorizationServerId(MockAuthorizationServerRepository.VALID_AUTHORIZATION_SERVER_ID);
            application.setCreatedOn(OffsetDateTime.now());
            application.setUpdatedOn(OffsetDateTime.now());
            return application;
        }
        return null;
    }

    @Override
    public <S extends Application> S save(S entity) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends Application> Iterable<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Application> findById(UUID uuid) {
        var application = new Application();
        application.setName("Test Application");
        application.setClientId(VALID_CLIENT_ID);
        application.setId(VALID_APPLICATION_ID);
        application.setAuthorizationServerId(MockAuthorizationServerRepository.VALID_AUTHORIZATION_SERVER_ID);
        application.setCreatedOn(OffsetDateTime.now());
        application.setUpdatedOn(OffsetDateTime.now());
        return Optional.of(application);
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<Application> findAll() {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<Application> findAllById(Iterable<UUID> uuids) {
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
    public void delete(Application entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends Application> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}
