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

package com.cartobucket.auth.postgres.client.entities

import com.revethq.core.Metadata
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.net.URL
import java.time.OffsetDateTime
import java.util.UUID

@Entity
open class AuthorizationServer {
    @Id
    @GeneratedValue
    open var id: UUID? = null

    open var name: String? = null

    open var serverUrl: URL? = null

    open var audience: String? = null

    open var clientCredentialsTokenExpiration: Long? = null

    open var authorizationCodeTokenExpiration: Long? = null

    @JdbcTypeCode(SqlTypes.JSON)
    open var metadata: Metadata? = null

    @OneToMany(targetEntity = Scope::class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "authorizationServer")
    open var scopes: List<Scope> = emptyList()

    open var createdOn: OffsetDateTime? = null

    open var updatedOn: OffsetDateTime? = null
}
