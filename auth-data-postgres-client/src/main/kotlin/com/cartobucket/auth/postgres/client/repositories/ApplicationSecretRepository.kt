package com.cartobucket.auth.postgres.client.repositories

import com.cartobucket.auth.postgres.client.entities.ApplicationSecret
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional
import java.util.UUID

@ApplicationScoped
class ApplicationSecretRepository : PanacheRepositoryBase<ApplicationSecret, UUID> {
    fun findByApplicationIdIn(applicationId: List<UUID>): List<ApplicationSecret> = list("applicationId in ?1", applicationId)

    fun findByApplicationSecretHash(secretHash: String): Optional<ApplicationSecret> =
        find("applicationSecretHash = ?1", secretHash).singleResultOptional()
}
