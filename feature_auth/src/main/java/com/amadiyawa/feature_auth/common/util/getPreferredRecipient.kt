package com.amadiyawa.feature_auth.common.util

import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_base.domain.util.RecipientType

internal fun getSignUpPreferredRecipient(signUpForm: SignUpForm) : String {
    return if (signUpForm.preferredRecipient == RecipientType.EMAIL) {
        signUpForm.email.value
    } else {
        signUpForm.phone.value
    }
}