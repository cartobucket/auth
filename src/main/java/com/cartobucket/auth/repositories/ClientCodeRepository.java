package com.cartobucket.auth.repositories;

import com.cartobucket.auth.models.ClientCode;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ClientCodeRepository extends CrudRepository<ClientCode, UUID> {
    ClientCode findByCode(String code);
}
