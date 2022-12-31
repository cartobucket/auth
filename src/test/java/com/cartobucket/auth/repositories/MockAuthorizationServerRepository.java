package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.AuthorizationServer;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockAuthorizationServerRepository implements AuthorizationServerRepository {
    public static final UUID VALID_AUTHORIZATION_SERVER_ID = UUID.randomUUID();

    @Override
    public <S extends AuthorizationServer> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AuthorizationServer> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AuthorizationServer> findById(UUID uuid) {
        if (VALID_AUTHORIZATION_SERVER_ID.equals(uuid)) {
            return Optional.of(buildAuthorizationServer());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public Iterable<AuthorizationServer> findAll() {
        final var authorizationServers = new ArrayList<AuthorizationServer>(1);
        authorizationServers.add(buildAuthorizationServer());
        return authorizationServers;
    }

    private static AuthorizationServer buildAuthorizationServer() {
        final var authorizationServer = new AuthorizationServer();
        try {
            authorizationServer.setServerUrl(new URL("https://acccounts.cartobucket.com"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        authorizationServer.setId(VALID_AUTHORIZATION_SERVER_ID);
        authorizationServer.setAudience("api://");
        authorizationServer.setAuthorizationCodeTokenExpiration(300L);
        authorizationServer.setClientCredentialsTokenExpiration(300L);
        return authorizationServer;
    }

    @Override
    public Iterable<AuthorizationServer> findAllById(Iterable<UUID> uuids) {
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
    public void delete(AuthorizationServer entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends AuthorizationServer> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}
