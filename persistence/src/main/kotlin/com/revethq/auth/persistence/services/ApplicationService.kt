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

import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.domain.Pair
import com.revethq.auth.core.domain.Application
import com.revethq.auth.core.domain.ApplicationSecret
import com.revethq.core.Metadata
import com.revethq.auth.core.domain.Profile
import com.revethq.iam.user.domain.ProfileType
import com.revethq.core.SchemaValidation
import com.revethq.auth.core.services.SchemaService
import com.revethq.auth.core.exceptions.badrequests.ApplicationSecretNoApplicationBadData
import com.revethq.auth.core.exceptions.notfound.ApplicationNotFound
import com.revethq.auth.core.exceptions.notfound.ApplicationSecretNotFound
import com.revethq.auth.core.exceptions.notfound.ProfileNotFound
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.persistence.repositories.*
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.ScopeReference
import com.revethq.auth.persistence.entities.mappers.ApplicationMapper
import com.revethq.auth.persistence.entities.mappers.ApplicationSecretMapper
import com.revethq.auth.persistence.entities.mappers.ProfileMapper
import io.quarkus.narayana.jta.QuarkusTransaction
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val applicationSecretRepository: ApplicationSecretRepository,
    private val eventRepository: EventRepository,
    private val profileRepository: ProfileRepository,
    private val scopeReferenceRepository: ScopeReferenceRepository,
    private val scopeService: ScopeService,
    private val schemaService: SchemaService
) : com.revethq.auth.core.services.ApplicationService {

    companion object {
        private val LOG = Logger.getLogger(ApplicationService::class.java)
    }

    @Transactional
    @Throws(ApplicationNotFound::class)
    override fun deleteApplication(applicationId: UUID) {
        val application = applicationRepository
            .findByIdOptional(applicationId)
            .orElseThrow { ApplicationNotFound() }
        val profile = profileRepository
            .findByResourceAndProfileType(applicationId, ProfileType.Application)
            .orElseThrow { ProfileNotFound() }
        applicationRepository.delete(application)
        profileRepository.delete(profile)
        eventRepository.createApplicationProfileEvent(
            Pair.create(ApplicationMapper.from(application), ProfileMapper.from(profile)),
            EventType.DELETE
        )
    }

    @Transactional
    @Throws(ApplicationNotFound::class, ProfileNotFound::class)
    override fun getApplication(applicationId: UUID): Pair<Application, Profile> {
        val application = applicationRepository
            .findByIdOptional(applicationId)
            .map { ApplicationMapper.from(it) }
            .orElseThrow { ApplicationNotFound() }
        val profile = profileRepository
            .findByResourceAndProfileType(applicationId, ProfileType.Application)
            .map { ProfileMapper.from(it) }
            .orElseThrow { ProfileNotFound() }
        return Pair.create(application, profile)
    }

    override fun createApplication(application: Application, profile: Profile): Pair<Application, Profile> {
        QuarkusTransaction.begin()

        // Generate ID for application since it doesn't use @GeneratedValue
        if (application.id == null) {
            application.id = UUID.randomUUID()
        }

        application.createdOn = OffsetDateTime.now()
        application.updatedOn = OffsetDateTime.now()

        // Validate profile against OIDC schema and store validation results in application metadata
        validateProfileAndUpdateApplicationMetadata(application, profile)

        val _application = ApplicationMapper.to(application)
        applicationRepository.persist(_application)

        profile.resource = _application.id
        profile.profileType = ProfileType.Application
        profile.authorizationServerId = _application.authorizationServerId
        profile.createdOn = OffsetDateTime.now()
        profile.updatedOn = OffsetDateTime.now()
        val _profile = ProfileMapper.to(profile)
        profileRepository.persist(_profile)

        for (scope in application.scopes.orEmpty()) {
            val scopeReference = ScopeReference()
            scopeReference.scopeId = scope.id
            scopeReference.resourceId = _application.id
            scopeReference.scopeReferenceType = ScopeReference.ScopeReferenceType.APPLICATION
            scopeReferenceRepository.persist(scopeReference)
        }
        QuarkusTransaction.commit()

        QuarkusTransaction.begin()
        val applicationProfilePair = Pair.create(
            ApplicationMapper.from(applicationRepository.findById(_application.id)),
            ProfileMapper.from(_profile)
        )
        // TODO: This second transaction is wrong and not really needed. Have to save for the scopes to get added,
        //  but that should not be that important for the even.
        eventRepository.createApplicationProfileEvent(applicationProfilePair, EventType.CREATE)
        QuarkusTransaction.commit()
        return applicationProfilePair
    }

    @Transactional
    @Throws(ApplicationNotFound::class)
    override fun getApplicationSecrets(applicationId: List<UUID>): List<ApplicationSecret> {
        if (applicationId.isEmpty()) {
            val secrets: List<com.revethq.auth.persistence.entities.ApplicationSecret> = applicationSecretRepository.findAll().list()
            return secrets.map { ApplicationSecretMapper.from(it) }
        }

        return applicationSecretRepository.findByApplicationIdIn(applicationId)
            .map { ApplicationSecretMapper.from(it) }
    }

    @Throws(ApplicationNotFound::class)
    override fun createApplicationSecret(applicationSecret: ApplicationSecret): ApplicationSecret {
        QuarkusTransaction.begin()
        val application = applicationRepository
            .findByIdOptional(applicationSecret.applicationId)
            .orElseThrow { ApplicationNotFound() }

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val secret = BigInteger(1, SecureRandom().generateSeed(120)).toString(16)
        val secretHash = BigInteger(1, messageDigest.digest(secret.toByteArray())).toString(16)
        applicationSecret.applicationSecret = secret
        applicationSecret.applicationSecretHash = secretHash
        applicationSecret.authorizationServerId = application.authorizationServerId
        applicationSecret.createdOn = OffsetDateTime.now()
        val _applicationSecret = ApplicationSecretMapper.to(applicationSecret)
        applicationSecretRepository.persist(_applicationSecret)
        val applicationSecretReferences = applicationSecret.scopes.orEmpty()
            .map { scope ->
                val scopeReference = ScopeReference()
                scopeReference.scopeId = scope.id
                scopeReference.resourceId = _applicationSecret.id
                scopeReference.scopeReferenceType = ScopeReference.ScopeReferenceType.APPLICATION_SECRET
                scopeReference
            }
        scopeReferenceRepository.persist(applicationSecretReferences)
        QuarkusTransaction.commit()

        // Refresh the secret to get the new scopes and add the actual secret string back on.
        val refreshedSecret = applicationSecretRepository.findById(_applicationSecret.id)
        refreshedSecret.applicationSecret = secret
        return ApplicationSecretMapper.from(refreshedSecret)
    }

    @Transactional
    @Throws(ApplicationSecretNoApplicationBadData::class, ApplicationSecretNotFound::class)
    override fun deleteApplicationSecret(secretId: UUID) {
        val secret = applicationSecretRepository
            .findByIdOptional(secretId)
            .orElseThrow { ApplicationSecretNotFound() }
        applicationSecretRepository.delete(secret)
    }

    @Transactional
    @Suppress("UNCHECKED_CAST")
    override fun getApplications(authorizationServerIds: List<UUID>, page: Page): List<Application> {
        val entities: List<com.revethq.auth.persistence.entities.Application>
        if (authorizationServerIds.isEmpty()) {
            val query: PanacheQuery<com.revethq.auth.persistence.entities.Application> = applicationRepository.findAll(Sort.descending("createdOn"))
            val paged: PanacheQuery<com.revethq.auth.persistence.entities.Application> = query.page(page.offset() / page.limit(), page.limit())
            entities = paged.list()
        } else {
            val query: PanacheQuery<com.revethq.auth.persistence.entities.Application> = applicationRepository.find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
            val paged: PanacheQuery<com.revethq.auth.persistence.entities.Application> = query.page(page.offset() / page.limit(), page.limit())
            entities = paged.list()
        }
        return entities.map { ApplicationMapper.from(it) }
    }

    @Transactional
    override fun isApplicationSecretValid(authorizationServerId: UUID, applicationSecretId: UUID, applicationSecret: String): Boolean {
        try {
            // Find the application secret by ID first
            val applicationSecretOpt = applicationSecretRepository.findByIdOptional(applicationSecretId)
            if (applicationSecretOpt.isEmpty) {
                return false
            }

            // Verify it belongs to the correct authorization server
            if (authorizationServerId != applicationSecretOpt.get().authorizationServerId) {
                return false
            }

            // Hash the provided secret and compare
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val secretHash = BigInteger(1, messageDigest.digest(applicationSecret.toByteArray())).toString(16)

            return secretHash == applicationSecretOpt.get().applicationSecretHash
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Transactional
    override fun getApplicationSecret(applicationSecretId: String): ApplicationSecret {
        return applicationSecretRepository
            .findByIdOptional(UUID.fromString(applicationSecretId))
            .map { ApplicationSecretMapper.from(it) }
            .orElseThrow { RuntimeException("ApplicationSecret not found") }
    }

    private fun validateProfileAndUpdateApplicationMetadata(application: Application, profile: Profile) {
        try {
            // Find the OIDC UserInfo claims schema for this authorization server
            val oidcSchema = application.authorizationServerId?.let {
                schemaService.getSchemaByNameAndAuthorizationServerId("oidc-userinfo-claims", it)
            }

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
                val currentMetadata = application.metadata
                val existingValidations = if (currentMetadata?.schemaValidations != null)
                    ArrayList(currentMetadata.schemaValidations)
                else
                    ArrayList<SchemaValidation>()

                // Remove any existing validation for this schema
                existingValidations.removeIf { sv -> oidcSchema.id == sv.schemaId }

                // Add the new validation result
                existingValidations.add(schemaValidation)

                // Create new metadata with updated schema validations
                val existingMetadata = application.metadata
                val newMetadata = Metadata(
                    existingMetadata?.identifiers,
                    existingValidations,
                    existingMetadata?.properties
                )
                application.metadata = newMetadata
            }
        } catch (e: Exception) {
            // Log warning but don't fail application creation if validation fails
            LOG.warn("Could not validate profile against OIDC schema: ${e.message}", e)
        }
    }
}
