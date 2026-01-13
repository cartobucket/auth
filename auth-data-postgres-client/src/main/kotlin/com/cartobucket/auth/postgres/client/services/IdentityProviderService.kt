/*
 * Copyright 2024 Bryce Groff (Cartobucket LLC)
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

import com.cartobucket.auth.data.domain.IdentityProvider
import com.cartobucket.auth.data.domain.WellKnownEndpoints
import com.cartobucket.auth.data.exceptions.badrequests.WellKnownEndpointsFetchFailure
import com.cartobucket.auth.data.exceptions.notfound.IdentityProviderNotFound
import com.cartobucket.auth.postgres.client.repositories.IdentityProviderRepository
import java.util.UUID

class IdentityProviderService(
    private val identityProviderRepository: IdentityProviderRepository
) : com.cartobucket.auth.data.services.IdentityProviderService {

    // TODO: Move to mapper
    private fun from(identityProvider: IdentityProvider): com.cartobucket.auth.postgres.client.entities.IdentityProvider {
        return com.cartobucket.auth.postgres.client.entities.IdentityProvider()
    }

    private fun to(byId: com.cartobucket.auth.postgres.client.entities.IdentityProvider): IdentityProvider {
        return IdentityProvider()
    }

    @Throws(IdentityProviderNotFound::class)
    override fun deleteIdentityProvider(identityProviderId: UUID) {
        identityProviderRepository.deleteById(identityProviderId)
    }

    @Throws(IdentityProviderNotFound::class)
    override fun getIdentityProvider(identityProviderId: UUID): IdentityProvider {
        return to(identityProviderRepository.findById(identityProviderId))
    }

    override fun createIdentityProvider(identityProvider: IdentityProvider): IdentityProvider {
        val entity = from(identityProvider)
        identityProviderRepository.persist(entity)
        return to(entity)
    }

    @Throws(IdentityProviderNotFound::class)
    override fun updateIdentityProvider(identityProviderId: UUID, identityProvider: IdentityProvider): IdentityProvider {
        val provider = identityProviderRepository.findById(identityProviderId)
        //TODO: Update values;
        identityProviderRepository.persist(provider)
        return to(provider)
    }

    @Throws(WellKnownEndpointsFetchFailure::class)
    override fun fetchWellKnownEndpoints(discoveryEndpoint: String): WellKnownEndpoints {
        return super.fetchWellKnownEndpoints(discoveryEndpoint)
    }
}
