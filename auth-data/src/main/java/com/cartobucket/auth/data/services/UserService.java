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

package com.cartobucket.auth.data.services;

import com.cartobucket.auth.data.domain.Identifier;
import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.SchemaValidation;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    record Page (
            int limit,
            int offset
    ) {}
    record UserQuery(
            List<UUID> authorizationServerIds,
            List<UUID> userIds,
            List<String> emails,
            List<Identifier> identifiers,
            List<SchemaValidation> validations,
            Page page
    ) {}

    List<User> query(UserQuery query);

    List<User> getUsers(final List<UUID> authorizationServerIds, com.cartobucket.auth.data.domain.Page page);

    Pair<User, Profile> createUser(final Pair<User, Profile> userProfilePair);

    void deleteUser(final UUID userId);

    Pair<User, Profile> getUser(final UUID userId) throws UserNotFound, ProfileNotFound;

    Pair<User, Profile> getUser(final String username) throws UserNotFound, ProfileNotFound;

    Pair<User, Profile> updateUser(final UUID userId, final Pair<User, Profile> userProfilePair) throws UserNotFound, ProfileNotFound;

    void setPassword(User user, String password);

    boolean validatePassword(final UUID userId, String password);
}
