/*
 * Copyright 2023 Bryce Groff (Revet)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.revethq.auth.persistence.repositories

import com.revethq.auth.core.domain.Pair
import com.revethq.auth.core.domain.Application
import com.revethq.auth.core.domain.AuthorizationServer
import com.revethq.auth.core.domain.Client
import com.revethq.auth.core.domain.ClientCode
import com.revethq.auth.core.domain.Group
import com.revethq.auth.core.domain.GroupMember
import com.revethq.auth.core.domain.Profile
import com.revethq.auth.core.domain.ResourceType
import com.revethq.auth.core.domain.Schema
import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.domain.SigningKey
import com.revethq.auth.core.domain.Template
import com.revethq.auth.core.domain.User
import com.revethq.auth.persistence.entities.Event
import com.revethq.auth.persistence.entities.EventType
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class EventRepository : PanacheRepositoryBase<Event, UUID> {
    // TODO: Figure out how to version things.
    // TODO: DRY event creation, should be easier in Java 21.

    fun createApplicationProfileEvent(applicationAndProfile: Pair<Application, Profile>, eventType: EventType): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = applicationAndProfile.left?.authorizationServerId
        event.resourceType = ResourceType.APPLICATION
        event.resourceId = applicationAndProfile.left?.id
        event.createdOn = OffsetDateTime.now()

        event.resource = mapOf(
            "application" to convertToMap(applicationAndProfile.left),
            "profile" to convertToMap(applicationAndProfile.right?.profile)
        )

        persist(event)
        return event
    }

    fun createAuthorizationServerEvent(authorizationServer: AuthorizationServer, eventType: EventType): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = authorizationServer.id
        event.resourceType = ResourceType.AUTHORIZATION_SERVER
        event.resourceId = authorizationServer.id
        event.createdOn = OffsetDateTime.now()

        event.resource = mapOf(
            "authorizationServer" to convertToMap(authorizationServer)
        )

        persist(event)
        return event
    }

    fun createSigningKeyEvent(signingKey: SigningKey, eventType: EventType): Event {
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

    fun createTemplateEvent(template: Template, eventType: EventType): Event {
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

    fun createClientCodeEvent(clientCode: ClientCode, eventType: EventType): Event {
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

    fun createClientEvent(client: Client, eventType: EventType): Event {
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

    fun createSchemaEvent(schema: Schema, eventType: EventType): Event {
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

    fun createScopeEvent(scope: Scope, eventType: EventType): Event {
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

    fun createUserProfileEvent(pair: Pair<User, Profile>, eventType: EventType): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = pair.left?.authorizationServerId
        event.resourceType = ResourceType.USER
        event.resourceId = pair.left?.id
        event.createdOn = OffsetDateTime.now()

        event.resource = mapOf(
            "user" to convertToMap(pair.left),
            "profile" to convertToMap(pair.right?.profile)
        )

        persist(event)
        return event
    }

    fun createGroupEvent(group: Group, eventType: EventType): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = group.authorizationServerId
        event.resourceType = ResourceType.GROUP
        event.resourceId = group.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(group)

        event.resource = mapOf("group" to resource)

        persist(event)
        return event
    }

    fun createGroupMemberEvent(groupMember: GroupMember, eventType: EventType): Event {
        val event = Event()
        event.eventType = eventType
        event.authorizationServerId = groupMember.authorizationServerId
        event.resourceType = ResourceType.GROUP_MEMBER
        event.resourceId = groupMember.id
        event.createdOn = OffsetDateTime.now()
        val resource = convertToMap(groupMember)

        event.resource = mapOf("groupMember" to resource)

        persist(event)
        return event
    }

    private fun convertToMap(obj: Any?): Map<String, Any> {
        // For event logging, we'll just store the object's toString representation
        // This avoids circular reference issues and removes Jackson dependency
        val map = mutableMapOf<String, Any>()
        map["type"] = obj?.javaClass?.simpleName ?: "null"
        map["data"] = obj?.toString() ?: "null"
        return map
    }
}
