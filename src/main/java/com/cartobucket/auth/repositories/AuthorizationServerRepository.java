package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.AuthorizationServer;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthorizationServerRepository extends CrudRepository<AuthorizationServer, UUID> {
}
