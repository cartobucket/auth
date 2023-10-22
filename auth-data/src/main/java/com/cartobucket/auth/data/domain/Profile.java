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


import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Profile {
    private UUID id;

    private UUID authorizationServerId;

    private UUID resource;

    private ProfileType profileType;

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


    public static Map<String, Object> fromProtoMap(Map<String, Value> valueMap) {
        return valueMap
                .keySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                key -> key,
                                key -> {
                                    var value = valueMap.get(key);
                                    return toNative(value);
                                }
                        )
                );
    }

    private static Object toNative(Value value) {
        switch (value.getKindCase()) {
            case NUMBER_VALUE -> {
                return value.getNumberValue();
            }
            case STRING_VALUE -> {
                return value.getStringValue();
            }
            case BOOL_VALUE -> {
                return value.getBoolValue();
            }
            case STRUCT_VALUE -> {
                return fromProtoMap(value.getStructValue().getFieldsMap());
            }
            case LIST_VALUE -> {
                return value.getListValue().getValuesList().stream().map(Profile::toNative).toList();
            }
            default -> {
                return "";
            }
        }
    }

    private static Value fromNative(Object value) {
        if (value instanceof String) {
            return Value.newBuilder().setStringValue(value.toString()).build();
        } else if (value instanceof Integer) {
            return Value.newBuilder().setNumberValue(((Integer) value).doubleValue()).build();
        } else if (value instanceof Double) {
            return Value.newBuilder().setNumberValue((Double) value).build();
        } else if (value instanceof Boolean) {
            return Value.newBuilder().setBoolValue((Boolean) value).build();
        } else if (value instanceof HashMap) {
            return Value.newBuilder().setStructValue(toProtoMap((HashMap) value)).build();
        } else if (value instanceof List) {
            // TODO: There is a bug in here.
            return Value
                    .newBuilder()
                    .setListValue(
                            ListValue
                                    .newBuilder()
                                    .addAllValues(
                                            ((List<Value>) value)
                                                    .stream()
                                                    .map(Profile::fromNative)
                                                    .toList()
                                    )
                    ).build();
        } else {
            return Value.newBuilder().build();
        }
    }

    public static Struct toProtoMap(Map<String, Object> profile) {
        var struct = Struct.newBuilder().putAllFields(
                        profile
                                .keySet()
                                .stream()
                                .collect(
                                        Collectors
                                                .toMap(
                                                        key -> key,
                                                        key -> {
                                                            var value = profile.get(key);
                                                            return fromNative(value);
                                                        }
                                                )
                                )
                )
                .build();
        return struct;
    }
}
