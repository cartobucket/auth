package com.cartobucket.auth.postgres.client.repositories

import com.cartobucket.auth.postgres.client.entities.ClientCode
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional
import java.util.UUID

@ApplicationScoped
class ClientCodeRepository : PanacheRepositoryBase<ClientCode, UUID> {
    fun findByCode(code: String): Optional<ClientCode> = find("code = ?1", code).singleResultOptional()
}
