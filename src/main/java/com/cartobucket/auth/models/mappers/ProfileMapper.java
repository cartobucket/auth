package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.models.User;

import java.util.Map;

public class ProfileMapper {
    public static Profile toProfile(User user, Map<String, Object> profile) {
        var _profile = new Profile();
        _profile.setProfileType(ProfileType.User);
        _profile.setProfile(profile);
        _profile.setResource(user.getId());
        _profile.setAuthorizationServerId(user.getAuthorizationServerId());
        return _profile;
    }
}
