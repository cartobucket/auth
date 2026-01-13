package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.util.Objects;

@JsonbNillable(false)
public class AuthorizationServerRequest {

    private String serverUrl;
    private String audience;
    private Integer clientCredentialsTokenExpiration;
    private Integer authorizationCodeTokenExpiration;
    private String name;
    private Metadata metadata;

    public AuthorizationServerRequest() {}

    public AuthorizationServerRequest serverUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthorizationServerRequest audience(String audience) {
        this.audience = audience;
        return this;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public AuthorizationServerRequest clientCredentialsTokenExpiration(Integer clientCredentialsTokenExpiration) {
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
        return this;
    }

    public Integer getClientCredentialsTokenExpiration() {
        return clientCredentialsTokenExpiration;
    }

    public void setClientCredentialsTokenExpiration(Integer clientCredentialsTokenExpiration) {
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
    }

    public AuthorizationServerRequest authorizationCodeTokenExpiration(Integer authorizationCodeTokenExpiration) {
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
        return this;
    }

    public Integer getAuthorizationCodeTokenExpiration() {
        return authorizationCodeTokenExpiration;
    }

    public void setAuthorizationCodeTokenExpiration(Integer authorizationCodeTokenExpiration) {
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
    }

    public AuthorizationServerRequest name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorizationServerRequest metadata(Metadata metadata) {
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
        AuthorizationServerRequest that = (AuthorizationServerRequest) o;
        return Objects.equals(serverUrl, that.serverUrl) &&
                Objects.equals(audience, that.audience) &&
                Objects.equals(clientCredentialsTokenExpiration, that.clientCredentialsTokenExpiration) &&
                Objects.equals(authorizationCodeTokenExpiration, that.authorizationCodeTokenExpiration) &&
                Objects.equals(name, that.name) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverUrl, audience, clientCredentialsTokenExpiration, 
                          authorizationCodeTokenExpiration, name, metadata);
    }

    @Override
    public String toString() {
        return "AuthorizationServerRequest{" +
                "serverUrl='" + serverUrl + '\'' +
                ", audience='" + audience + '\'' +
                ", clientCredentialsTokenExpiration=" + clientCredentialsTokenExpiration +
                ", authorizationCodeTokenExpiration=" + authorizationCodeTokenExpiration +
                ", name='" + name + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}