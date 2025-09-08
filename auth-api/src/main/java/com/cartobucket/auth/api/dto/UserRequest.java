package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class UserRequest {
    
    @JsonbProperty("authorization_server_id")
    private UUID authorizationServerId;
    
    @JsonbProperty("username")
    private String username;
    
    @JsonbProperty("email")
    private String email;
    
    @JsonbProperty("profile")
    private Object profile;
    
    @JsonbProperty("password")
    private String password;
    
    @JsonbProperty("metadata")
    private Metadata metadata;

    public UserRequest() {}

    public UserRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public UserRequest username(String username) {
        this.username = username;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRequest email(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRequest profile(Object profile) {
        this.profile = profile;
        return this;
    }

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    public UserRequest password(String password) {
        this.password = password;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRequest metadata(Metadata metadata) {
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
        UserRequest that = (UserRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(profile, that.profile) &&
                Objects.equals(password, that.password) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, username, email, profile, password, metadata);
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profile=" + profile +
                ", password='[PROTECTED]'" +
                ", metadata=" + metadata +
                '}';
    }
}