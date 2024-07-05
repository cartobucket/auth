package com.cartobucket.auth.postgres.client.repositories;

import com.cartobucket.auth.postgres.client.entities.Application;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ApplicationRepository implements PanacheRepositoryBase<Application, UUID> {
}
