package com.cartobucket.auth.data.services.grpc.mappers;

import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.google.protobuf.Struct;

import java.util.Collections;

public class ProfileMapper {
    public static Profile toProfile(Struct profile, ProfileType profileType) {
        var _profile = new Profile();
        _profile.setProfileType(profileType);
            _profile.setProfile(Profile.fromProtoMap(profile.getFieldsMap()));
        return _profile;
    }


}
