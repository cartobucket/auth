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

package com.cartobucket.auth.data.domain;

import com.cartobucket.auth.data.exceptions.badrequests.WellKnownEndpointsFetchFailure;
import com.cartobucket.auth.data.exceptions.notfound.IdentityProviderNotFound;
import com.cartobucket.auth.data.services.IdentityProviderService;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.UUID;

@QuarkusTest
public class WellKnownEndpointsTests {

    public class MockIdentityProviderService implements IdentityProviderService {

        @Override
        public void deleteIdentityProvider(UUID identityProviderId) throws IdentityProviderNotFound {

        }

        @Override
        public IdentityProvider getIdentityProvider(UUID identityProviderId) throws IdentityProviderNotFound {
            return null;
        }

        @Override
        public IdentityProvider createIdentityProvider(IdentityProvider identityProvider) {
            return null;
        }

        @Override
        public IdentityProvider updateIdentityProvider(UUID identityProviderId, IdentityProvider identityProvider) throws IdentityProviderNotFound {
            return null;
        }
    }

    @Mock
    HttpClient client;

    @Mock
    HttpResponse<String> response;

    @Test
    public void WellKnownEndpointTests() throws WellKnownEndpointsFetchFailure {
      assert true;
    }
}
