package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_auth.domain.validation.ForgotPasswordFormValidator
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

data class ForgotPasswordForm(
    val identifier: ValidatedField<String> = ValidatedField(""),
) {
    fun asValidatedForm() = ValidatedForm(
        fields = mapOf(
            "identifier" to identifier
        )
    )
}

fun ForgotPasswordForm.updateAndValidateField(
    key: String,
    fieldValue: FieldValue,
    validator: ForgotPasswordFormValidator
): ForgotPasswordForm {
    return when (fieldValue) {
        is FieldValue.Text -> when (key) {
            "identifier" -> copy(
                identifier = identifier.copy(
                    value = fieldValue.value,
                    validation = validator.validateField("identifier", fieldValue.value)
                )
            )
            else -> this
        }
        else -> this
    }
}