package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class UserResponse {
    
    @JsonbProperty("id")
    private String id;
    
    @JsonbProperty("authorization_server_id")
    private UUID authorizationServerId;
    
    @JsonbProperty("username")
    private String username;
    
    @JsonbProperty("email")
    private String email;
    
    @JsonbProperty("profile")
    private Object profile;
    
    @JsonbProperty("created_on")
    private OffsetDateTime createdOn;
    
    @JsonbProperty("updated_on")
    private OffsetDateTime updatedOn;
    
    @JsonbProperty("metadata")
    private Metadata metadata;

    public UserResponse() {}

    public UserResponse id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public UserResponse username(String username) {
        this.username = username;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserResponse email(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserResponse profile(Object profile) {
        this.profile = profile;
        return this;
    }

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    public UserResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public UserResponse updatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UserResponse metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(profile, that.profile) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(updatedOn, that.updatedOn) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationServerId, username, email, profile, createdOn, updatedOn, metadata);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id='" + id + '\'' +
                ", authorizationServerId=" + authorizationServerId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profile=" + profile +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", metadata=" + metadata +
                '}';
    }
}