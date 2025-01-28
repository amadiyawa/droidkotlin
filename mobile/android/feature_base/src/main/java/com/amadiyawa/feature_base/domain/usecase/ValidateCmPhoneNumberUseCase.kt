package com.amadiyawa.feature_base.domain.usecase

import com.amadiyawa.feature_base.domain.validation.ValidationPatterns

class ValidateCmPhoneNumberUseCase {
    fun execute(phoneNumber: String): Boolean {
        val formattedPhoneNumber = "+237$phoneNumber"
        return formattedPhoneNumber.matches(ValidationPatterns.CAMEROON_PHONE_NUMBER)
    }
}