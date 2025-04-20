package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_auth.domain.validation.SignUpFormValidator
import com.amadiyawa.feature_base.domain.enum.RecipientType
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

/**
 * Represents the sign-up form containing validated fields and the preferred recipient type.
 *
 * @property fullName A validated field for the user's full name.
 * @property username A validated field for the user's username.
 * @property email A validated field for the user's email address.
 * @property phone A validated field for the user's phone number.
 * @property password A validated field for the user's password.
 * @property confirmPassword A validated field for confirming the user's password.
 * @property termsAccepted A validated field indicating whether the terms and conditions are accepted.
 * @property preferredRecipient The preferred recipient type (email or phone).
 */
data class SignUpForm(
    val fullName: ValidatedField<String> = ValidatedField(""),
    val username: ValidatedField<String> = ValidatedField(""),
    val email: ValidatedField<String> = ValidatedField(""),
    val phone: ValidatedField<String> = ValidatedField(""),
    val password: ValidatedField<String> = ValidatedField(""),
    val confirmPassword: ValidatedField<String> = ValidatedField(""),
    val termsAccepted: ValidatedField<Boolean> = ValidatedField(false),
    val preferredRecipient: RecipientType = RecipientType.EMAIL
) {
    /**
     * Converts the sign-up form into a `ValidatedForm` object.
     *
     * This method aggregates the validated fields into a `ValidatedForm` structure,
     * which can be used for global validation or additional processing.
     *
     * @return A `ValidatedForm` object containing the fields of the form.
     */
    fun asValidatedForm() = ValidatedForm(
        fields = mapOf(
            "fullName" to fullName,
            "username" to username,
            "email" to email,
            "phone" to phone,
            "password" to password,
            "confirmPassword" to confirmPassword,
            "termsAccepted" to termsAccepted
        )
    )

    fun togglePasswordVisibility(): SignUpForm {
        return copy(
            password = password.copy(isValueHidden = !password.isValueHidden),
            confirmPassword = confirmPassword.copy(isValueHidden = !confirmPassword.isValueHidden)
        )
    }
}

/**
 * Retrieves the recipient's contact information based on the preferred recipient type.
 *
 * This function checks the `preferredRecipient` property of the `SignUpForm` instance
 * and returns the corresponding value (email or phone).
 *
 * @return A `String` representing the recipient's email address if the preferred recipient
 *         is `EMAIL`, or the phone number if the preferred recipient is `PHONE`.
 */
fun SignUpForm.getRecipient(): String {
    return when (preferredRecipient) {
        RecipientType.EMAIL -> email.value
        RecipientType.PHONE -> phone.value
    }
}

fun SignUpForm.updateAndValidateField(
    key: String,
    fieldValue: FieldValue,
    validator: SignUpFormValidator
): SignUpForm {
    return when (fieldValue) {
        is FieldValue.Text -> when (key) {
            "fullName" -> copy(
                fullName = fullName.copy(
                    value = fieldValue.value,
                    validation = validator.validateFullName.execute(fieldValue.value)
                )
            )
            "username" -> copy(
                username = username.copy(
                    value = fieldValue.value,
                    validation = validator.validateUsername.execute(fieldValue.value)
                )
            )
            "email" -> copy(
                email = email.copy(
                    value = fieldValue.value,
                    validation = validator.validateEmail.execute(fieldValue.value)
                )
            )
            "phone" -> copy(
                phone = phone.copy(
                    value = fieldValue.value,
                    validation = validator.validatePhone.execute(fieldValue.value)
                )
            )
            "password" -> copy(
                password = password.copy(
                    value = fieldValue.value,
                    validation = validator.validatePassword.execute(fieldValue.value)
                ),
                confirmPassword = confirmPassword.copy(
                    validation = validator.validatePasswordConfirmation.execute(
                        password = fieldValue.value,
                        confirmPassword = confirmPassword.value
                    )
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

        is FieldValue.BooleanValue -> when (key) {
            "termsAccepted" -> copy(
                termsAccepted = termsAccepted.copy(
                    value = fieldValue.value,
                    validation = validator.validateTermsAccepted.execute(fieldValue.value)
                )
            )
            else -> this
        }
    }
}