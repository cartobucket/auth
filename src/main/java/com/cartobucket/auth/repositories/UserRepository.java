package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    User findByEmail(String email);
    User findByUsername(String username);
}
