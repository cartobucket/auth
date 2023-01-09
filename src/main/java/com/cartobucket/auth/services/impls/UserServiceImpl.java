package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.UserRequest;
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
import jakarta.ws.rs.NotFoundException;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
    public UsersResponse getUsers() {
        var users = StreamSupport
                .stream(userRepository.findAll().spliterator(), false)
                .map(UserMapper::toResponse)
                .toList();
        var usersResponse = new UsersResponse();
        usersResponse.setUsers(users);
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
        final var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }

        return UserMapper.toResponseWithProfile(
                user.get(),
                profileRepository.findByResourceAndProfileType(user.get().getId(), ProfileType.User)
        );
    }

    @Override
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        final var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }

        var _user = UserMapper.from(userRequest);
        _user.setId(user.get().getId());
        _user.setAuthorizationServerId(user.get().getAuthorizationServerId());
        _user.setUpdatedOn(OffsetDateTime.now());
        _user = userRepository.save(_user);

        var profile = profileRepository.findByResourceAndProfileType(user.get().getId(), ProfileType.User);
        profile.setUpdatedOn(OffsetDateTime.now());
        profile = profileRepository.save(profile);

        return UserMapper.toResponseWithProfile(_user, profile);
    }
}
