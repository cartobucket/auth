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

import com.revethq.auth.core.domain.Client
import com.revethq.auth.core.domain.ClientCode
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.exceptions.badrequests.CodeChallengeBadData
import com.revethq.auth.core.exceptions.notfound.ClientCodeNotFound
import com.revethq.auth.core.exceptions.notfound.ClientNotFound
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.persistence.repositories.ClientCodeRepository
import com.revethq.auth.persistence.repositories.ClientRepository
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.ScopeReferenceRepository
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.ScopeReference
import com.revethq.auth.persistence.entities.mappers.ClientCodeMapper
import com.revethq.auth.persistence.entities.mappers.ClientMapper
import com.revethq.auth.persistence.entities.mappers.ScopeMapper
import io.quarkus.narayana.jta.QuarkusTransaction
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
class ClientService(
    private val clientRepository: ClientRepository,
    private val clientCodeRepository: ClientCodeRepository,
    private val scopeReferenceRepository: ScopeReferenceRepository,
    private val eventRepository: EventRepository,
    private val scopeService: ScopeService
) : com.revethq.auth.core.services.ClientService {

    @Transactional
    override fun createClientCode(authorizationServerId: UUID, clientCode: ClientCode): ClientCode {
        var mutableClientCode = clientCode
        val LOG = Logger.getLogger(ClientService::class.java)
        LOG.error("We are going to try and create the client code now.")
        val client = clientRepository.findByClientId(clientCode.clientId ?: throw CodeChallengeBadData("Client ID is required"))
        if (client.isEmpty || client.get().authorizationServerId != authorizationServerId) {
            throw CodeChallengeBadData("Unable to find the Client with the credentials provided")
        }

        // Validate that all requested scopes exist for the authorization server
        if (!mutableClientCode.scopes.isNullOrEmpty()) {
            val requestedScopeNames = mutableClientCode.scopes.orEmpty()
                .mapNotNull { it.name }

            val validScopes = scopeService.filterScopesForAuthorizationServerId(
                authorizationServerId,
                ScopeService.scopeListToScopeString(requestedScopeNames)
            )

            // Check if any requested scopes are invalid (not found on the authorization server)
            val validScopeNames = validScopes.mapNotNull { it.name }

            val invalidScopes = requestedScopeNames.filter { !validScopeNames.contains(it) }

            if (invalidScopes.isNotEmpty()) {
                throw CodeChallengeBadData("Invalid scopes requested: ${invalidScopes.joinToString(", ")}")
            }

            // Update clientCode with only the valid scopes
            mutableClientCode.scopes = validScopes
        }

        val messageDigest = MessageDigest.getInstance("SHA256")

        val code = BigInteger(
            1,
            messageDigest.digest(SecureRandom().generateSeed(120))
        ).toString(16)
        mutableClientCode.code = code

        mutableClientCode.createdOn = OffsetDateTime.now()
        val _clientCode = ClientCodeMapper.to(mutableClientCode)
        clientCodeRepository.persist(_clientCode)

        // Return the clientCode with the scopes converted from IDs
        mutableClientCode = ClientCodeMapper.from(_clientCode, scopeService)

        eventRepository.createClientCodeEvent(mutableClientCode, EventType.CREATE)

        return mutableClientCode
    }

    @Transactional
    override fun deleteClient(clientId: UUID) {
        val client = clientRepository
            .findByIdOptional(clientId)
            .orElseThrow { ClientNotFound() }
        clientRepository.delete(client)
        eventRepository.createClientEvent(ClientMapper.from(client), EventType.DELETE)
    }

    @Transactional
    @Throws(ClientNotFound::class)
    override fun updateClient(clientId: UUID, client: Client): Client {
        val _client = clientRepository
            .findByIdOptional(clientId)
            .orElseThrow { ClientNotFound() }

        _client.updatedOn = OffsetDateTime.now()
        _client.scopes = client.scopes.orEmpty().map { ScopeMapper.to(it) }
        _client.name = client.name
        _client.redirectUris = client.redirectUris
        clientRepository.persist(_client)
        eventRepository.createClientEvent(ClientMapper.from(_client), EventType.UPDATE)
        return ClientMapper.from(_client)
    }

    @Transactional
    @Throws(ClientNotFound::class)
    override fun getClient(clientId: String): Client {
        return clientRepository
            .findByClientId(clientId)
            .map { ClientMapper.from(it) }
            .orElseThrow { ClientNotFound() }
    }

    @Transactional
    @Throws(ClientCodeNotFound::class)
    override fun getClientCode(clientCode: String): ClientCode {
        val code = clientCodeRepository.findByCode(clientCode)
        return code
            .map { ClientCodeMapper.from(it, scopeService) }
            .orElseThrow { ClientCodeNotFound() }
    }

    @Transactional
    override fun getClients(authorizationServerIds: List<UUID>, page: Page): List<Client> {
        val allEntities: List<com.revethq.auth.persistence.entities.Client> = if (authorizationServerIds.isNotEmpty()) {
            clientRepository.list("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
        } else {
            clientRepository.listAll(Sort.descending("createdOn"))
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { ClientMapper.from(it) }
    }

    override fun createClient(client: Client): Client {
        QuarkusTransaction.begin()
        client.createdOn = OffsetDateTime.now()
        client.updatedOn = OffsetDateTime.now()
        client.id = UUID.randomUUID()
        val _client = ClientMapper.to(client)
        clientRepository.persist(_client)

        val scopeReferences = client
            .scopes.orEmpty()
            .map { scope ->
                val scopeReference = ScopeReference()
                scopeReference.resourceId = _client.id
                scopeReference.scopeId = scope.id
                scopeReference.scopeReferenceType = ScopeReference.ScopeReferenceType.CLIENT
                scopeReference
            }
        scopeReferenceRepository.persist(scopeReferences)
        QuarkusTransaction.commit()

        QuarkusTransaction.begin()
        val refreshedClient = ClientMapper.from(clientRepository.findById(_client.id))
        eventRepository.createClientEvent(refreshedClient, EventType.CREATE)
        QuarkusTransaction.commit()
        return refreshedClient
    }
}
