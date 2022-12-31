package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.User;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.OffsetDateTime;
import java.util.HashMap;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final ProfileRepository profileRepository;
    final AuthorizationServerService authorizationServerService;
    public UserServiceImpl(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            AuthorizationServerService authorizationServerService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public User createUser(User user, Profile profile, String password) {
        var userWithProfile = new User();
        userWithProfile.setAuthorizationServerId(authorizationServerService.getDefaultAuthorizationServer().getId());
        userWithProfile.setCreatedOn(OffsetDateTime.now());
        userWithProfile.setUpdatedOn(OffsetDateTime.now());
        userWithProfile.setEmail(user.getEmail());
        userWithProfile.setUsername(user.getUsername());
        userWithProfile.setPasswordHash(new BCryptPasswordEncoder().encode(password));

        userRepository.save(userWithProfile);

        var _profile = new Profile();
        _profile.setAuthorizationServerId(authorizationServerService.getDefaultAuthorizationServer().getId());
        _profile.setResource(userWithProfile.getId());
        _profile.setProfile(new HashMap<>());
        _profile.setProfileType(ProfileType.User);
        _profile.setCreatedOn(OffsetDateTime.now());
        _profile.setUpdatedOn(OffsetDateTime.now());
        user.setProfile(_profile);
        profileRepository.save(_profile);
        return user;
    }
}
