package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.ApplicationSecret;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationSecretRepository extends CrudRepository<ApplicationSecret, UUID> {
    List<ApplicationSecret> findByApplicationId(UUID applicationId);

    Optional<ApplicationSecret> findByApplicationIdAndId(UUID applicationId, UUID id);
}
