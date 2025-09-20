// (C)2024
package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.data.exceptions.notfound.UserNotFound
import com.cartobucket.auth.data.services.UserService
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidUsername.Validator::class])
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
annotation class ValidUsername(
    val message: String = "must not be null or the user already exists",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidUsername, String> {
        @Inject
        lateinit var userService: UserService

        override fun initialize(constraintAnnotation: ValidUsername) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: String?,
            context: ConstraintValidatorContext,
        ): Boolean {
            if (value == null) {
                return false
            }
            return try {
                userService.getUser(value) == null
            } catch (userNotFound: UserNotFound) {
                true
            }
        }
    }
}
