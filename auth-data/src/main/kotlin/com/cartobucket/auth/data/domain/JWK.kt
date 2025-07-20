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

package com.cartobucket.auth.data.domain

data class JWK(
    var kid: String? = null,
    var kty: String? = null,
    var alg: String? = null,
    var use: String? = null,
    var n: String? = null,
    var e: String? = null,
    var x5c: MutableList<String>? = null,
    var x5t: String? = null,
    var x5tHashS256: String? = null
) {
    fun addX5cItem(x5cItem: String): JWK {
        if (this.x5c == null) {
            this.x5c = mutableListOf()
        }
        this.x5c?.add(x5cItem)
        return this
    }

    fun removeX5cItem(x5cItem: String): JWK {
        this.x5c?.remove(x5cItem)
        return this
    }
}