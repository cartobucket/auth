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

public final class ApplicationSecret {
    private UUID id;

    private UUID applicationId;

    private UUID authorizationServerId;

    private String name;

    private String applicationSecret;

    private String applicationSecretHash;

    private Metadata metadata;

    private OffsetDateTime createdOn;

    private  Integer expiresIn;

    private List<Scope> scopes;

    public ApplicationSecret(UUID id, UUID applicationId, UUID authorizationServerId, String name, String applicationSecret, String applicationSecretHash, Metadata metadata, OffsetDateTime createdOn, Integer expiresIn, List<Scope> scopes) {
        this.id = id;
        this.applicationId = applicationId;
        this.authorizationServerId = authorizationServerId;
        this.name = name;
        this.applicationSecret = applicationSecret;
        this.applicationSecretHash = applicationSecretHash;
        this.metadata = metadata;
        this.createdOn = createdOn;
        this.expiresIn = expiresIn;
        this.scopes = scopes;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }
    public String getApplicationSecretHash() {
        return applicationSecretHash;
    }

    public void setApplicationSecretHash(String clientSecretHash) {
        this.applicationSecretHash = clientSecretHash;
    }

    public String getApplicationSecret() {
        return applicationSecret;
    }

    public void setApplicationSecret(String clientSecret) {
        this.applicationSecret = clientSecret;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public static class Builder {
        private UUID id;
        private UUID applicationId;
        private UUID authorizationServerId;
        private String name;
        private String applicationSecret;
        private String applicationSecretHash;
        private Metadata metadata;
        private OffsetDateTime createdOn;
        private Integer expiresIn;
        private List<Scope> scopes;

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setApplicationId(UUID applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder setAuthorizationServerId(UUID authorizationServerId) {
            this.authorizationServerId = authorizationServerId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setApplicationSecret(String applicationSecret) {
            this.applicationSecret = applicationSecret;
            return this;
        }

        public Builder setApplicationSecretHash(String applicationSecretHash) {
            this.applicationSecretHash = applicationSecretHash;
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

        public Builder setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder setScopes(List<Scope> scopes) {
            this.scopes = scopes;
            return this;
        }

        public ApplicationSecret build() {
            if (expiresIn == null) {
                expiresIn = 0;
            }
            return new ApplicationSecret(id, applicationId, authorizationServerId, name, applicationSecret, applicationSecretHash, metadata, createdOn, expiresIn, scopes);
        }
    }
}
