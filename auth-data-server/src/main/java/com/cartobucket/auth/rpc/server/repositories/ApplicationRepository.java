package com.cartobucket.auth.rpc.server.repositories;

import com.cartobucket.auth.rpc.server.entities.Application;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ApplicationRepository implements PanacheRepositoryBase<Application, UUID> {
    public List<Application> findAllByAuthorizationServerIdIn(List<UUID> authorizationServerIds) {
        return list("authorizationServerId in ?1", authorizationServerIds);
    }

}
