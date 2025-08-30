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

package com.cartobucket.auth.postgres.client.repositories;

import com.cartobucket.auth.postgres.client.entities.RefreshToken;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepositoryBase<RefreshToken, UUID> {
    
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return find("tokenHash = ?1 and isRevoked = false and expiresAt > ?2", 
                    tokenHash, OffsetDateTime.now())
                .firstResultOptional();
    }
    
    public void revokeToken(String tokenHash) {
        update("isRevoked = true, revokedAt = ?1 where tokenHash = ?2", 
               OffsetDateTime.now(), tokenHash);
    }
    
    public void revokeAllUserTokens(UUID userId) {
        update("isRevoked = true, revokedAt = ?1 where userId = ?2 and isRevoked = false", 
               OffsetDateTime.now(), userId);
    }
    
    public void deleteExpiredTokens() {
        delete("expiresAt < ?1", OffsetDateTime.now());
    }
}