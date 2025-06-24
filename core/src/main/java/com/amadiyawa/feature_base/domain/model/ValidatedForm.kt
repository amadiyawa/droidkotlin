package com.amadiyawa.feature_base.domain.model

/**
 * A data class representing a validated form containing multiple fields.
 *
 * @property fields A map of field names to their corresponding validated fields.
 */
data class ValidatedForm(
    val fields: Map<String, ValidatedField<*>>
) {
    /**
     * Indicates whether all fields in the form are valid.
     */
    val isValid: Boolean
        get() = fields.values.all { it.validation.isValid }

    /**
     * Retrieves the error message of the first invalid field, if any.
     *
     * @return The error message of the first invalid field, or `null` if all fields are valid.
     */
    fun getFirstError(): String? {
        return fields.values.firstOrNull { !it.validation.isValid }?.validation?.errorMessage
    }

    /**
     * Retrieves a map of field names to their corresponding error messages.
     *
     * @return A map where the key is the field name and the value is the error message, or `null` if no error exists.
     */
    fun getErrors(): Map<String, String?> {
        return fields.filterValues { !it.validation.isValid }
            .mapValues { (_, field) -> field.validation.errorMessage }
    }

    /**
     * Retrieves a validated field by its key.
     *
     * @param key The name of the field.
     * @return The corresponding validated field, or `null` if the key does not exist.
     */
    operator fun get(key: String): ValidatedField<*>? = fields[key]

    /**
     * Updates a specific field in the form with a new validated field.
     *
     * @param key The name of the field to update.
     * @param field The new validated field to replace the existing one.
     * @return A new instance of `ValidatedForm` with the updated field.
     */
    fun updateField(key: String, field: ValidatedField<*>): ValidatedForm {
        val updatedFields = fields.toMutableMap()
        updatedFields[key] = field
        return copy(fields = updatedFields)
    }
}