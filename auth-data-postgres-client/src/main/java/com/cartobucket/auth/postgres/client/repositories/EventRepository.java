package com.cartobucket.auth.postgres.client.repositories;

import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ResourceType;
import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.domain.SigningKey;
import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.postgres.client.entities.Event;
import com.cartobucket.auth.postgres.client.entities.EventType;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.JsonObject;
import java.util.HashMap;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class EventRepository implements PanacheRepositoryBase<Event, UUID> {
    // TODO: Figure out how to version things.
    // TODO: DRY event creation, should be easier in Java 21.
    final private Jsonb jsonb = JsonbBuilder.create();

    public Event createApplicationProfileEvent(Pair<Application, Profile> applicationAndProfile, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(applicationAndProfile.getLeft().getAuthorizationServerId());
        event.setResourceType(ResourceType.APPLICATION);
        event.setResourceId(applicationAndProfile.getLeft().getId());
        event.setCreatedOn(OffsetDateTime.now());

        event.setResource(
                Map.of(
                        "application", convertToMap(applicationAndProfile.getLeft()),
                        "profile", convertToMap(applicationAndProfile.getRight().getProfile()))
        );

        persist(event);
        return event;
    }

    public Event createAuthorizationServerEvent(AuthorizationServer authorizationServer, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(authorizationServer.getId());
        event.setResourceType(ResourceType.AUTHORIZATION_SERVER);
        event.setResourceId(authorizationServer.getId());
        event.setCreatedOn(OffsetDateTime.now());

        event.setResource(
                Map.of(
                        "authorizationServer",
                        convertToMap(authorizationServer)
                )
        );

        persist(event);
        return event;
    }

    public Event createSingingKeyEvent(SigningKey signingKey, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(signingKey.getAuthorizationServerId());
        event.setResourceType(ResourceType.SIGNING_KEY);
        event.setResourceId(signingKey.getId());
        event.setCreatedOn(OffsetDateTime.now());
        Map<String, Object> resource = convertToMap(signingKey);
        resource.remove("privateKey");

        event.setResource(
                Map.of("signingKey", resource)
        );

        persist(event);
        return event;
    }

    public Event createTemplateEvent(Template template, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(template.getAuthorizationServerId());
        event.setResourceType(ResourceType.TEMPLATE);
        event.setResourceId(template.getId());
        event.setCreatedOn(OffsetDateTime.now());
        Map<String, Object> resource = convertToMap(template);

        event.setResource(
                Map.of("template", resource)
        );

        persist(event);
        return event;
    }
    public Event createClientCodeEvent(ClientCode clientCode, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(clientCode.getAuthorizationServerId());
        event.setResourceType(ResourceType.CLIENT_CODE);
        event.setResourceId(clientCode.getId());
        event.setCreatedOn(OffsetDateTime.now());
        Map<String, Object> resource = convertToMap(clientCode);

        event.setResource(
                Map.of("clientCode", resource)
        );

        persist(event);
        return event;
    }
    public Event createClientEvent(Client client, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(client.getAuthorizationServerId());
        event.setResourceType(ResourceType.CLIENT);
        event.setResourceId(client.getId());
        event.setCreatedOn(OffsetDateTime.now());
        Map<String, Object> resource = convertToMap(client);

        event.setResource(
                Map.of("client", resource)
        );

        persist(event);
        return event;
    }

    public Event createSchemaEvent(Schema schema, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(schema.getAuthorizationServerId());
        event.setResourceType(ResourceType.SCHEMA);
        event.setResourceId(schema.getId());
        event.setCreatedOn(OffsetDateTime.now());
        Map<String, Object> resource = convertToMap(schema);

        event.setResource(
                Map.of("schema", resource)
        );

        persist(event);
        return event;
    }

    public Event createScopeEvent(Scope scope, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(scope.getAuthorizationServer().getId());
        event.setResourceType(ResourceType.SCOPE);
        event.setResourceId(scope.getId());
        event.setCreatedOn(OffsetDateTime.now());
        Map<String, Object> resource = convertToMap(scope);

        event.setResource(
                Map.of("scope", resource)
        );

        persist(event);
        return event;
    }

    public Event createUserProfileEvent(Pair<User, Profile> pair, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(pair.getLeft().getAuthorizationServerId());
        event.setResourceType(ResourceType.USER);
        event.setResourceId(pair.getLeft().getId());
        event.setCreatedOn(OffsetDateTime.now());

        event.setResource(
                Map.of(
                        "user", convertToMap(pair.getLeft()),
                        "profile", convertToMap(pair.getRight().getProfile()))
        );

        persist(event);
        return event;
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertToMap(Object obj) {
        String json = jsonb.toJson(obj);
        JsonObject jsonObject = jsonb.fromJson(json, JsonObject.class);
        Map<String, Object> map = new HashMap<>();
        jsonObject.forEach((key, value) -> {
            if (value instanceof jakarta.json.JsonValue) {
                switch (value.getValueType()) {
                    case STRING:
                        map.put(key, ((jakarta.json.JsonString) value).getString());
                        break;
                    case NUMBER:
                        map.put(key, ((jakarta.json.JsonNumber) value).numberValue());
                        break;
                    case TRUE:
                        map.put(key, true);
                        break;
                    case FALSE:
                        map.put(key, false);
                        break;
                    case NULL:
                        map.put(key, null);
                        break;
                    default:
                        map.put(key, value);
                }
            } else {
                map.put(key, value);
            }
        });
        return map;
    }
}
