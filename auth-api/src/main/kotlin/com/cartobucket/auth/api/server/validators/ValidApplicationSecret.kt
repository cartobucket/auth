// (C)2024
package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.data.services.ApplicationService
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import java.util.UUID
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplicationSecret.Validator::class])
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
annotation class ValidApplicationSecret(
    val message: String = "The Application Secret was not found",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidApplicationSecret, UUID> {
        @Inject
        lateinit var applicationService: ApplicationService

        override fun initialize(constraintAnnotation: ValidApplicationSecret) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: UUID?,
            context: ConstraintValidatorContext,
        ): Boolean =
            try {
                value?.let { applicationService.getApplicationSecrets(listOf(it)) }
                true
            } catch (e: Exception) {
                context.buildConstraintViolationWithTemplate("")
                false
            }
    }
}
