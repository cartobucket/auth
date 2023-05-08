package com.cartobucket.auth.repositories;


import com.cartobucket.auth.models.Schema;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SchemaRepository extends CrudRepository<Schema, UUID> {
}
