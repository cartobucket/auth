package com.cartobucket.auth.services;

import com.cartobucket.auth.repositories.MockAuthorizationServerRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class ScopeServiceTests {

    @Inject
    ScopeService scopeService;

    @Test
    void testScopeStringToScopeList() {
        var singleScope = "test";
        var singleScopeList = ScopeService.scopeStringToScopeList(singleScope);
        assert singleScopeList.size() == 1;
        assert singleScopeList.contains(singleScope);

        var multipleScopesList = ScopeService.scopeStringToScopeList("test scope  last");
        assert multipleScopesList.size() == 3;
        assert multipleScopesList.contains("test");
        assert multipleScopesList.contains("last");

        // TODO should test more unicode whitespaces.
    }

    @Test
    void testScopeListToString() {
        var scopeString = ScopeService.scopeListToScopeString(List.of("test", "scope", "last"));
        assert scopeString.equals("test scope last");
    }

    @Test
    void testScopesFilteredByAuthorizationServer() {
        var scopes = scopeService.filterScopesForAuthorizationServerId(
                MockAuthorizationServerRepository.VALID_AUTHORIZATION_SERVER_ID,
                "test.read test.write not_valid"
                );
        assert scopes.size() == 2;
        assert scopes.contains("test.read");
        assert scopes.contains("test.write");
        assert !scopes.contains("not_valid");

    }
}
