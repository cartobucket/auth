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

package com.cartobucket.auth.rpc.server.entities.mappers;

import com.cartobucket.auth.data.domain.Profile;
import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileMapper {
    public static Profile from (com.cartobucket.auth.rpc.server.entities.Profile profile) {
        var _profile = new Profile();
        _profile.setId(profile.getId());
        _profile.setProfile(profile.getProfile());
        _profile.setProfileType(profile.getProfileType());
        _profile.setResource(profile.getResource());
        _profile.setAuthorizationServerId(profile.getAuthorizationServerId());
        _profile.setUpdatedOn(profile.getUpdatedOn());
        _profile.setCreatedOn(profile.getCreatedOn());
        return _profile;
    }

    public static com.cartobucket.auth.rpc.server.entities.Profile to (Profile profile) {
        var _profile = new com.cartobucket.auth.rpc.server.entities.Profile();
        _profile.setId(profile.getId());
        _profile.setProfile(profile.getProfile());
        _profile.setProfileType(profile.getProfileType());
        _profile.setResource(profile.getResource());
        _profile.setAuthorizationServerId(profile.getAuthorizationServerId());
        _profile.setUpdatedOn(profile.getUpdatedOn());
        _profile.setCreatedOn(profile.getCreatedOn());
        return _profile;
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
                return value.getListValue().getValuesList().stream().map(ProfileMapper::toNative).toList();
            }
            default -> {
                return "";
            }
        }
    }

    private static Value fromNative(Object value) {
        if (value instanceof String) {
            return Value.newBuilder().setStringValue(value.toString()).build();
        } else if (value instanceof Double) {
            return Value.newBuilder().setNumberValue((Double) value).build();
        } else if (value instanceof Boolean) {
            return Value.newBuilder().setBoolValue((Boolean) value).build();
        } else if (value instanceof HashMap) {
            return Value.newBuilder().setStructValue(toProtoMap((HashMap) value)).build();
        } else if (value instanceof List) {
            return Value
                    .newBuilder()
                    .setListValue(
                            ListValue
                                    .newBuilder()
                                    .addAllValues(
                                            ((List<Value>) value)
                                                    .stream()
                                                    .map(ProfileMapper::fromNative)
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
