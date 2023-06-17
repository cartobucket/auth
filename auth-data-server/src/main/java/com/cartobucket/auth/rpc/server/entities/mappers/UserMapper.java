/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
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

package com.cartobucket.auth.rpc.server.entities.mappers;

import com.cartobucket.auth.data.domain.User;

public class UserMapper {
    public static User from (com.cartobucket.auth.rpc.server.entities.User user) {
        var _user = new User();
        _user.setId(user.getId());
        _user.setUpdatedOn(user.getUpdatedOn());
        _user.setCreatedOn(user.getCreatedOn());
        _user.setAuthorizationServerId(user.getAuthorizationServerId());
        _user.setEmail(user.getEmail());
        _user.setUsername(user.getUsername());
        _user.setPasswordHash(user.getPasswordHash());
        return _user;
    }

    public static com.cartobucket.auth.rpc.server.entities.User to (User user) {
        var _user = new com.cartobucket.auth.rpc.server.entities.User();
        _user.setId(user.getId());
        _user.setUpdatedOn(user.getUpdatedOn());
        _user.setCreatedOn(user.getCreatedOn());
        _user.setAuthorizationServerId(user.getAuthorizationServerId());
        _user.setEmail(user.getEmail());
        _user.setUsername(user.getUsername());
        _user.setPasswordHash(user.getPasswordHash());
        return _user;
    }

}
