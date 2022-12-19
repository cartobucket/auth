package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.models.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {
    Profile findByResourceAndProfileType(UUID resource, ProfileType profileType);

    Profile findByResource(UUID resource);
}
