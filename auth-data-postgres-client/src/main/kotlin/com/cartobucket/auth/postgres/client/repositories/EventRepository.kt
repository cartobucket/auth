package com.cartobucket.auth.postgres.client.repositories

import com.cartobucket.auth.data.domain.Application
import com.cartobucket.auth.data.domain.AuthorizationServer
import com.cartobucket.auth.data.domain.Client
import com.cartobucket.auth.data.domain.ClientCode
import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.ResourceType
import com.cartobucket.auth.data.domain.Schema
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.domain.SigningKey
import com.cartobucket.auth.data.domain.Template
import com.cartobucket.auth.data.domain.User
import com.cartobucket.auth.postgres.client.entities.Event
import com.cartobucket.auth.postgres.client.entities.EventType
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class EventRepository : PanacheRepositoryBase<Event, UUID> {
    // TODO: Figure out how to version things.
    // TODO: DRY event creation, should be easier in Java 21.

    fun createApplicationProfileEvent(
        applicationAndProfile: Pair<Application, Profile>,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = applicationAndProfile.left?.authorizationServerId
        event.resourceType = ResourceType.APPLICATION
        event.resourceId = applicationAndProfile.left?.id
        event.createdOn = OffsetDateTime.now()

        event.resource =
            mapOf(
                "application" to convertToMap(applicationAndProfile.left),
                "profile" to convertToMap(applicationAndProfile.right?.profile ?: emptyMap<String, Any>()),
            )

        persist(event)
        return event
    }

    fun createAuthorizationServerEvent(
        authorizationServer: AuthorizationServer,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = authorizationServer.id
        event.resourceType = ResourceType.AUTHORIZATION_SERVER
        event.resourceId = authorizationServer.id
        event.createdOn = OffsetDateTime.now()

        event.resource =
            mapOf(
                "authorizationServer" to convertToMap(authorizationServer),
            )

        persist(event)
        return event
    }

    fun createSingingKeyEvent(
        signingKey: SigningKey,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = signingKey.authorizationServerId
        event.resourceType = ResourceType.SIGNING_KEY
        event.resourceId = signingKey.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(signingKey).toMutableMap()
        resource.remove("privateKey")

        event.resource = mapOf("signingKey" to resource)

        persist(event)
        return event
    }

    fun createTemplateEvent(
        template: Template,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = template.authorizationServerId
        event.resourceType = ResourceType.TEMPLATE
        event.resourceId = template.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(template)

        event.resource = mapOf("template" to resource)

        persist(event)
        return event
    }

    fun createClientCodeEvent(
        clientCode: ClientCode,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = clientCode.authorizationServerId
        event.resourceType = ResourceType.CLIENT_CODE
        event.resourceId = clientCode.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(clientCode)

        event.resource = mapOf("clientCode" to resource)

        persist(event)
        return event
    }

    fun createClientEvent(
        client: Client,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = client.authorizationServerId
        event.resourceType = ResourceType.CLIENT
        event.resourceId = client.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(client)

        event.resource = mapOf("client" to resource)

        persist(event)
        return event
    }

    fun createSchemaEvent(
        schema: Schema,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = schema.authorizationServerId
        event.resourceType = ResourceType.SCHEMA
        event.resourceId = schema.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(schema)

        event.resource = mapOf("schema" to resource)

        persist(event)
        return event
    }

    fun createScopeEvent(
        scope: Scope,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = scope.authorizationServer?.id
        event.resourceType = ResourceType.SCOPE
        event.resourceId = scope.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(scope)

        event.resource = mapOf("scope" to resource)

        persist(event)
        return event
    }

    fun createUserProfileEvent(
        pair: Pair<User, Profile>,
        eventType: EventType,
    ): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = pair.left?.authorizationServerId
        event.resourceType = ResourceType.USER
        event.resourceId = pair.left?.id
        event.createdOn = OffsetDateTime.now()

        event.resource =
            mapOf(
                "user" to convertToMap(pair.left),
                "profile" to convertToMap(pair.right?.profile ?: emptyMap<String, Any>()),
            )

        persist(event)
        return event
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertToMap(obj: Any?): Map<String, Any?> {
        // For event logging, we'll just store the object's toString representation
        // This avoids circular reference issues and removes Jackson dependency
        val map = HashMap<String, Any?>()
        map["type"] = obj?.javaClass?.simpleName
        map["data"] = obj?.toString()
        return map
    }
}
