package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_auth.domain.validation.SignInFormValidator
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

/**
 * Represents a sign-in form with validated fields.
 *
 * This data class contains two fields: `identifier` and `password`, both of which
 * are wrapped in `ValidatedField` to handle validation and associated errors.
 *
 * @property identifier A validated field for the user's identifier (e.g., username or email).
 * @property password A validated field for the user's password.
 */
data class SignInForm(
    val identifier: ValidatedField<String> = ValidatedField(""),
    val password: ValidatedField<String> = ValidatedField("")
) {
    /**
     * Converts the sign-in form into a `ValidatedForm` object.
     *
     * This method aggregates the validated fields into a `ValidatedForm` structure,
     * which can be used for global validation or additional processing.
     *
     * @return A `ValidatedForm` object containing the fields of the form.
     */
    fun asValidatedForm() = ValidatedForm(
        fields = mapOf(
            "identifier" to identifier,
            "password" to password
        )
    )
}

fun SignInForm.updateAndValidateField(
    key: String,
    fieldValue: FieldValue,
    validator: SignInFormValidator
): SignInForm {
    return when (fieldValue) {
        is FieldValue.Text -> when (key) {
            "identifier" -> copy(
                identifier = identifier.copy(
                    value = fieldValue.value,
                    validation = validator.validateField("identifier", fieldValue.value)
                )
            )
            "password" -> copy(
                password = password.copy(
                    value = fieldValue.value,
                    validation = validator.validateField("password", fieldValue.value)
                )
            )
            else -> this
        }
        else -> this
    }
}

fun SignInForm.togglePasswordVisibility(): SignInForm {
    return copy(
        password = password.copy(isValueHidden = !password.isValueHidden)
    )
}

fun ValidatedForm.toSignInForm(): SignInForm {
    val identifierField = fields["identifier"]?.let {
        @Suppress("UNCHECKED_CAST")
        it as ValidatedField<String>
    } ?: ValidatedField("")

    val passwordField = fields["password"]?.let {
        @Suppress("UNCHECKED_CAST")
        it as ValidatedField<String>
    } ?: ValidatedField("")

    return SignInForm(
        identifier = identifierField,
        password = passwordField
    )
}