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

import com.cartobucket.auth.rpc.server.entities.mappers.ProfileMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.UserMapper;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.rpc.server.repositories.ProfileRepository;
import com.cartobucket.auth.rpc.server.repositories.UserRepository;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.graalvm.collections.Pair;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class UserService implements com.cartobucket.auth.data.services.UserService {
    final UserRepository userRepository;
    final ProfileRepository profileRepository;
    final AuthorizationServerService authorizationServerService;
    public UserService(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            AuthorizationServerService authorizationServerService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public List<User> getUsers(final List<UUID> authorizationServerIds) {
        if (!authorizationServerIds.isEmpty()) {
            return StreamSupport
                    .stream(
                            userRepository
                                    .findAllByAuthorizationServerIdIn(authorizationServerIds)
                                    .stream()
                                    .spliterator(),
                            false
                    )
                    .map(UserMapper::from)
                    .toList();
        } else {
            return StreamSupport
                    .stream(userRepository.findAll().spliterator(), false)
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
        var _user = UserMapper.from(userRepository.save(UserMapper.to(user)));

        profile.setCreatedOn(OffsetDateTime.now());
        profile.setUpdatedOn(OffsetDateTime.now());
        profile.setResource(_user.getId());
        var _profile = ProfileMapper.from(profileRepository.save(ProfileMapper.to(profile)));
        return Pair.create(_user, _profile);
    }

    @Override
    @Transactional
    public void deleteUser(final UUID userId) {
        profileRepository.deleteByResourceIdAndProfileType(userId, ProfileType.User);
        userRepository.deleteById(userId);
    }

    @Override
    public Pair<User, Profile> getUser(final UUID userId) throws UserNotFound, ProfileNotFound {
        final var user = userRepository
                .findById(userId)
                .map(UserMapper::from)
                .orElseThrow(UserNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
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
                .findById(userId)
                .orElseThrow(UserNotFound::new);

        _user.setId(user.getId());
        _user.setAuthorizationServerId(user.getAuthorizationServerId());
        _user.setUpdatedOn(OffsetDateTime.now());
        _user = userRepository.save(_user);

        var _profile = profileRepository
                .findByResourceAndProfileType(user.getId(), ProfileType.User)
                .orElseThrow(ProfileNotFound::new);
        _profile.setUpdatedOn(OffsetDateTime.now());
        _profile.setProfile(profile.getProfile());
        _profile = profileRepository.save(_profile);

        return Pair.create(UserMapper.from(_user), ProfileMapper.from(_profile));
    }
}
