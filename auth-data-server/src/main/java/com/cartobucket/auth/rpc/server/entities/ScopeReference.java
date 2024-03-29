package com.cartobucket.auth.rpc.server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "scopeId", "resourceId" }) })
public class ScopeReference {
    public enum ScopeReferenceType {
        AUTHORIZATION_SERVER,
        APPLICATION,
        APPLICATION_SECRET,
        CLIENT
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getScopeId() {
        return scopeId;
    }

    public void setScopeId(UUID scopeId) {
        this.scopeId = scopeId;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public ScopeReferenceType getScopeReferenceType() {
        return scopeReferenceType;
    }

    public void setScopeReferenceType(ScopeReferenceType scopeReferenceType) {
        this.scopeReferenceType = scopeReferenceType;
    }

    @Id
    @GeneratedValue
    private UUID id;

    private UUID scopeId;

    private UUID resourceId;
    private ScopeReferenceType scopeReferenceType;
}
