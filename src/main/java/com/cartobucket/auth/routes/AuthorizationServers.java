package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.AuthorizationServersApi;
import com.cartobucket.auth.model.generated.AuthorizationServerRequest;
import com.cartobucket.auth.models.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class AuthorizationServers implements AuthorizationServersApi {
    public final AuthorizationServerService authorizationServerService;

    public AuthorizationServers(AuthorizationServerService authorizationServerService) {
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response authorizationServersAuthorizationServerIdDelete(UUID authorizationServerId) {
        authorizationServerService.deleteAuthorizationServer(authorizationServerId);
        return Response.ok().build();
    }

    @Override
    public Response authorizationServersAuthorizationServerIdGet(UUID authorizationServerId) {
        return Response
                .ok()
                .entity(AuthorizationServerMapper.toResponse(
                        authorizationServerService.getAuthorizationServer(authorizationServerId)))
                .build();
    }

    @Override
    public Response authorizationServersAuthorizationServerIdPut(
            UUID authorizationServerId,
            AuthorizationServerRequest authorizationServerRequest
    ) {
        return Response
                .ok()
                .entity(authorizationServerService.updateAuthorizationServer(
                        authorizationServerId,
                        authorizationServerRequest))
                .build();
    }

    @Override
    public Response authorizationServersGet() {
        return Response.ok().entity(authorizationServerService.getAuthorizationServers()).build();
    }

    @Override
    public Response authorizationServersPost(AuthorizationServerRequest authorizationServerRequest) {
        return Response
                .ok()
                .entity(authorizationServerService.createAuthorizationServer(authorizationServerRequest))
                .build();
    }
}
