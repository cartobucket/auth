/*
 * Copyright 2024 Bryce Groff (Revet)
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

package com.revethq.auth.core.services

import com.revethq.auth.core.domain.IdentityProvider
import com.revethq.auth.core.domain.WellKnownEndpoints
import com.revethq.auth.core.exceptions.badrequests.WellKnownEndpointsFetchFailure
import com.revethq.auth.core.exceptions.notfound.IdentityProviderNotFound
import jakarta.json.bind.JsonbBuilder
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.UUID

interface IdentityProviderService {
    @Throws(IdentityProviderNotFound::class)
    fun deleteIdentityProvider(identityProviderId: UUID)

    @Throws(IdentityProviderNotFound::class)
    fun getIdentityProvider(identityProviderId: UUID): IdentityProvider

    fun createIdentityProvider(identityProvider: IdentityProvider): IdentityProvider

    @Throws(IdentityProviderNotFound::class)
    fun updateIdentityProvider(identityProviderId: UUID, identityProvider: IdentityProvider): IdentityProvider

    @Throws(WellKnownEndpointsFetchFailure::class)
    fun fetchWellKnownEndpoints(discoveryEndpoint: String): WellKnownEndpoints {
        return try {
            HttpClient.newBuilder().build().use { client ->
                val response = client.send(
                    HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(discoveryEndpoint))
                        .header("Accept", "application/json")
                        .timeout(Duration.ofSeconds(60))
                        .build(),
                    HttpResponse.BodyHandlers.ofString()
                )
                if (response.statusCode() != 200) {
                    throw WellKnownEndpointsFetchFailure(response.body())
                }

                JsonbBuilder.create().fromJson(response.body(), WellKnownEndpoints::class.java)
            }
        } catch (e: IOException) {
            throw WellKnownEndpointsFetchFailure(e.message.toString())
        } catch (e: InterruptedException) {
            throw WellKnownEndpointsFetchFailure(e.message.toString())
        }
    }
}