package com.cartobucket.auth.postgres.client.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
//        uniqueConstraints = [UniqueConstraint(columnNames = ["scopeId", "resourceId"])]
)
class ScopeReference {
    enum class ScopeReferenceType {
        AUTHORIZATION_SERVER,
        APPLICATION,
        APPLICATION_SECRET,
        CLIENT,
    }

    @Id
    @GeneratedValue
    var id: UUID? = null

    var scopeId: UUID? = null

    var resourceId: UUID? = null

    var scopeReferenceType: ScopeReferenceType? = null
}
