package com.cartobucket.auth.services;

import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.Client;
import com.cartobucket.auth.models.ClientCode;

import java.util.UUID;

public interface ClientService {
    ClientCode getClientCodeForEmailAndPassword(AuthorizationServer authorizationServer, String clientId, String email, String password, String nonce);

}
