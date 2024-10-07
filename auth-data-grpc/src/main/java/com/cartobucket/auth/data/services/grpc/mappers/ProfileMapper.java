package com.cartobucket.auth.data.services.grpc.mappers;

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
        return new Profile.Builder()
                .setProfileType(profileType)
                .setProfile(fromProtoMap(profile.getFieldsMap()))
                .build();
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
        return switch (value) {
            case String s -> Value.newBuilder().setStringValue(s).build();
            case Integer i -> Value.newBuilder().setNumberValue(i.doubleValue()).build();
            case Double v -> Value.newBuilder().setNumberValue(v).build();
            case Boolean b -> Value.newBuilder().setBoolValue(b).build();
            case HashMap hashMap -> Value.newBuilder().setStructValue(
                    toProtoMap(
                            new ObjectMapper().convertValue(hashMap, new TypeReference<>() {
                            })
                    )).build();
            case List list ->
                // TODO: There is a bug in here.
                    Value
                            .newBuilder()
                            .setListValue(
                                    ListValue
                                            .newBuilder()
                                            .addAllValues(
                                                    list
                                                            .stream()
                                                            .map(ProfileMapper::fromNative)
                                                            .toList()
                                            )
                            ).build();
            case null, default -> Value.newBuilder().build();
        };
    }

    public static Struct toProtoMap(Map<String, Object> profile) {
        return Struct.newBuilder().putAllFields(
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
    }

}
