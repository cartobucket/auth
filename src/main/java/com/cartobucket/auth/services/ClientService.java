package com.cartobucket.auth.services;

import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;

public interface ClientService {
    ClientCode buildClientCodeForEmailAndPassword(AuthorizationServer authorizationServer, String clientId, String email, String password, String nonce);

}
