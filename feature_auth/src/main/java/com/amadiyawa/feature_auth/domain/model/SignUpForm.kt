package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

data class SignUpForm(
    val fullName: ValidatedField<String> = ValidatedField(""),
    val username: ValidatedField<String> = ValidatedField(""),
    val email: ValidatedField<String> = ValidatedField(""),
    val phone: ValidatedField<String> = ValidatedField(""),
    val password: ValidatedField<String> = ValidatedField(""),
    val confirmPassword: ValidatedField<String> = ValidatedField(""),
    val termsAccepted: ValidatedField<Boolean> = ValidatedField(false)
) {
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
}
