package com.cartobucket.auth.postgres.client.services;

import com.cartobucket.auth.data.domain.IdentityProvider;
import com.cartobucket.auth.data.domain.WellKnownEndpoints;
import com.cartobucket.auth.data.exceptions.badrequests.WellKnownEndpointsFetchFailure;
import com.cartobucket.auth.data.exceptions.notfound.IdentityProviderNotFound;
import com.cartobucket.auth.postgres.client.repositories.IdentityProviderRepository;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IdentityProviderService implements com.cartobucket.auth.data.services.IdentityProviderService {

    // TODO: Move to mapper
    private IdentityProviderRepository identityProviderRepository;

    private com.cartobucket.auth.postgres.client.entities.IdentityProvider from(IdentityProvider identityProvider) {
        return new com.cartobucket.auth.postgres.client.entities.IdentityProvider();
    }

    private IdentityProvider to(com.cartobucket.auth.postgres.client.entities.IdentityProvider byId) {
        return new IdentityProvider();
    }

    @Override
    public void deleteIdentityProvider(UUID identityProviderId) throws IdentityProviderNotFound {
        identityProviderRepository.deleteById(identityProviderId);
    }

    @Override
    public IdentityProvider getIdentityProvider(UUID identityProviderId) throws IdentityProviderNotFound {
        return this.to(identityProviderRepository.findById(identityProviderId));
    }

    @Override
    public IdentityProvider createIdentityProvider(IdentityProvider identityProvider) {
        identityProviderRepository.persist(this.from(identityProvider));
        return null;
    }

    @Override
    public IdentityProvider updateIdentityProvider(UUID identityProviderId, IdentityProvider identityProvider) throws IdentityProviderNotFound {
        var provider = identityProviderRepository.findById(identityProviderId);
        //TODO: Update values;
        identityProviderRepository.persist(provider);
        return to(provider);
    }

    @Override
    public @NotNull WellKnownEndpoints fetchWellKnownEndpoints(@NotNull String discoveryEndpoint) throws WellKnownEndpointsFetchFailure {
        return com.cartobucket.auth.data.services.IdentityProviderService.super.fetchWellKnownEndpoints(discoveryEndpoint);
    }
}
