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
import java.util.Map;
import java.util.UUID;

public class Profile {
    private UUID id;

    private UUID authorizationServerId;

    private UUID resource;

    private ProfileType profileType;

    private Map<String, Object> profile;

    private OffsetDateTime createdOn;

    private OffsetDateTime updatedOn;

    public Profile(UUID id, UUID authorizationServerId, UUID resource, ProfileType profileType, Map<String, Object> profile, OffsetDateTime createdOn, OffsetDateTime updatedOn) {
        this.id = id;
        this.authorizationServerId = authorizationServerId;
        this.resource = resource;
        this.profileType = profileType;
        this.profile = profile;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

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


    public static class Builder {
        private UUID id;
        private UUID authorizationServerId;
        private UUID resource;
        private ProfileType profileType;
        private Map<String, Object> profile;
        private OffsetDateTime createdOn;
        private OffsetDateTime updatedOn;

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setAuthorizationServerId(UUID authorizationServerId) {
            this.authorizationServerId = authorizationServerId;
            return this;
        }

        public Builder setResource(UUID resource) {
            this.resource = resource;
            return this;
        }

        public Builder setProfileType(ProfileType profileType) {
            this.profileType = profileType;
            return this;
        }

        public Builder setProfile(Map<String, Object> profile) {
            this.profile = profile;
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

        public Profile build() {
            return new Profile(id, authorizationServerId, resource, profileType, profile, createdOn, updatedOn);
        }
    }

}
