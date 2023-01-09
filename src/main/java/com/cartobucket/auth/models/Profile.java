package com.cartobucket.auth.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "profileType" }) })
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Profile {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID authorizationServerId;

    private UUID resource;

    private ProfileType profileType;

//    @JdbcTypeCode(SqlTypes.JSON)
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> profile;

    private OffsetDateTime createdOn;

    private OffsetDateTime updatedOn;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public Map<String, Object> getProfile() {
        return profile;
    }

    public void setProfile(Map<String, Object> profile) {
        this.profile = profile;
    }

    public UUID getResource() {
        return resource;
    }

    public void setResource(UUID resource) {
        this.resource = resource;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
