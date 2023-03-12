package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.exceptions.notfound.UserNotFound;
import com.cartobucket.auth.model.generated.UserRequest;
import com.cartobucket.auth.model.generated.UserRequestFilter;
import com.cartobucket.auth.model.generated.UserResponse;
import com.cartobucket.auth.model.generated.UsersResponse;
import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.models.mappers.ProfileMapper;
import com.cartobucket.auth.models.mappers.UserMapper;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

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
    public UsersResponse getUsers(UserRequestFilter filter) {
        var usersResponse = new UsersResponse();
        usersResponse.setUsers(
                userRepository
                        .findAllByAuthorizationServerIdIn(filter.getAuthorizationServerIds())
                        .stream()
                        .map(UserMapper::toResponse)
                        .toList());
        return usersResponse;
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        var user = UserMapper.from(userRequest);
        user.setCreatedOn(OffsetDateTime.now());
        user.setUpdatedOn(OffsetDateTime.now());
        user = userRepository.save(user);

        var profile = ProfileMapper.toProfile(user, (Map<String, Object>) userRequest.getProfile());
        profile.setCreatedOn(OffsetDateTime.now());
        profile.setUpdatedOn(OffsetDateTime.now());
        profile = profileRepository.save(profile);
        return UserMapper.toResponseWithProfile(user, profile);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        profileRepository.deleteByResourceIdAndProfileType(userId, ProfileType.User);
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse getUser(UUID userId) {
        return UserMapper.toResponseWithProfile(
                userRepository
                        .findById(userId)
                        .orElseThrow(UserNotFound::new),
                profileRepository
                        .findByResourceAndProfileType(userId, ProfileType.User)
                        .orElseThrow(ProfileNotFound::new)
        );
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        final var user = userRepository
                .findById(userId)
                .orElseThrow(UserNotFound::new);

        var _user = UserMapper.from(userRequest);
        _user.setId(user.getId());
        _user.setAuthorizationServerId(user.getAuthorizationServerId());
        _user.setUpdatedOn(OffsetDateTime.now());
        _user = userRepository.save(_user);

        var profile = profileRepository
                .findByResourceAndProfileType(user.getId(), ProfileType.User)
                .orElseThrow(ProfileNotFound::new);
        profile.setUpdatedOn(OffsetDateTime.now());
        profile = profileRepository.save(profile);

        return UserMapper.toResponseWithProfile(_user, profile);
    }
}
