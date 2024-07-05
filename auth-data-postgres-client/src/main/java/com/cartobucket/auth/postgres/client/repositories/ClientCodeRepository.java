package com.cartobucket.auth.postgres.client.repositories;

import com.cartobucket.auth.postgres.client.entities.ClientCode;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClientCodeRepository implements PanacheRepositoryBase<ClientCode, UUID> {
    public Optional<ClientCode> findByCode(String code) {
        return find("code = ?1", code).singleResultOptional();
    }

}
