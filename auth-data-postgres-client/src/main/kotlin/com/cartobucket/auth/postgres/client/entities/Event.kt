package com.cartobucket.auth.postgres.client.entities

import com.cartobucket.auth.data.domain.ResourceType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.OffsetDateTime
import java.util.UUID

@Entity
class Event {
    @Id
    @GeneratedValue
    var id: UUID? = null

    var eventType: EventType? = null

    var version: String? = null

    var authorizationServerId: UUID? = null

    var resourceType: ResourceType? = null

    var resourceId: UUID? = null

    @JdbcTypeCode(SqlTypes.JSON)
    var resource: Map<String, Any?>? = null

    var createdOn: OffsetDateTime? = null
}
