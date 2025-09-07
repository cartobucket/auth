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

package com.cartobucket.auth.postgres.client.services;

import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.domain.SchemaValidation;
import com.cartobucket.auth.data.domain.User;
import com.cartobucket.auth.data.domain.Metadata;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound;
import com.cartobucket.auth.postgres.client.repositories.EventRepository;
import com.cartobucket.auth.postgres.client.repositories.ProfileRepository;
import com.cartobucket.auth.postgres.client.repositories.SchemaRepository;
import com.cartobucket.auth.postgres.client.repositories.UserRepository;
import com.cartobucket.auth.postgres.client.entities.EventType;
import com.cartobucket.auth.postgres.client.entities.mappers.ProfileMapper;
import com.cartobucket.auth.postgres.client.entities.mappers.UserMapper;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
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
    final SchemaService schemaService;
    public UserService(
            EventRepository eventRepository,
            UserRepository userRepository,
            ProfileRepository profileRepository,
            SchemaService schemaService
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.schemaService = schemaService;
    }

    @Override
    @Transactional
    public List<User> query(UserQuery query) {
        var queryParameters = new Parameters();

        // Next, filter by user ids
        if (!query.userIds().isEmpty()) {
            queryParameters.and("userIds", query.userIds());
        }
        // Next, filter by emails
        if (!query.emails().isEmpty()) {
            queryParameters.and("emails", query.emails());
        }
        // Next, filter by identifiers
        if (!query.identifiers().isEmpty()) {
            query.identifiers()
                    // TODO: This is probably wrong becuase it needs to be grouped
                    .forEach(identifier -> {
                        queryParameters.and("metadata.identifiers.system", identifier.getSystem());
                        queryParameters.and("metadata.identifiers.system", identifier.getValue());
                    });
            queryParameters.and("identifiers", query.identifiers());
        }
        // Next, filter by validations
        if (!query.schemaValidations().isEmpty()) {
            queryParameters.and("validations", query.schemaValidations());
        }
        final var _map = queryParameters.map();

        return userRepository
                .find("authorizationServerId in ?1", query.authorizationServerIds())
                .range(query.page().offset(), query.page().limit())
                .stream()
                .map(UserMapper::from)
                .toList();
    }

    @Override
    @Transactional
    public List<User> getUsers(final List<UUID> authorizationServerIds, com.cartobucket.auth.data.domain.Page page) {
        if (!authorizationServerIds.isEmpty()) {
            return userRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(UserMapper::from)
                    .toList();
        } else {
            return userRepository.findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
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
        
        // Validate profile against OIDC schema and store validation results in user metadata
        validateProfileAndUpdateUserMetadata(user, profile);
        
        var _user = UserMapper.to(user);
        userRepository.persist(_user);
        if (user.getPassword() != null) {
            setPassword(UserMapper.from(_user), user.getPassword());
        }

        profile.setCreatedOn(OffsetDateTime.now());
        profile.setUpdatedOn(OffsetDateTime.now());
        profile.setProfileType(ProfileType.User);
        profile.setResource(_user.getId());
        profile.setAuthorizationServerId(user.getAuthorizationServerId());
        
        var _profile = ProfileMapper.to(profile);
        profileRepository.persist(_profile);
        var pair = Pair.Companion.create(UserMapper.from(_user), ProfileMapper.from(_profile));
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
                Pair.Companion.create(UserMapper.from(user), ProfileMapper.from(profile)),
                EventType.DELETE
        );
    }

    @Override
    @Transactional
    public Pair<User, Profile> getUser(final UUID userId) throws UserNotFound, ProfileNotFound {
        final var user = userRepository
                .findByIdOptional(userId)
                .map(UserMapper::from)
                .orElseThrow(UserNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);
        return Pair.Companion.create(user, profile);
    }

    @Override
    @Transactional
    public Pair<User, Profile> getUser(String username) throws UserNotFound, ProfileNotFound {
        final var user = userRepository
                .findByUsername(username)
                .map(UserMapper::from)
                .orElseThrow(UserNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(user.getId(), ProfileType.User)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);
        return Pair.Companion.create(user, profile);
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
                .findByResourceAndProfileType(userId, ProfileType.User)
                .orElseThrow(ProfileNotFound::new);
        _profile.setUpdatedOn(OffsetDateTime.now());
        _profile.setProfile(profile.getProfile());
        profileRepository.persist(_profile);

        final var pair = Pair.Companion.create(UserMapper.from(_user), ProfileMapper.from(_profile));
        eventRepository.createUserProfileEvent(pair, EventType.UPDATE);
        return pair;
    }

    @Override
    @Transactional
    public void setPassword(User user, String password) {
        // TODO: This config should be pulled from the Authorization Server
        final var encoder = new BCryptPasswordEncoder();
        final var passwordHash = encoder.encode(password);
        var _user = userRepository
                .findByIdOptional(user.getId())
                .orElseThrow(UserNotFound::new);
        _user.setPasswordHash(passwordHash);
        _user.setUpdatedOn(OffsetDateTime.now());
        userRepository.persist(_user);
    }

    @Override
    @Transactional
    public boolean validatePassword(UUID userId, String password) {
        final var user = userRepository
                .findByIdOptional(userId)
                .orElseThrow();
        return new BCryptPasswordEncoder().matches(password, user.getPasswordHash());
    }

    private void validateProfileAndUpdateUserMetadata(User user, Profile profile) {
        try {
            // Find the OIDC UserInfo claims schema for this authorization server
            var oidcSchema = schemaService.getSchemaByNameAndAuthorizationServerId(
                "oidc-userinfo-claims", user.getAuthorizationServerId());
                
            if (oidcSchema != null) {
                // Validate the profile against the schema
                var validationErrors = schemaService.validateProfileAgainstSchema(profile, oidcSchema);
                
                // Create schema validation result
                var schemaValidation = new SchemaValidation();
                schemaValidation.setSchemaId(oidcSchema.getId());
                schemaValidation.setValid(validationErrors.isEmpty());
                schemaValidation.setValidatedOn(OffsetDateTime.now());
                
                // Initialize user metadata if needed
                if (user.getMetadata() == null) {
                    user.setMetadata(new Metadata());
                }
                
                // Initialize schema validations list if needed
                if (user.getMetadata().getSchemaValidations() == null) {
                    user.getMetadata().setSchemaValidations(new java.util.ArrayList<>());
                } else {
                    // Make sure we have a mutable list
                    user.getMetadata().setSchemaValidations(
                        new java.util.ArrayList<>(user.getMetadata().getSchemaValidations())
                    );
                }
                
                // Remove any existing validation for this schema
                user.getMetadata().getSchemaValidations().removeIf(
                    sv -> oidcSchema.getId().equals(sv.getSchemaId())
                );
                
                // Add the new validation result
                user.getMetadata().getSchemaValidations().add(schemaValidation);
            }
        } catch (Exception e) {
            // Log warning but don't fail user creation if validation fails
            System.err.println("Warning: Could not validate profile against OIDC schema: " + e.getMessage());
        }
    }
}
