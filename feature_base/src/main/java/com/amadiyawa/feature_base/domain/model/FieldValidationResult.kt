package com.amadiyawa.feature_base.domain.model

/**
 * A data class representing the result of a field validation.
 *
 * @property isValid Indicates whether the field is valid.
 * @property errorMessage The error message associated with the validation failure, or `null` if no error message is provided.
 */
data class FieldValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        val Valid = FieldValidationResult(true)
        fun invalid(message: String) = FieldValidationResult(false, message)
    }
}