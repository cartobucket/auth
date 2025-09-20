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

package com.cartobucket.auth.postgres.client.services

import com.cartobucket.auth.data.domain.Metadata
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.ProfileType
import com.cartobucket.auth.data.domain.SchemaValidation
import com.cartobucket.auth.data.domain.User
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound
import com.cartobucket.auth.postgres.client.entities.EventType
import com.cartobucket.auth.postgres.client.entities.mappers.ProfileMapper
import com.cartobucket.auth.postgres.client.entities.mappers.UserMapper
import com.cartobucket.auth.postgres.client.repositories.EventRepository
import com.cartobucket.auth.postgres.client.repositories.ProfileRepository
import com.cartobucket.auth.postgres.client.repositories.UserRepository
import io.quarkus.panache.common.Parameters
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.OffsetDateTime
import java.util.ArrayList
import java.util.UUID

private typealias UserEntity = com.cartobucket.auth.postgres.client.entities.User
private typealias UserQuery = io.quarkus.hibernate.orm.panache.PanacheQuery<UserEntity>

@ApplicationScoped
class UserService(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val schemaService: SchemaService,
) : com.cartobucket.auth.data.services.UserService {
    companion object {
        private val LOG = Logger.getLogger(UserService::class.java)
    }

    @Override
    @Transactional
    override fun query(query: com.cartobucket.auth.data.services.UserService.UserQuery): List<User> {
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
            query
                .identifiers()
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
        val parameterMap = queryParameters.map()

        val userQuery: UserQuery =
            userRepository
                .find(
                    "authorizationServerId in ?1",
                    query.authorizationServerIds(),
                )
        val rangedQuery: UserQuery =
            userQuery
                .range(
                    query.page().offset(),
                    query.page().limit,
                )
        val entityList: List<UserEntity> = rangedQuery.list()
        return entityList.map { user -> UserMapper.from(user) }
    }

    @Override
    @Transactional
    override fun getUsers(
        authorizationServerIds: List<UUID>,
        page: Page,
    ): List<User> =
        if (authorizationServerIds.isNotEmpty()) {
            val userQuery: UserQuery =
                userRepository
                    .find(
                        "authorizationServerId in ?1",
                        Sort.descending("createdOn"),
                        authorizationServerIds,
                    )
            val rangedQuery: UserQuery =
                userQuery
                    .range(
                        page.offset(),
                        page.limit,
                    )
            val entityList: List<UserEntity> = rangedQuery.list()
            entityList.map { user -> UserMapper.from(user) }
        } else {
            val userQuery: UserQuery =
                userRepository
                    .findAll(
                        Sort.descending("createdOn"),
                    )
            val rangedQuery: UserQuery =
                userQuery
                    .range(
                        page.offset(),
                        page.limit,
                    )
            val entityList: List<UserEntity> = rangedQuery.list()
            entityList.map { user -> UserMapper.from(user) }
        }

    @Override
    @Transactional
    override fun createUser(userProfilePair: Pair<User, Profile>): Pair<User, Profile> {
        val user = userProfilePair.left ?: throw UserNotFound()
        val profile = userProfilePair.right ?: throw ProfileNotFound()
        user.createdOn = OffsetDateTime.now()
        user.updatedOn = OffsetDateTime.now()

        // Validate profile against OIDC schema and store validation results in user metadata
        validateProfileAndUpdateUserMetadata(user, profile)

        val entityUser = UserMapper.to(user)
        userRepository.persist(entityUser)
        if (user.password != null) {
            setPassword(UserMapper.from(entityUser), user.password!!)
        }

        profile.createdOn = OffsetDateTime.now()
        profile.updatedOn = OffsetDateTime.now()
        profile.profileType = ProfileType.User
        profile.resource = entityUser.id
        profile.authorizationServerId = user.authorizationServerId

        val entityProfile = ProfileMapper.to(profile)
        profileRepository.persist(entityProfile)
        val pair = Pair.create(UserMapper.from(entityUser), ProfileMapper.from(entityProfile))
        eventRepository.createUserProfileEvent(pair, EventType.CREATE)
        return pair
    }

    @Override
    @Transactional
    override fun deleteUser(userId: UUID) {
        val user =
            userRepository
                .findByIdOptional(userId)
                .orElseThrow { UserNotFound() }
        val profile =
            profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
                .orElseThrow { ProfileNotFound() }
        userRepository.delete(user)
        profileRepository.delete(profile)
        eventRepository.createUserProfileEvent(
            Pair.create(UserMapper.from(user), ProfileMapper.from(profile)),
            EventType.DELETE,
        )
    }

    @Override
    @Transactional
    @Throws(UserNotFound::class, ProfileNotFound::class)
    override fun getUser(userId: UUID): Pair<User, Profile> {
        val user =
            userRepository
                .findByIdOptional(userId)
                .map { UserMapper.from(it) }
                .orElseThrow { UserNotFound() }
        val profile =
            profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
                .map { ProfileMapper.from(it) }
                .orElseThrow { ProfileNotFound() }
        return Pair.create(user, profile)
    }

    @Override
    @Transactional
    @Throws(UserNotFound::class, ProfileNotFound::class)
    override fun getUser(username: String): Pair<User, Profile> {
        val user =
            userRepository
                .findByUsername(username)
                .map { UserMapper.from(it) }
                .orElseThrow { UserNotFound() }
        val profile =
            profileRepository
                .findByResourceAndProfileType(user.id ?: throw UserNotFound(), ProfileType.User)
                .map { ProfileMapper.from(it) }
                .orElseThrow { ProfileNotFound() }
        return Pair.create(user, profile)
    }

    @Override
    @Transactional
    @Throws(UserNotFound::class, ProfileNotFound::class)
    override fun updateUser(
        userId: UUID,
        userProfilePair: Pair<User, Profile>,
    ): Pair<User, Profile> {
        val user = userProfilePair.left ?: throw UserNotFound()
        val profile = userProfilePair.right ?: throw ProfileNotFound()

        val entityUser =
            userRepository
                .findByIdOptional(userId)
                .orElseThrow { UserNotFound() }

        entityUser.email = user.email
        entityUser.username = user.username
        entityUser.updatedOn = OffsetDateTime.now()
        userRepository.persist(entityUser)

        val entityProfile =
            profileRepository
                .findByResourceAndProfileType(userId, ProfileType.User)
                .orElseThrow { ProfileNotFound() }
        entityProfile.updatedOn = OffsetDateTime.now()
        entityProfile.profile = profile.profile?.mapValues { it.value as Any? }
        profileRepository.persist(entityProfile)

        val pair = Pair.create(UserMapper.from(entityUser), ProfileMapper.from(entityProfile))
        eventRepository.createUserProfileEvent(pair, EventType.UPDATE)
        return pair
    }

    @Override
    @Transactional
    override fun setPassword(
        user: User,
        password: String,
    ) {
        // TODO: This config should be pulled from the Authorization Server
        val encoder = BCryptPasswordEncoder()
        val passwordHash = encoder.encode(password)
        val entityUser =
            userRepository
                .findByIdOptional(user.id ?: throw UserNotFound())
                .orElseThrow { UserNotFound() }
        entityUser.passwordHash = passwordHash
        entityUser.updatedOn = OffsetDateTime.now()
        userRepository.persist(entityUser)
    }

    @Override
    @Transactional
    override fun validatePassword(
        userId: UUID,
        password: String,
    ): Boolean {
        val user =
            userRepository
                .findByIdOptional(userId)
                .orElseThrow { UserNotFound() }
        return BCryptPasswordEncoder().matches(password, user.passwordHash ?: return false)
    }

    private fun validateProfileAndUpdateUserMetadata(
        user: User,
        profile: Profile,
    ) {
        try {
            // Find the OIDC UserInfo claims schema for this authorization server
            val oidcSchema =
                schemaService.getSchemaByNameAndAuthorizationServerId(
                    "oidc-userinfo-claims",
                    user.authorizationServerId ?: return,
                )

            if (oidcSchema != null) {
                // Validate the profile against the schema
                val validationErrors = schemaService.validateProfileAgainstSchema(profile, oidcSchema)

                // Create schema validation result
                val schemaValidation =
                    SchemaValidation().apply {
                        schemaId = oidcSchema.id
                        isValid = validationErrors.isEmpty()
                        validatedOn = OffsetDateTime.now()
                    }

                // Initialize user metadata if needed
                if (user.metadata == null) {
                    user.metadata = Metadata()
                }

                // Initialize schema validations list if needed
                if (user.metadata!!.schemaValidations == null) {
                    user.metadata!!.schemaValidations = mutableListOf()
                } else {
                    // Make sure we have a mutable list
                    user.metadata!!.schemaValidations = ArrayList(user.metadata!!.schemaValidations!!)
                }

                // Remove any existing validation for this schema
                (user.metadata!!.schemaValidations!! as MutableList).removeIf { sv ->
                    oidcSchema.id == sv?.schemaId
                }

                // Add the new validation result
                (user.metadata!!.schemaValidations!! as MutableList).add(schemaValidation)
            }
        } catch (e: Exception) {
            // Log warning but don't fail user creation if validation fails
            LOG.warn("Could not validate profile against OIDC schema: ${e.message}", e)
        }
    }
}
