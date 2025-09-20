// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbCreator

/**
 *
 */
enum class TemplateTypeEnum(
    private val value: String,
) {
    LOGIN("login"),
    ;

    companion object {
        /**
         * Convert a String into String, as specified in the
         * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
         */
        @JvmStatic
        fun fromString(s: String): TemplateTypeEnum {
            for (b in values()) {
                // using Objects.toString() to be safe if value type non-object type
                // because types like 'int' etc. will be auto-boxed
                if (b.value == s) {
                    return b
                }
            }
            throw IllegalArgumentException("Unexpected string value '$s'")
        }

        @JsonbCreator
        @JvmStatic
        fun fromValue(value: String): TemplateTypeEnum {
            for (b in values()) {
                if (b.value == value) {
                    return b
                }
            }
            throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    override fun toString(): String = value
}
