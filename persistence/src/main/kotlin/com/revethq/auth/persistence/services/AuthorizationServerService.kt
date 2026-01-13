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

import com.revethq.auth.core.domain.AccessToken
import com.revethq.auth.core.domain.AuthorizationServer
import com.revethq.auth.core.domain.JWK
import com.revethq.core.Metadata
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.domain.Profile
import com.revethq.auth.core.domain.Schema
import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.domain.SigningKey
import com.revethq.auth.core.domain.Template
import com.revethq.auth.core.domain.TemplateTypeEnum
import com.revethq.auth.core.domain.TokenTypeEnum
import com.revethq.auth.core.exceptions.NotAuthorized
import com.revethq.auth.core.exceptions.TokenGenerationException
import com.revethq.auth.core.exceptions.notfound.AuthorizationServerNotFound
import com.revethq.auth.core.exceptions.notfound.ProfileNotFound
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.core.services.SchemaService
import com.revethq.auth.core.services.TemplateService
import jakarta.json.bind.JsonbBuilder
import com.revethq.auth.persistence.repositories.ApplicationSecretRepository
import com.revethq.auth.persistence.repositories.AuthorizationServerRepository
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.RefreshTokenRepository
import com.revethq.auth.persistence.repositories.ScopeRepository
import com.revethq.auth.persistence.repositories.SigningKeyRepository
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.mappers.AuthorizationServerMapper
import com.revethq.auth.persistence.entities.mappers.ProfileMapper
import com.revethq.auth.persistence.entities.mappers.SigningKeyMapper
import com.revethq.auth.persistence.repositories.ProfileRepository
import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.panache.common.Sort
import io.smallrye.jwt.algorithm.SignatureAlgorithm
import io.smallrye.jwt.build.Jwt
import io.smallrye.jwt.util.KeyUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.RSAPublicKey
import java.time.OffsetDateTime
import java.util.Base64
import java.util.Random
import java.util.UUID
import java.util.stream.Collectors

@ApplicationScoped
class AuthorizationServerService(
    private val authorizationServerRepository: AuthorizationServerRepository,
    private val applicationSecretRepository: ApplicationSecretRepository,
    private val eventRepository: EventRepository,
    private val profileRepository: ProfileRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val scopeRepository: ScopeRepository,
    private val signingKeyRepository: SigningKeyRepository,
    private val templateService: TemplateService,
    private val scopeService: ScopeService,
    private val schemaService: SchemaService
) : com.revethq.auth.core.services.AuthorizationServerService {

    companion object {
        private val LOG = Logger.getLogger(AuthorizationServerService::class.java)

        fun generateSigningKey(authorizationServer: AuthorizationServer): SigningKey {
            val signingKey = SigningKey()
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(2048)
            val pair = generator.generateKeyPair()

            val publicKey = StringBuilder()
            publicKey.append("-----BEGIN PUBLIC KEY-----\n")
            publicKey.append(Base64.getMimeEncoder().encodeToString(pair.public.encoded))
            publicKey.append("\n-----END PUBLIC KEY-----\n")
            signingKey.publicKey = publicKey.toString()

            val privateKey = StringBuilder()
            privateKey.append("-----BEGIN PRIVATE KEY-----\n")
            privateKey.append(Base64.getMimeEncoder().encodeToString(pair.private.encoded))
            privateKey.append("\n-----END PRIVATE KEY-----\n")
            signingKey.privateKey = privateKey.toString()

            signingKey.keyType = "RSA"
            signingKey.authorizationServerId = authorizationServer.id
            signingKey.metadata = HashMap()
            signingKey.createdOn = OffsetDateTime.now()
            signingKey.updatedOn = OffsetDateTime.now()

            return signingKey
        }

        private fun buildJwk(key: com.revethq.auth.persistence.entities.SigningKey): JWK {
            val jwk = JWK()

            val publicKey = KeyUtils.decodePublicKey(key.publicKey)
            // Some clients will not accept padding in the base64 encoded string.
            jwk.n = Base64.getUrlEncoder().withoutPadding().encodeToString((publicKey as RSAPublicKey).modulus.toByteArray())
            jwk.e = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.publicExponent.toByteArray())

            jwk.kid = key.id.toString()
            jwk.kty = "RSA"
            jwk.use = "sig"
            jwk.alg = when (key.keyType) {
                else -> "RS256"
            }
            jwk.e = "AQAB"
            jwk.x5c = mutableListOf()
            jwk.x5t = ""
            jwk.x5tHashS256 = ""
            return jwk
        }
    }

    @Transactional
    override fun getJwksForAuthorizationServer(authorizationServerId: UUID): List<JWK> {
        return signingKeyRepository
            .findAllByAuthorizationServerId(authorizationServerId)
            .map { buildJwk(it) }
    }

    @Transactional
    override fun generateClientCredentialsAccessToken(
        authorizationServerId: UUID,
        applicationId: UUID,
        subject: String,
        scopes: List<Scope>,
        expiresInSeconds: Long
    ): AccessToken {
        val authorizationServer = authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .orElseThrow()

        val profile = profileRepository.findByResourceId(applicationId)
            .map { ProfileMapper.from(it) }
            .orElseThrow { ProfileNotFound() }

        val additionalClaims = HashMap<String, Any>()
        additionalClaims["sub"] = subject
        if (scopes.isNotEmpty()) {
            additionalClaims["scope"] = ScopeService.scopeListToScopeString(scopes.mapNotNull { it.name })
        }
        additionalClaims["exp"] = OffsetDateTime.now().plusSeconds(expiresInSeconds).toEpochSecond()

        return buildClientCredentialsAccessToken(authorizationServer, profile, additionalClaims)
    }

    @Transactional
    override fun generateAuthorizationCodeFlowAccessToken(
        authorizationServerId: UUID,
        userId: UUID,
        subject: String,
        clientId: String,
        scopes: List<Scope>,
        expiresInSeconds: Long,
        nonce: String?
    ): AccessToken {
        val authorizationServer = authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .orElseThrow()

        val profile = profileRepository.findByResourceId(userId)
            .map { ProfileMapper.from(it) }
            .orElseThrow { ProfileNotFound() }

        val additionalClaims = HashMap<String, Any>()
        if (nonce != null) {
            additionalClaims["nonce"] = nonce
        }
        additionalClaims["sub"] = subject
        if (scopes.isNotEmpty()) {
            additionalClaims["scope"] = ScopeService.scopeListToScopeString(scopes.mapNotNull { it.name })
        } else {
            additionalClaims["scope"] = "openid"
        }
        additionalClaims["exp"] = OffsetDateTime.now().plusSeconds(expiresInSeconds).toEpochSecond()

        val accessToken = buildAccessTokenResponse(authorizationServer, profile, additionalClaims)

        // Generate refresh token
        val refreshToken = generateRefreshToken()
        val refreshTokenHash = hashRefreshToken(refreshToken)

        // Store refresh token in database
        val refreshTokenEntity = com.revethq.auth.persistence.entities.RefreshToken()
        refreshTokenEntity.tokenHash = refreshTokenHash
        refreshTokenEntity.userId = userId
        refreshTokenEntity.clientId = clientId
        refreshTokenEntity.authorizationServerId = authorizationServerId
        refreshTokenEntity.scopeIds = scopes.mapNotNull { it.id }
        refreshTokenEntity.expiresAt = OffsetDateTime.now().plusDays(30)
        refreshTokenEntity.createdOn = OffsetDateTime.now()
        refreshTokenEntity.isRevoked = false

        refreshTokenRepository.persist(refreshTokenEntity)
        accessToken.refreshToken = refreshToken

        return accessToken
    }

    @Transactional
    override fun refreshAccessToken(authorizationServerId: UUID, refreshToken: String, clientId: String): AccessToken {
        // Hash the incoming refresh token to match against stored hash
        val tokenHash = hashRefreshToken(refreshToken)

        // Find the refresh token in the database
        val storedToken = refreshTokenRepository.findByTokenHash(tokenHash)
            .orElseThrow { NotAuthorized("Invalid or expired refresh token") }

        // Verify the client ID matches
        if (storedToken.clientId != clientId) {
            throw NotAuthorized("Client ID mismatch")
        }

        // Verify the authorization server ID matches
        if (storedToken.authorizationServerId != authorizationServerId) {
            throw NotAuthorized("Authorization server mismatch")
        }

        // Immediately revoke the old refresh token (token rotation)
        storedToken.isRevoked = true
        storedToken.revokedAt = OffsetDateTime.now()
        refreshTokenRepository.persist(storedToken)

        // Get the user profile
        val profile = profileRepository.findByResourceId(storedToken.userId!!)
            .map { ProfileMapper.from(it) }
            .orElseThrow { ProfileNotFound() }

        // Get the authorization server
        val authorizationServer = authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .orElseThrow()

        // Get scopes from stored scope IDs
        val scopes = storedToken.scopeIds?.map { scopeId ->
            try {
                com.revethq.auth.persistence.entities.mappers.ScopeMapper.from(
                    scopeRepository.findByIdOptional(scopeId)
                        .orElseThrow { RuntimeException("Scope not found: $scopeId") }
                )
            } catch (e: Exception) {
                // If scope is deleted, create a basic scope with the ID for backwards compatibility
                val scope = Scope()
                scope.id = scopeId
                scope
            }
        } ?: emptyList()

        // Generate new access token
        val additionalClaims = HashMap<String, Any>()
        additionalClaims["sub"] = storedToken.clientId!!
        additionalClaims["scope"] = ScopeService.scopeListToScopeString(scopes.mapNotNull { it.name })
        additionalClaims["exp"] = OffsetDateTime.now().plusSeconds(authorizationServer.authorizationCodeTokenExpiration ?: 3600L).toEpochSecond()

        val newAccessToken = buildAccessTokenResponse(authorizationServer, profile, additionalClaims)

        // Generate and store a new refresh token (token rotation)
        val newRefreshToken = generateRefreshToken()
        val newRefreshTokenHash = hashRefreshToken(newRefreshToken)

        val newRefreshTokenEntity = com.revethq.auth.persistence.entities.RefreshToken()
        newRefreshTokenEntity.tokenHash = newRefreshTokenHash
        newRefreshTokenEntity.userId = storedToken.userId
        newRefreshTokenEntity.clientId = storedToken.clientId
        newRefreshTokenEntity.authorizationServerId = storedToken.authorizationServerId
        newRefreshTokenEntity.scopeIds = storedToken.scopeIds
        newRefreshTokenEntity.expiresAt = OffsetDateTime.now().plusDays(30) // 30 days expiration
        newRefreshTokenEntity.createdOn = OffsetDateTime.now()
        newRefreshTokenEntity.isRevoked = false

        refreshTokenRepository.persist(newRefreshTokenEntity)
        newAccessToken.refreshToken = newRefreshToken

        return newAccessToken
    }

    @Transactional
    override fun createAuthorizationServer(authorizationServer: AuthorizationServer): AuthorizationServer {
        var mutableAuthorizationServer = authorizationServer
        mutableAuthorizationServer.createdOn = OffsetDateTime.now()
        mutableAuthorizationServer.updatedOn = OffsetDateTime.now()

        val _authorizationServer = AuthorizationServerMapper.to(mutableAuthorizationServer)
        authorizationServerRepository.persist(_authorizationServer)
        mutableAuthorizationServer = AuthorizationServerMapper.from(_authorizationServer)
        eventRepository.createAuthorizationServerEvent(mutableAuthorizationServer, EventType.CREATE)

        // Create the signing keys
        val signingKey = SigningKeyMapper.to(generateSigningKey(mutableAuthorizationServer))
        signingKeyRepository.persist(signingKey)
        eventRepository.createSigningKeyEvent(SigningKeyMapper.from(signingKey), EventType.CREATE)

        // Create the default scopes
        createDefaultScopesForAuthorizationServer(mutableAuthorizationServer)

        // Create the templates
        createDefaultTemplatesForAuthorizationServer(mutableAuthorizationServer)

        // Create the default OIDC schemas
        createDefaultSchemasForAuthorizationServer(mutableAuthorizationServer)

        return mutableAuthorizationServer
    }

    @Transactional
    @Throws(AuthorizationServerNotFound::class)
    override fun getAuthorizationServer(authorizationServerId: UUID): AuthorizationServer {
        return authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .map { AuthorizationServerMapper.from(it) }
            .orElseThrow { AuthorizationServerNotFound() }
    }

    @Transactional
    @Throws(AuthorizationServerNotFound::class)
    override fun updateAuthorizationServer(authorizationServerId: UUID, authorizationServer: AuthorizationServer): AuthorizationServer {
        val _authorizationServer = authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .orElseThrow { AuthorizationServerNotFound() }

        _authorizationServer.createdOn = authorizationServer.createdOn
        _authorizationServer.updatedOn = OffsetDateTime.now()
        _authorizationServer.serverUrl = authorizationServer.serverUrl
        _authorizationServer.audience = authorizationServer.audience
        _authorizationServer.name = authorizationServer.name
        _authorizationServer.authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration
        _authorizationServer.clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration
        authorizationServerRepository.persist(_authorizationServer)
        eventRepository.createAuthorizationServerEvent(
            AuthorizationServerMapper.from(_authorizationServer),
            EventType.UPDATE
        )
        return AuthorizationServerMapper.from(_authorizationServer)
    }

    @Transactional
    override fun getAuthorizationServers(page: Page): List<AuthorizationServer> {
        val allEntities: List<com.revethq.auth.persistence.entities.AuthorizationServer> = authorizationServerRepository.listAll(Sort.descending("createdOn"))
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { AuthorizationServerMapper.from(it) }
    }

    @Transactional
    @Throws(AuthorizationServerNotFound::class)
    override fun deleteAuthorizationServer(authorizationServerId: UUID) {
        val authorizationServer = authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .orElseThrow { AuthorizationServerNotFound() }
        authorizationServerRepository.delete(authorizationServer)
        eventRepository.createAuthorizationServerEvent(
            AuthorizationServerMapper.from(authorizationServer),
            EventType.DELETE
        )
    }

    override fun getSigningKeysForAuthorizationServer(authorizationServerId: UUID): SigningKey {
        val keys = signingKeyRepository.findAllByAuthorizationServerId(authorizationServerId)
        return keys
            .drop(Random().nextInt(keys.size))
            .firstOrNull()
            ?.let { SigningKeyMapper.from(it) }
            ?: throw NoSuchElementException()
    }

    @Transactional
    @Throws(NotAuthorized::class)
    override fun validateJwtForAuthorizationServer(authorizationServerId: UUID, jwt: String): Map<String, Any> {
        val authorizationServer = authorizationServerRepository
            .findByIdOptional(authorizationServerId)
            .orElseThrow()
        val jwks = getJwksForAuthorizationServer(authorizationServer.id!!)

        try {
            val builder = JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setRequireSubject()
                .setSkipDefaultAudienceValidation()
                .setExpectedIssuer(authorizationServer.serverUrl.toString() + authorizationServer.id + "/")
                .setExpectedAudience(authorizationServer.audience)
                .setJwsAlgorithmConstraints(
                    AlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT,
                        AlgorithmIdentifiers.RSA_USING_SHA256
                    )
                )

            for (jwk in jwks) {
                val signingKey = signingKeyRepository.findByIdAndAuthorizationServerId(
                    UUID.fromString(jwk.kid),
                    authorizationServer.id!!
                )
                builder.setVerificationKey(KeyUtils.decodePublicKey(signingKey?.publicKey))
                val jwtConsumer = builder.build()
                val jwtClaims = jwtConsumer.processToClaims(jwt.split(" ")[1])
                if (jwtClaims != null) {
                    return jwtClaims.claimsMap
                }
            }
        } catch (e: org.jose4j.jwt.consumer.InvalidJwtException) {
            throw NotAuthorized(e.message ?: "Invalid JWT")
        } catch (e: java.security.GeneralSecurityException) {
            throw RuntimeException(e)
        }
        throw NotAuthorized("JWT validation failed")
    }

    private fun generateRefreshToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun hashRefreshToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.toByteArray(StandardCharsets.UTF_8))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }

    @Transactional
    fun cleanupExpiredRefreshTokens() {
        refreshTokenRepository.deleteExpiredTokens()
    }

    @Transactional
    fun revokeAllUserRefreshTokens(userId: UUID) {
        refreshTokenRepository.revokeAllUserTokens(userId)
    }

    private fun createDefaultScopesForAuthorizationServer(authorizationServer: AuthorizationServer) {
        val defaultScopeNames = arrayOf("openid", "email", "profile", "offline_access")

        for (scopeName in defaultScopeNames) {
            val scope = Scope()
            scope.name = scopeName
            scope.authorizationServer = authorizationServer
            val metadata = Metadata(emptyList(), null, emptyMap())
            scope.metadata = metadata
            scopeService.createScope(scope)
        }
    }

    private fun createDefaultTemplatesForAuthorizationServer(authorizationServer: AuthorizationServer) {
        javaClass.getResourceAsStream("/templates/login.html")?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                val contents = reader.lines().collect(Collectors.joining(System.lineSeparator()))
                val template = Template()
                template.template = Base64.getEncoder().encode(contents.toByteArray())
                template.templateType = TemplateTypeEnum.LOGIN
                template.authorizationServerId = authorizationServer.id
                val metadata = Metadata(emptyList(), null, emptyMap())
                template.metadata = metadata
                val _template = templateService.createTemplate(template)
                eventRepository.createTemplateEvent(_template, EventType.CREATE)
            }
        }
    }

    private fun createDefaultSchemasForAuthorizationServer(authorizationServer: AuthorizationServer) {
        try {
            javaClass.getResourceAsStream("/schemas/oidc-userinfo-claims.json")?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                    val contents = reader.lines().collect(Collectors.joining(System.lineSeparator()))
                    val jsonb = JsonbBuilder.create()
                    @Suppress("UNCHECKED_CAST")
                    val schemaMap = jsonb.fromJson(contents, Map::class.java) as Map<String, Any>

                    val schema = Schema()
                    schema.name = "oidc-userinfo-claims"
                    schema.authorizationServerId = authorizationServer.id
                    schema.jsonSchemaVersion = "V202012"
                    schema.schema = schemaMap
                    schema.createdOn = OffsetDateTime.now()
                    schema.updatedOn = OffsetDateTime.now()

                    val metadata = Metadata(emptyList(), null, emptyMap())
                    schema.metadata = metadata

                    schemaService.createSchema(schema)
                }
            }
        } catch (e: Exception) {
            // Log warning but don't fail authorization server creation if schema creation fails
            LOG.warn("Could not create OIDC UserInfo claims schema: ${e.message}", e)
        }
    }

    private fun buildAccessTokenResponse(
        authorizationServer: com.revethq.auth.persistence.entities.AuthorizationServer,
        profile: Profile,
        additionalClaims: Map<String, Any>
    ): AccessToken {
        val signingKey = getSigningKeysForAuthorizationServer(authorizationServer.id!!)
        try {
            val jwt = Jwt
                .issuer(authorizationServer.serverUrl!!.toExternalForm() + "/" + authorizationServer.id + "/")
                .audience(authorizationServer.audience)
                .expiresIn(authorizationServer.clientCredentialsTokenExpiration!!)

            jwt.jws()
                .algorithm(
                    when (signingKey.keyType) {
                        else -> SignatureAlgorithm.RS256
                    }
                )
                .keyId(signingKey.id.toString())

            additionalClaims.forEach { (key, value) -> jwt.claim(key, value) }

            profile.profile?.forEach { (key, value) -> jwt.claim(key, value) }
            val token = jwt.sign(KeyUtils.decodePrivateKey(signingKey.privateKey))

            val accessToken = AccessToken()
            accessToken.accessToken = token
            accessToken.idToken = token // TODO: ID token should be separate from access token
            accessToken.tokenType = TokenTypeEnum.BEARER
            accessToken.expiresIn = Math.toIntExact(authorizationServer.authorizationCodeTokenExpiration!!)
            accessToken.scope = additionalClaims["scope"] as? String
            return accessToken
        } catch (e: Exception) {
            throw TokenGenerationException("Failed to generate access token", e)
        }
    }

    private fun buildClientCredentialsAccessToken(
        authorizationServer: com.revethq.auth.persistence.entities.AuthorizationServer,
        profile: Profile,
        additionalClaims: Map<String, Any>
    ): AccessToken {
        val signingKey = getSigningKeysForAuthorizationServer(authorizationServer.id!!)
        try {
            val jwt = Jwt
                .issuer(authorizationServer.serverUrl!!.toExternalForm() + "/" + authorizationServer.id + "/")
                .audience(authorizationServer.audience)
                .expiresIn(authorizationServer.clientCredentialsTokenExpiration!!)

            jwt.jws()
                .algorithm(
                    when (signingKey.keyType) {
                        else -> SignatureAlgorithm.RS256
                    }
                )
                .keyId(signingKey.id.toString())

            additionalClaims.forEach { (key, value) -> jwt.claim(key, value) }

            profile.profile?.forEach { (key, value) -> jwt.claim(key, value) }
            val token = jwt.sign(KeyUtils.decodePrivateKey(signingKey.privateKey))

            val accessToken = AccessToken()
            accessToken.accessToken = token
            // No ID token for client credentials flow
            accessToken.tokenType = TokenTypeEnum.BEARER
            accessToken.expiresIn = Math.toIntExact(authorizationServer.clientCredentialsTokenExpiration!!)
            accessToken.scope = additionalClaims["scope"] as? String
            return accessToken
        } catch (e: Exception) {
            throw TokenGenerationException("Failed to generate client credentials access token", e)
        }
    }
}
