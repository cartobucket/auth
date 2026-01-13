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

import jakarta.persistence.CascadeType
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Transient
import java.time.OffsetDateTime
import java.util.UUID

@Entity
open class ApplicationSecret {
    @Id
    @GeneratedValue
    open var id: UUID? = null

    open var applicationId: UUID? = null

    open var authorizationServerId: UUID? = null

    open var name: String? = null

    @Transient
    open var applicationSecret: String? = null

    open var applicationSecretHash: String? = null

    open var createdOn: OffsetDateTime? = null

    open var expiresIn: Int? = null

    @JoinTable(
        name = "scopereference",
        joinColumns = [JoinColumn(
            name = "resourceId",
            foreignKey = ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
        )],
        inverseJoinColumns = [JoinColumn(name = "scopeId")]
    )
    @OneToMany(targetEntity = Scope::class, cascade = [CascadeType.DETACH], orphanRemoval = true, fetch = FetchType.EAGER)
    open var scopes: List<Scope> = emptyList()
}
