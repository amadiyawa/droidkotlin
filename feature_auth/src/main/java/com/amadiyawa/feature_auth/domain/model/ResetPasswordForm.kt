package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_auth.domain.validation.ResetPasswordValidator
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

data class ResetPasswordForm(
    val password: ValidatedField<String> = ValidatedField(""),
    val confirmPassword: ValidatedField<String> = ValidatedField("")
) {
    fun asValidatedForm() = ValidatedForm (
        fields = mapOf(
            "password" to password,
            "confirmPassword" to confirmPassword
        )
    )
}

fun ResetPasswordForm.updateAndValidateField(
    key: String,
    fieldValue: FieldValue,
    validator: ResetPasswordValidator
): ResetPasswordForm {
    return when (fieldValue) {
        is FieldValue.Text -> when (key) {
            "password" -> copy(
                password = password.copy(
                    value = fieldValue.value,
                    validation = validator.validatePassword.execute(fieldValue.value)
                )
            )
            "confirmPassword" -> copy(
                confirmPassword = confirmPassword.copy(
                    value = fieldValue.value,
                    validation = validator.validatePasswordConfirmation.execute(
                        password = password.value,
                        confirmPassword = fieldValue.value
                    )
                )
            )
            else -> this
        }
        else -> this
    }
}