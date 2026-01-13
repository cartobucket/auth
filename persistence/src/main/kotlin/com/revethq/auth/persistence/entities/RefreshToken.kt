/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.persistence.entities

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "refresh_tokens")
open class RefreshToken : PanacheEntityBase() {
    @Id
    @GeneratedValue
    open var id: UUID? = null

    @Column(name = "token_hash", nullable = false, unique = true)
    open var tokenHash: String? = null

    @Column(name = "user_id", nullable = false)
    open var userId: UUID? = null

    @Column(name = "client_id", nullable = false)
    open var clientId: String? = null

    @Column(name = "authorization_server_id", nullable = false)
    open var authorizationServerId: UUID? = null

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "scope_ids", columnDefinition = "jsonb")
    open var scopeIds: List<UUID>? = null

    @Column(name = "expires_at", nullable = false)
    open var expiresAt: OffsetDateTime? = null

    @Column(name = "created_on", nullable = false)
    open var createdOn: OffsetDateTime? = null

    @Column(name = "is_revoked", nullable = false)
    open var isRevoked: Boolean = false

    @Column(name = "revoked_at")
    open var revokedAt: OffsetDateTime? = null
}
