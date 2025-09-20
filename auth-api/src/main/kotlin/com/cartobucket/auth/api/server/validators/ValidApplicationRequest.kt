// (C)2024
package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.api.dto.ApplicationRequest
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.services.ApplicationService
import com.cartobucket.auth.data.services.AuthorizationServerService
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplicationRequest.Validator::class])
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
annotation class ValidApplicationRequest(
    val message: String = "The Application was not valid",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidApplicationRequest, ApplicationRequest> {
        @Inject
        lateinit var applicationService: ApplicationService

        @Inject
        lateinit var authorizationServerService: AuthorizationServerService

        override fun initialize(constraintAnnotation: ValidApplicationRequest) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: ApplicationRequest?,
            context: ConstraintValidatorContext,
        ): Boolean =
            try {
                value?.let {
                    val authorizationServer =
                        authorizationServerService.getAuthorizationServer(
                            it.authorizationServerId,
                        )
                    authorizationServer
                        .scopes
                        ?.map(Scope::id)
                        ?.containsAll(it.scopes ?: emptyList()) ?: false
                } ?: false
            } catch (e: Exception) {
                false
            }
    }
}
