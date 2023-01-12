package com.cartobucket.auth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
public class AuthorizationServer {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private URL serverUrl;

    private String audience;

    private Long clientCredentialsTokenExpiration;

    private Long authorizationCodeTokenExpiration;

    private OffsetDateTime createdOn;

    private OffsetDateTime updatedOn;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(URL serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Long getClientCredentialsTokenExpiration() {
        return clientCredentialsTokenExpiration;
    }

    public void setClientCredentialsTokenExpiration(Long clientCredentialsTokenExpiration) {
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
    }

    public Long getAuthorizationCodeTokenExpiration() {
        return authorizationCodeTokenExpiration;
    }

    public void setAuthorizationCodeTokenExpiration(Long authorizationCodeTokenExpiration) {
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
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
