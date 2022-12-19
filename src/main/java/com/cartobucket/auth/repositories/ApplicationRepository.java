package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Application;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ApplicationRepository extends CrudRepository<Application, UUID> {
    Application findByClientId(String clientId);
}
