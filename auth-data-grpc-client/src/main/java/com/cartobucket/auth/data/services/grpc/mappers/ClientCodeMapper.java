package com.cartobucket.auth.data.services.grpc.mappers;

import com.cartobucket.auth.rpc.ClientCodeResponse;
import com.cartobucket.auth.rpc.ClientCreateClientCodeResponse;
import com.cartobucket.auth.data.domain.ClientCode;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ClientCodeMapper {
    public static ClientCode to(ClientCreateClientCodeResponse clientCode) {
        var clientCodeDomain = new ClientCode();
        clientCodeDomain.setId(UUID.fromString(clientCode.getId()));
        clientCodeDomain.setClientId(clientCode.getClientId());
        clientCodeDomain.setAuthorizationServerId(UUID.fromString(clientCode.getAuthorizationServerId()));
        clientCodeDomain.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(clientCode.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        clientCodeDomain.setRedirectUri(clientCode.getRedirectUri());
        clientCodeDomain.setScopes(clientCode.getScopesList().stream().map(ScopeMapper::toScope).toList());
        clientCodeDomain.setState(clientCode.getState());
        clientCodeDomain.setCode(clientCode.getCode());
        clientCodeDomain.setCodeChallenge(clientCode.getCodeChallenge());
        clientCodeDomain.setCodeChallengeMethod(clientCode.getCodeChallengeMethod());
        clientCodeDomain.setUserId(UUID.fromString(clientCode.getUserId()));
        clientCodeDomain.setNonce(clientCode.getNonce());
        return clientCodeDomain;
    }

    public static ClientCode to(ClientCodeResponse clientCode) {
        var clientCodeDomain = new ClientCode();
        clientCodeDomain.setId(UUID.fromString(clientCode.getId()));
        clientCodeDomain.setClientId(clientCode.getClientId());
        clientCodeDomain.setAuthorizationServerId(UUID.fromString(clientCode.getAuthorizationServerId()));
        clientCodeDomain.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(clientCode.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        clientCodeDomain.setRedirectUri(clientCode.getRedirectUri());
        clientCodeDomain.setScopes(clientCode.getScopesList().stream().map(ScopeMapper::toScope).toList());
        clientCodeDomain.setState(clientCode.getState());
        clientCodeDomain.setCode(clientCode.getCode());
        clientCodeDomain.setCodeChallenge(clientCode.getCodeChallenge());
        clientCodeDomain.setCodeChallengeMethod(clientCode.getCodeChallengeMethod());
        clientCodeDomain.setUserId(UUID.fromString(clientCode.getUserId()));
        clientCodeDomain.setNonce(clientCode.getNonce());
        return clientCodeDomain;
    }
}
