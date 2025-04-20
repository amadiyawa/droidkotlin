package com.amadiyawa.feature_base.domain.model

/**
 * A generic data class representing a validated field in a form.
 *
 * @param T The type of the field's value.
 * @property value The actual value of the field.
 * @property validation The result of the field's validation, including its validity and any associated error message.
 * @property isTouched Indicates whether the field has been interacted with by the user.
 * @property isFocused Indicates whether the field is currently focused.
 * @property isValueHidden Indicates whether the field's value should be hidden (e.g., for password fields).
 */
data class ValidatedField<T>(
    val value : T,
    val validation: FieldValidationResult = FieldValidationResult(isValid = false),
    val isTouched: Boolean = false,
    val isFocused: Boolean = false,
    val isValueHidden: Boolean = false
)