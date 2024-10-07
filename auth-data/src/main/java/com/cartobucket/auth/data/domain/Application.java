/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
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
import java.util.List;
import java.util.UUID;

public class Application {
    private UUID id;

    private String clientId;

    private String name;

    private UUID authorizationServerId;

    private List<Scope> scopes;

    private Metadata metadata;

    private OffsetDateTime createdOn;

    private OffsetDateTime updatedOn;

    public Application(UUID id, String clientId, String name, UUID authorizationServerId, List<Scope> scopes, Metadata metadata, OffsetDateTime createdOn, OffsetDateTime updatedOn) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.authorizationServerId = authorizationServerId;
        this.scopes = scopes;
        this.metadata = metadata;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }

    public static class Builder {
        private UUID id;
        private String clientId;
        private String name;
        private UUID authorizationServerId;
        private List<Scope> scopes;
        private Metadata metadata;
        private OffsetDateTime createdOn;
        private OffsetDateTime updatedOn;

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAuthorizationServerId(UUID authorizationServerId) {
            this.authorizationServerId = authorizationServerId;
            return this;
        }

        public Builder setScopes(List<Scope> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder setMetadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder setCreatedOn(OffsetDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder setUpdatedOn(OffsetDateTime updatedOn) {
            this.updatedOn = updatedOn;
            return this;
        }

        public Application build() {
            return new Application(id, clientId, name, authorizationServerId, scopes, metadata, createdOn, updatedOn);
        }
    }
}
