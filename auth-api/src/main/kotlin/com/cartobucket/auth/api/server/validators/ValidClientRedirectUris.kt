// (C)2024
package com.cartobucket.auth.api.server.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import java.net.URI
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidClientRedirectUris.Validator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@MustBeDocumented
annotation class ValidClientRedirectUris(
    val message: String = "must be valid URI's in the form of protocol://host[:port]/path",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidClientRedirectUris, List<String>> {
        override fun initialize(constraintAnnotation: ValidClientRedirectUris) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: List<String>?,
            context: ConstraintValidatorContext,
        ): Boolean =
            try {
                value?.map { URI.create(it) }
                true
            } catch (e: IllegalArgumentException) {
                false
            }
    }
}
