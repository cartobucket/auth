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
@Constraint(validatedBy = [ValidClientName.Validator::class])
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
annotation class ValidClientName(
    val message: String = "must be greater than 3 characters and less than 400 characters and contain only letters, numbers, and dashes",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidClientName, String> {
        override fun initialize(constraintAnnotation: ValidClientName) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: String?,
            context: ConstraintValidatorContext,
        ): Boolean {
            if (value == null) {
                return false
            }
            return value.length >= 3 && value.length <= 400
        }
    }
}
