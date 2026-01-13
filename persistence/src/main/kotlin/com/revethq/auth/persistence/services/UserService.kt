/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.persistence.services

import com.revethq.auth.core.domain.Pair
import com.revethq.auth.core.domain.Profile
import com.revethq.iam.user.domain.ProfileType
import com.revethq.core.SchemaValidation
import com.revethq.auth.core.domain.User
import com.revethq.core.Metadata
import com.revethq.auth.core.exceptions.notfound.ProfileNotFound
import com.revethq.auth.core.exceptions.notfound.UserNotFound
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.ProfileRepository
import com.revethq.auth.persistence.repositories.UserRepository
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.mappers.ProfileMapper
import com.revethq.auth.persistence.entities.mappers.UserMapper
import io.quarkus.panache.common.Parameters
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class UserService(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val schemaService: SchemaService
) : com.revethq.auth.core.services.UserService {

    companion object {
        private val LOG = Logger.getLogger(UserService::class.java)
    }

    @Transactional
    override fun query(query: com.revethq.auth.core.services.UserService.UserQuery): List<User> {
        val queryParameters = Parameters()

        // Next, filter by user ids
        if (query.userIds().isNotEmpty()) {
            queryParameters.and("userIds", query.userIds())
        }
        // Next, filter by emails
        if (query.emails().isNotEmpty()) {
            queryParameters.and("emails", query.emails())
        }
        // Next, filter by identifiers
        if (query.identifiers().isNotEmpty()) {
            query.identifiers()
                // TODO: This is probably wrong because it needs to be grouped
                .forEach { identifier ->
                    queryParameters.and("metadata.identifiers.system", identifier.system)
                    queryParameters.and("metadata.identifiers.system", identifier.value)
                }
            queryParameters.and("identifiers", query.identifiers())
        }
        // Next, filter by validations
        if (query.schemaValidations().isNotEmpty()) {
            queryParameters.and("validations", query.schemaValidations())
        }

        val allEntities: List<com.revethq.auth.persistence.entities.User> =
            userRepository.list("authorizationServerId in ?1", query.authorizationServerIds())
        val page = query.page()
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { UserMapper.from(it) }
    }

    @Transactional
    override fun getUsers(authorizationServerIds: List<UUID>, page: com.revethq.auth.core.domain.Page): List<User> {
        val allEntities: List<com.revethq.auth.persistence.entities.User> = if (authorizationServerIds.isNotEmpty()) {
            userRepository.list("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
        } else {
            userRepository.listAll(Sort.descending("createdOn"))
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { UserMapper.from(it) }
    }

    @Transactional
    override fun createUser(userProfilePair: Pair<User, Profile>): Pair<User, Profile> {
        val user = userProfilePair.left ?: throw IllegalArgumentException("User cannot be null")
        val profile = userProfilePair.right ?: throw IllegalArgumentException("Profile cannot be null")
        user.createdOn = OffsetDateTime.now()
        user.updatedOn = OffsetDateTime.now()

        // Validate profile against OIDC schema and store validation results in user metadata
        validateProfileAndUpdateUserMetadata(user, profile)

        val _user = UserMapper.to(user)
        userRepository.persist(_user)
        if (user.password != null) {
            setPassword(UserMapper.from(_user), user.password!!)
        }

        profile.createdOn = OffsetDateTime.now()
        profile.updatedOn = OffsetDateTime.now()
        profile.profileType = ProfileType.User
        profile.resource = _user.id
        profile.authorizationServerId = user.authorizationServerId

        val _profile = ProfileMapper.to(profile)
        profileRepository.persist(_profile)
        val pair = Pair.create(UserMapper.from(_user), ProfileMapper.from(_profile))
        eventRepository.createUserProfileEvent(pair, EventType.CREATE)
        return pair
    }

    @Transactional
    override fun deleteUser(userId: UUID) {
        val user = userRepository
            .findByIdOptional(userId)
            .orElseThrow { UserNotFound() }
        val profile = profileRepository
            .findByResourceAndProfileType(userId, ProfileType.User)
            .orElseThrow { ProfileNotFound() }
        userRepository.delete(user)
        profileRepository.delete(profile)
        eventRepository.createUserProfileEvent(
            Pair.create(UserMapper.from(user), ProfileMapper.from(profile)),
            EventType.DELETE
        )
    }

    @Transactional
    @Throws(UserNotFound::class, ProfileNotFound::class)
    override fun getUser(userId: UUID): Pair<User, Profile> {
        val user = userRepository
            .findByIdOptional(userId)
            .map { UserMapper.from(it) }
            .orElseThrow { UserNotFound() }
        val profile = profileRepository
            .findByResourceAndProfileType(userId, ProfileType.User)
            .map { ProfileMapper.from(it) }
            .orElseThrow { ProfileNotFound() }
        return Pair.create(user, profile)
    }

    @Transactional
    @Throws(UserNotFound::class, ProfileNotFound::class)
    override fun getUser(username: String): Pair<User, Profile> {
        val user = userRepository
            .findByUsername(username)
            .map { UserMapper.from(it) }
            .orElseThrow { UserNotFound() }
        val userId = user.id ?: throw UserNotFound()
        val profile = profileRepository
            .findByResourceAndProfileType(userId, ProfileType.User)
            .map { ProfileMapper.from(it) }
            .orElseThrow { ProfileNotFound() }
        return Pair.create(user, profile)
    }

    @Transactional
    @Throws(UserNotFound::class, ProfileNotFound::class)
    override fun updateUser(userId: UUID, userProfilePair: Pair<User, Profile>): Pair<User, Profile> {
        val user = userProfilePair.left ?: throw IllegalArgumentException("User cannot be null")
        val profile = userProfilePair.right ?: throw IllegalArgumentException("Profile cannot be null")

        val _user = userRepository
            .findByIdOptional(userId)
            .orElseThrow { UserNotFound() }

        _user.email = user.email
        _user.username = user.username
        _user.updatedOn = OffsetDateTime.now()
        userRepository.persist(_user)

        val _profile = profileRepository
            .findByResourceAndProfileType(userId, ProfileType.User)
            .orElseThrow { ProfileNotFound() }
        _profile.updatedOn = OffsetDateTime.now()
        _profile.profile = profile.profile
        profileRepository.persist(_profile)

        val pair = Pair.create(UserMapper.from(_user), ProfileMapper.from(_profile))
        eventRepository.createUserProfileEvent(pair, EventType.UPDATE)
        return pair
    }

    @Transactional
    override fun setPassword(user: User, password: String) {
        // TODO: This config should be pulled from the Authorization Server
        val encoder = BCryptPasswordEncoder()
        val passwordHash = encoder.encode(password)
        val _user = userRepository
            .findByIdOptional(user.id)
            .orElseThrow { UserNotFound() }
        _user.passwordHash = passwordHash
        _user.updatedOn = OffsetDateTime.now()
        userRepository.persist(_user)
    }

    @Transactional
    override fun validatePassword(userId: UUID, password: String): Boolean {
        val user = userRepository
            .findByIdOptional(userId)
            .orElseThrow { UserNotFound() }
        return BCryptPasswordEncoder().matches(password, user.passwordHash)
    }

    private fun validateProfileAndUpdateUserMetadata(user: User, profile: Profile) {
        try {
            // Find the OIDC UserInfo claims schema for this authorization server
            val authServerId = user.authorizationServerId ?: return
            val oidcSchema = schemaService.getSchemaByNameAndAuthorizationServerId(
                "oidc-userinfo-claims", authServerId
            )

            if (oidcSchema != null) {
                // Validate the profile against the schema
                val validationErrors = schemaService.validateProfileAgainstSchema(profile, oidcSchema)

                // Create schema validation result
                val schemaValidation = SchemaValidation(
                    oidcSchema.id,
                    validationErrors.isEmpty(),
                    OffsetDateTime.now()
                )

                // Build the updated schema validations list
                val currentMetadata = user.metadata
                val existingValidations = if (currentMetadata?.schemaValidations != null)
                    ArrayList(currentMetadata.schemaValidations)
                else
                    ArrayList<SchemaValidation>()

                // Remove any existing validation for this schema
                existingValidations.removeIf { sv -> oidcSchema.id == sv.schemaId }

                // Add the new validation result
                existingValidations.add(schemaValidation)

                // Create new metadata with updated schema validations
                val existingMetadata = user.metadata
                val newMetadata = Metadata(
                    existingMetadata?.identifiers,
                    existingValidations,
                    existingMetadata?.properties
                )
                user.metadata = newMetadata
            }
        } catch (e: Exception) {
            // Log warning but don't fail user creation if validation fails
            LOG.warn("Could not validate profile against OIDC schema: ${e.message}", e)
        }
    }
}
