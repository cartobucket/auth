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

package com.cartobucket.auth.rpc.server.services;

import com.cartobucket.auth.data.collections.Pair;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound;
import com.cartobucket.auth.rpc.server.entities.EventType;
import com.cartobucket.auth.rpc.server.entities.mappers.ProfileMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.UserMapper;
import com.cartobucket.auth.rpc.server.repositories.EventRepository;
import com.cartobucket.auth.rpc.server.repositories.ProfileRepository;
import com.cartobucket.auth.rpc.server.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService implements com.cartobucket.auth.data.services.UserService {
    final EventRepository eventRepository;
    final UserRepository userRepository;
    final ProfileRepository profileRepository;
    public UserService(
            EventRepository eventRepository,
            UserRepository userRepository,
            ProfileRepository profileRepository
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public List<User> getUsers(final List<UUID> authorizationServerIds) {
        if (!authorizationServerIds.isEmpty()) {
            return userRepository
                    .findAllByAuthorizationServerIdIn(authorizationServerIds)
                    .stream()
                    .map(UserMapper::from)
                    .toList();
        } else {
            return userRepository.findAll()
                    .stream()
                    .map(UserMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public Pair<User, Profile> createUser(final Pair<User, Profile> userProfilePair) {
        final var user = userProfilePair.getLeft();
        final var profile = userProfilePair.getRight();
        user.setCreatedOn(OffsetDateTime.now());
        user.setUpdatedOn(OffsetDateTime.now());
        var _user = UserMapper.to(user);
        userRepository.persist(_user);

        profile.setCreatedOn(OffsetDateTime.now());
        profile.setUpdatedOn(OffsetDateTime.now());
        profile.setProfileType(ProfileType.User);
        profile.setResource(_user.getId());
        profile.setAuthorizationServerId(user.getAuthorizationServerId());
        var _profile = ProfileMapper.to(profile);
        profileRepository.persist(_profile);
        var pair = Pair.create(UserMapper.from(_user), ProfileMapper.from(_profile));
        eventRepository.createUserProfileEvent(pair, EventType.CREATE);
        return pair;
    }

    @Override
    @Transactional
    public void deleteUser(final UUID userId) {
        final var user = userRepository
                .findByIdOptional(userId)
                .orElseThrow(UserNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
                .orElseThrow(ProfileNotFound::new);
        userRepository.delete(user);
        profileRepository.delete(profile);
        eventRepository.createUserProfileEvent(
                Pair.create(UserMapper.from(user), ProfileMapper.from(profile)),
                EventType.DELETE
        );
    }

    @Override
    public Pair<User, Profile> getUser(final UUID userId) throws UserNotFound, ProfileNotFound {
        final var user = userRepository
                .findByIdOptional(userId)
                .map(UserMapper::from)
                .orElseThrow(UserNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);
        return Pair.create(user, profile);
    }

    @Override
    public Pair<User, Profile> getUser(String username) throws UserNotFound, ProfileNotFound {
        final var user = userRepository
                .findByUsername(username)
                .map(UserMapper::from)
                .orElseThrow(UserNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(user.getId(), ProfileType.User)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);
        return Pair.create(user, profile);
    }

    @Override
    @Transactional
    public Pair<User, Profile> updateUser(final UUID userId, final Pair<User, Profile> userProfilePair) throws UserNotFound, ProfileNotFound {
        final var user = userProfilePair.getLeft();
        final var profile = userProfilePair.getRight();

        var _user = userRepository
                .findByIdOptional(userId)
                .orElseThrow(UserNotFound::new);

        _user.setEmail(user.getEmail());
        _user.setUsername(user.getUsername());
        _user.setUpdatedOn(OffsetDateTime.now());
        userRepository.persist(_user);

        var _profile = profileRepository
                .findByResourceAndProfileType(user.getId(), ProfileType.User)
                .orElseThrow(ProfileNotFound::new);
        _profile.setUpdatedOn(OffsetDateTime.now());
        _profile.setProfile(profile.getProfile());
        profileRepository.persist(_profile);

        final var pair = Pair.create(UserMapper.from(_user), ProfileMapper.from(_profile));
        eventRepository.createUserProfileEvent(pair, EventType.UPDATE);
        return pair;
    }

    @Override
    public void setPassword(User user, String password) {
        // TODO: This config should be pulled from the Authorization Server
        final var encoder = new BCryptPasswordEncoder();
        final var passwordHash = encoder.encode(password);
        user.setPassword(passwordHash);
        user.setUpdatedOn(OffsetDateTime.now());
        userRepository.persist(UserMapper.to(user));
    }

    @Override
    public boolean validatePassword(UUID userId, String password) {
        final var user = userRepository
                .findByIdOptional(userId)
                .orElseThrow();
        return new BCryptPasswordEncoder().matches(password, user.getPasswordHash());
    }
}
