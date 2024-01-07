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
    @Id
    @GeneratedValue
    private UUID id;

    private UUID scopeId;

    private UUID resourceId;
}
