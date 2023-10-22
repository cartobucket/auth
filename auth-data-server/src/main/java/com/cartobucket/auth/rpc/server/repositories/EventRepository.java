package com.cartobucket.auth.rpc.server.repositories;

import com.cartobucket.auth.data.collections.Pair;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.rpc.server.entities.Event;
import com.cartobucket.auth.rpc.server.entities.EventType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class EventRepository implements PanacheRepositoryBase<Event, UUID> {
    // TODO: Figure out how to version things.
    final private ObjectMapper objectMapper = getObjectMapper();

    public Event createApplicationProfileEvent(Pair<Application, Profile> applicationAndProfile, EventType eventType) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setAuthorizationServerId(applicationAndProfile.getLeft().getAuthorizationServerId());
        event.setResourceId(applicationAndProfile.getLeft().getId());
        event.setCreatedOn(OffsetDateTime.now());

        event.setResource(
                Map.of(
                        "application", objectMapper.convertValue(applicationAndProfile.getLeft(), new TypeReference<Map<String, Object>>() {}),
                        "profile", objectMapper.convertValue(applicationAndProfile.getRight().getProfile(), new TypeReference<Map<String, Object>>() {}))
        );

        persist(event);
        return event;
    }

    private static ObjectMapper getObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
