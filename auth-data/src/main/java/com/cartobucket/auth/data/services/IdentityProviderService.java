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

package com.cartobucket.auth.data.services;

import com.cartobucket.auth.data.domain.IdentityProvider;
import com.cartobucket.auth.data.domain.WellKnownEndpoints;
import com.cartobucket.auth.data.exceptions.badrequests.WellKnownEndpointsFetchFailure;
import com.cartobucket.auth.data.exceptions.notfound.IdentityProviderNotFound;
import io.quarkus.restclient.runtime.QuarkusRestClientBuilder;
import io.quarkus.resteasy.common.runtime.jackson.QuarkusJacksonSerializer;
import jakarta.json.JsonBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

public interface IdentityProviderService {
    void deleteIdentityProvider(final UUID identityProviderId) throws IdentityProviderNotFound;

    IdentityProvider getIdentityProvider(final UUID identityProviderId) throws IdentityProviderNotFound;

    IdentityProvider createIdentityProvider(final IdentityProvider identityProvider);

    IdentityProvider updateIdentityProvider(final UUID identityProviderId, final IdentityProvider identityProvider) throws IdentityProviderNotFound;

    default WellKnownEndpoints fetchWellKnownEndpoints(final String discoveryEndpoint) throws WellKnownEndpointsFetchFailure {
        final var client = HttpClient
                .newBuilder()
                .build();
        final var request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(discoveryEndpoint))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60))
                .build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var json = response.body();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new WellKnownEndpoints
                .Builder()
                .setAuthorizationEndpoint("")
                .setTokenEndpoint("")
                .setIssuerEndpoint("")
                .setUserInfoEndpoint("")
                .setJwksEndpoint("")
                .build();
    }
}
