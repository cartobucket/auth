package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.ClientCode;
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
public class MockClientCodeRepository implements ClientCodeRepository {
    public static final String VALID_CLIENT_CODE = "TEST_CODE";
    public static final UUID VALID_CLIENT_ID = UUID.randomUUID();

    @Override
    public ClientCode findByCode(String code) {
        if (VALID_CLIENT_CODE.equals(code)) {
            var clientCode = new ClientCode();
            clientCode.setClientId(VALID_CLIENT_ID);
            clientCode.setCode(VALID_CLIENT_CODE);
            clientCode.setCodeChallenge("lO4FkzXlh+UBzEv5BhPggU8Ap7CLx8ZI/YZaKvaiLMI=");
            clientCode.setCodeChallengeMethod("S256");
            clientCode.setNonce("NONCE");
            clientCode.setState("STATE");
            clientCode.setUserId(UUID.randomUUID());
            clientCode.setId(UUID.randomUUID());
            clientCode.setAuthorizationServerId(UUID.randomUUID()); // TODO: This needs to be something that matches
            clientCode.setRedirectUri("https://test");
            clientCode.setScopes(List.of("test"));
            clientCode.setCreatedOn(OffsetDateTime.now());
            return clientCode;
        }
        return null;
    }

    @Override
    public <S extends ClientCode> S save(S entity) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends ClientCode> Iterable<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<ClientCode> findById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<ClientCode> findAll() {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<ClientCode> findAllById(Iterable<UUID> uuids) {
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
    public void delete(ClientCode entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends ClientCode> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}
