package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.User;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.UUID;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final ProfileRepository profileRepository;

    public UserServiceImpl(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public User createUser(User user, Profile profile) {
        var userWithProfile = new User();
        userWithProfile.setAuthorizationServerId(UUID.fromString("4b36afc8-5205-49c1-af16-4dc6f96db982"));
        userWithProfile.setCreatedOn(OffsetDateTime.now());
        userWithProfile.setUpdatedOn(OffsetDateTime.now());
        userWithProfile.setEmail("bryce@cartobucket.com");
        userWithProfile.setUsername("bryce@cartobucket.com");
        userWithProfile.setPasswordHash(new BCryptPasswordEncoder().encode("maddog"));

        userRepository.save(userWithProfile);

        var _profile = new Profile();
        _profile.setAuthorizationServerId(UUID.fromString("4b36afc8-5205-49c1-af16-4dc6f96db982"));
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
