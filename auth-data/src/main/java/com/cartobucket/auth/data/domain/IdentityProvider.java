/*
 * Copyright 2024 Bryce Groff (Cartobucket LLC)
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

package com.cartobucket.auth.data.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class IdentityProvider {
    private UUID id;

    private String name;

    private UUID authorizationServerId;

    private DiscoveryMethodEnum discoveryMethod;

    private String discoveryEndpoint;

    private WellKnownEndpoints wellKnownEndpoints;

    private String clientId;

    private String clientSecret;

    private Boolean usePkce;

    private Metadata metadata;

    private OffsetDateTime createdOn;

    private OffsetDateTime updatedOn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public DiscoveryMethodEnum getDiscoveryMethod() {
        return discoveryMethod;
    }

    public void setDiscoveryMethod(DiscoveryMethodEnum discoveryMethod) {
        this.discoveryMethod = discoveryMethod;
    }

    public String getDiscoveryEndpoint() {
        return discoveryEndpoint;
    }

    public void setDiscoveryEndpoint(String discoveryEndpoint) {
        this.discoveryEndpoint = discoveryEndpoint;
    }

    public WellKnownEndpoints getWellKnownEndpoints() {
        return wellKnownEndpoints;
    }

    public void setWellKnownEndpoints(WellKnownEndpoints wellKnownEndpoints) {
        this.wellKnownEndpoints = wellKnownEndpoints;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Boolean getUsePkce() {
        return usePkce;
    }

    public void setUsePkce(Boolean usePkce) {
        this.usePkce = usePkce;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityProvider that = (IdentityProvider) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(authorizationServerId, that.authorizationServerId) && discoveryMethod == that.discoveryMethod && Objects.equals(discoveryEndpoint, that.discoveryEndpoint) && Objects.equals(clientId, that.clientId) && Objects.equals(clientSecret, that.clientSecret) && Objects.equals(usePkce, that.usePkce) && Objects.equals(metadata, that.metadata) && Objects.equals(createdOn, that.createdOn) && Objects.equals(updatedOn, that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorizationServerId, discoveryMethod, discoveryEndpoint, clientId, clientSecret, usePkce, metadata, createdOn, updatedOn);
    }

}
