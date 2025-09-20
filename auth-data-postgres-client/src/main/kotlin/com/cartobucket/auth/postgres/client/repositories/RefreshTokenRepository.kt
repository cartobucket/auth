package com.cartobucket.auth.postgres.client.repositories

import com.cartobucket.auth.postgres.client.entities.RefreshToken
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.time.OffsetDateTime
import java.util.Optional
import java.util.UUID

@ApplicationScoped
class RefreshTokenRepository : PanacheRepositoryBase<RefreshToken, UUID> {
    fun findByTokenHash(tokenHash: String): Optional<RefreshToken> =
        find(
            "tokenHash = ?1 and expiresOn > ?2",
            tokenHash,
            OffsetDateTime.now(),
        ).firstResultOptional()

    fun deleteExpiredTokens() {
        delete("expiresOn < ?1", OffsetDateTime.now())
    }
}
