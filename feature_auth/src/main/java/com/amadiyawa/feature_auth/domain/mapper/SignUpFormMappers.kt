package com.amadiyawa.feature_auth.domain.mapper

import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

fun ValidatedForm.toSignUpForm(): SignUpForm {
    return SignUpForm(
        fullName = getField("fullName"),
        username = getField("username"),
        email = getField("email"),
        phone = getField("phone"),
        password = getField("password"),
        confirmPassword = getField("confirmPassword"),
        termsAccepted = getField("termsAccepted")
    )
}

@Suppress("UNCHECKED_CAST")
private fun <T> ValidatedForm.getField(key: String): ValidatedField<T> {
    return fields[key] as? ValidatedField<T>
        ?: throw IllegalStateException("Field '$key' is missing or has wrong type")
}