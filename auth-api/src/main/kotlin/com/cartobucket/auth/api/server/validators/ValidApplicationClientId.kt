// (C)2024
package com.cartobucket.auth.api.server.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplicationClientId.Validator::class])
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
annotation class ValidApplicationClientId(
    val message: String = "must be null or greater than 8 and less than 400 characters",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidApplicationClientId, String> {
        override fun initialize(constraintAnnotation: ValidApplicationClientId) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: String?,
            context: ConstraintValidatorContext,
        ): Boolean {
            if (value != null) {
                return value.length >= 8 && value.length <= 400
            }
            return true
        }
    }
}
