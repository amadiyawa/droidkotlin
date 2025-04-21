package com.amadiyawa.feature_auth.domain.validation

import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.usecase.ValidateIdentifierUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase

class SignInFormValidator(
    private val validateIdentifier: ValidateIdentifierUseCase,
    private val validatePassword: ValidatePasswordUseCase
) {
    fun validateField(key: String, value: String): FieldValidationResult {
        return when (key) {
            "identifier" -> validateIdentifier.execute(value)
            "password" -> validatePassword.execute(value)
            else -> FieldValidationResult(false, "Invalid field")
        }
    }

    fun validate(form: SignInForm): ValidatedForm {
        return ValidatedForm(
            fields = mapOf(
                "identifier" to form.identifier.copy(validation = validateIdentifier.execute(form.identifier.value)),
                "password" to form.password.copy(validation = validatePassword.execute(form.password.value))
            )
        )
    }
}
