/*
 * Copyright (c) 2017, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.revethq.auth.core.domain

/**
 * Utility class representing a pair of values.
 *
 * @since 19.0
 */
data class Pair<L, R>(val left: L?, val right: R?) {
    
    companion object {
        private val EMPTY: Pair<Any?, Any?> = Pair(null, null)
        
        /**
         * Returns an empty pair.
         *
         * @since 19.0
         */
        @Suppress("UNCHECKED_CAST")
        fun <L, R> empty(): Pair<L, R> = EMPTY as Pair<L, R>
        
        /**
         * Constructs a pair with its left value being [left], or returns an empty pair if
         * [left] is null.
         *
         * @return the constructed pair or an empty pair if [left] is null.
         * @since 19.0
         */
        fun <L, R> createLeft(left: L?): Pair<L, R> =
            if (left == null) empty() else Pair(left, null)
        
        /**
         * Constructs a pair with its right value being [right], or returns an empty pair if
         * [right] is null.
         *
         * @return the constructed pair or an empty pair if [right] is null.
         * @since 19.0
         */
        fun <L, R> createRight(right: R?): Pair<L, R> =
            if (right == null) empty() else Pair(null, right)
        
        /**
         * Constructs a pair with its left value being [left], and its right value being
         * [right], or returns an empty pair if both inputs are null.
         *
         * @return the constructed pair or an empty pair if both inputs are null.
         * @since 19.0
         */
        fun <L, R> create(left: L?, right: R?): Pair<L, R> =
            if (left == null && right == null) empty() else Pair(left, right)
    }
    
    override fun toString(): String = "($left, $right)"
}