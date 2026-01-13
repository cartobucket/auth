package com.revethq.auth.persistence.entities

import com.revethq.auth.core.domain.ResourceType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.OffsetDateTime
import java.util.UUID

@Entity
open class Event {
    @Id
    @GeneratedValue
    open var id: UUID? = null
    open var eventType: EventType? = null
    open var version: String? = null
    open var authorizationServerId: UUID? = null
    open var resourceType: ResourceType? = null
    open var resourceId: UUID? = null

    @JdbcTypeCode(SqlTypes.JSON)
    open var resource: Map<String, Any>? = null
    open var createdOn: OffsetDateTime? = null
}
