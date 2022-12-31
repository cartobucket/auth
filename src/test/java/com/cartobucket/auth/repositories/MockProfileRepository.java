package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.ProfileType;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockProfileRepository implements ProfileRepository {
    @Override
    public Profile findByResourceAndProfileType(UUID resource, ProfileType profileType) {
        var profile = new Profile();
        var _profile = new HashMap<String, Object>();
        profile.setResource(resource);
        profile.setUpdatedOn(OffsetDateTime.now());
        profile.setCreatedOn(OffsetDateTime.now());
        profile.setProfileType(profileType);
        profile.setAuthorizationServerId(UUID.randomUUID());
        profile.setId(UUID.randomUUID());
        profile.setProfile(_profile);
        switch (profileType) {
            case User -> {
                _profile.put("firstName", "Test");
                _profile.put("lastName", "User");
                _profile.put("email", "test.user@acme.co");
                _profile.put("sub", "test.user@acme.co");
            }
            case Application -> {
                _profile.put("sub", resource);
            }
        }
        return profile;
    }

    @Override
    public Profile findByResource(UUID resource) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends Profile> S save(S entity) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends Profile> Iterable<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Profile> findById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<Profile> findAll() {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<Profile> findAllById(Iterable<UUID> uuids) {
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
    public void delete(Profile entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends Profile> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}
