package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.AuthorizationServersApi;
import com.cartobucket.auth.model.generated.AuthorizationServerRequest;
import com.cartobucket.auth.model.generated.AuthorizationServerResponse;
import com.cartobucket.auth.model.generated.AuthorizationServersResponse;
import com.cartobucket.auth.services.AuthorizationServerService;

import java.util.UUID;

public class AuthorizationServers implements AuthorizationServersApi {
    public final AuthorizationServerService authorizationServerService;

    public AuthorizationServers(AuthorizationServerService authorizationServerService) {
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public void authorizationServersAuthorizationServerIdDelete(UUID authorizationServerId) {
        authorizationServerService.deleteAuthorizationServer(authorizationServerId);
    }

    @Override
    public AuthorizationServerResponse authorizationServersAuthorizationServerIdGet(UUID authorizationServerId) {
        return authorizationServerService.getAuthorizationServer(authorizationServerId);

    }

    @Override
    public AuthorizationServerResponse authorizationServersAuthorizationServerIdPut(
            UUID authorizationServerId,
            AuthorizationServerRequest authorizationServerRequest
    ) {
        return authorizationServerService.updateAuthorizationServer(authorizationServerId, authorizationServerRequest);
    }

    @Override
    public AuthorizationServersResponse authorizationServersGet() {
        return authorizationServerService.getAuthorizationServers();

    }

    @Override
    public AuthorizationServerResponse authorizationServersPost(AuthorizationServerRequest authorizationServerRequest) {
        return authorizationServerService.createAuthorizationServer(authorizationServerRequest);
    }
}
