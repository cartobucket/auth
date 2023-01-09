package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ClientRepository extends CrudRepository<Client, UUID> {
}
