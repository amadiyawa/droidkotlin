package com.amadiyawa.feature_auth.domain.util.validation

import com.amadiyawa.feature_auth.domain.model.ForgotPasswordForm
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.usecase.ValidateEmailOrPhoneUseCase

class ForgotPasswordFormValidator (
    private val validateEmailOrPhone: ValidateEmailOrPhoneUseCase
) {
    fun validateField(key: String, value: String): FieldValidationResult {
        return when (key) {
            "identifier" -> validateEmailOrPhone.execute(value)
            else -> FieldValidationResult(false, "Invalid field")
        }
    }

    fun validate(form: ForgotPasswordForm): ValidatedForm {
        return ValidatedForm(
            fields = mapOf(
                "identifier" to form.identifier.copy(
                    validation = validateEmailOrPhone.execute(form.identifier.value)
                )
            )
        )
    }
}