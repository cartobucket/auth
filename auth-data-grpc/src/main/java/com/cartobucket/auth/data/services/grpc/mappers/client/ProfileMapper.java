package com.cartobucket.auth.data.services.grpc.mappers.client;

import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Struct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;

public class ProfileMapper {
    public static Profile toProfile(Struct profile, ProfileType profileType) {
        var _profile = new Profile();
        _profile.setProfileType(profileType);
            _profile.setProfile(fromProtoMap(profile.getFieldsMap()));
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
        } else if (value instanceof Integer) {
            return Value.newBuilder().setNumberValue(((Integer) value).doubleValue()).build();
        } else if (value instanceof Double) {
            return Value.newBuilder().setNumberValue((Double) value).build();
        } else if (value instanceof Boolean) {
            return Value.newBuilder().setBoolValue((Boolean) value).build();
        } else if (value instanceof HashMap) {
            return Value.newBuilder().setStructValue(
                    toProtoMap(
                            new ObjectMapper().convertValue(value, new TypeReference<>() {})
                    )).build();
        } else if (value instanceof List) {
            // TODO: There is a bug in here.
            return Value
                    .newBuilder()
                    .setListValue(
                            ListValue
                                    .newBuilder()
                                    .addAllValues(
                                            ((List)value)
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
