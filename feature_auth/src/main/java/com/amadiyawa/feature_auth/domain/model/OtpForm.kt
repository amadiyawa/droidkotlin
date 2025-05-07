package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.droidkotlin.base.R
import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_auth.presentation.screen.otpverification.ResendState
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.model.ValidatedField

data class OtpForm(
    val digits: List<ValidatedField<String>> = List(OTP_LENGTH) { ValidatedField("") },
    val verificationId: String = "",
    val purpose: OtpPurpose = OtpPurpose.SIGN_UP,
    val recipient: String = "",
    val resendState: ResendState = ResendState.Available
) {
    companion object {
        const val OTP_LENGTH = 6
    }

    // Code extraction
    val code: String get() = digits.joinToString("") { it.value }

    // Check if form is complete
    val isComplete: Boolean
        get() = digits.all {
            it.value.length == 1 &&
            it.value.first().isDigit()
        }

    // Check if all digits are valid according to their validation field
    val isValid: Boolean
        get() = digits.all { it.validation.isValid }

    // Update digit with self-validation
    fun updateDigit(index: Int, value: String, stringProvider: StringResourceProvider): OtpForm {
        if (index !in digits.indices) return this

        val updatedDigits = digits.toMutableList()
        val currentField = updatedDigits[index]

        updatedDigits[index] = currentField.copy(
            value = value,
            validation = validateSingleDigit(value, stringProvider),
            isTouched = true
        )

        return copy(digits = updatedDigits)
    }

    fun validateSingleDigit(digit: String, stringProvider: StringResourceProvider): FieldValidationResult {
        return when {
            digit.isEmpty() -> FieldValidationResult.Valid // Allow empty during input
            digit.length > 1 -> FieldValidationResult.invalid(
                stringProvider.getString(R.string.single_digit_only)
            )
            !digit[0].isDigit() -> FieldValidationResult.invalid(
                stringProvider.getString(R.string.numbers_only)
            )
            else -> FieldValidationResult.Valid
        }
    }

    // Full OTP validation
    fun validateFullCode(stringProvider: StringResourceProvider, expected: String? = null): FieldValidationResult {
        return when {
            code.length != OTP_LENGTH ->
                FieldValidationResult.invalid(stringProvider.getString(R.string.otp_length_error))
            !code.all { it.isDigit() } ->
                FieldValidationResult.invalid(stringProvider.getString(R.string.numbers_only))
            expected != null && code != expected ->
                FieldValidationResult.invalid(stringProvider.getString(R.string.otp_mismatch))
            else -> FieldValidationResult.Valid
        }
    }
}