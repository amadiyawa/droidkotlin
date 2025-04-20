package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

data class OtpForm(
    val digits: List<ValidatedField<String>> = List(6) { ValidatedField("") }
) {
    fun getCode(): String = digits.joinToString("") { it.value }

    fun isFullyFilled(): Boolean =
        digits.all { it.value.length == 1 && it.value.firstOrNull()?.isDigit() == true }

    fun updateDigitWithValidation(
        index: Int,
        value: String,
        validator: (String) -> FieldValidationResult
    ): OtpForm {
        if (index !in digits.indices) return this
        val updatedDigits = digits.toMutableList()
        val currentField = updatedDigits[index]

        updatedDigits[index] = currentField.copy(
            value = value,
            validation = validator(value),
            isTouched = true
        )

        return copy(digits = updatedDigits)
    }

    fun asValidatedForm(): ValidatedForm = ValidatedForm(
        fields = digits.mapIndexed { index, field -> "digit$index" to field }.toMap()
    )
}