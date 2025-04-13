package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm

/**
 * Represents a sign-in form with validated fields.
 *
 * This data class contains two fields: `identifier` and `password`, both of which
 * are wrapped in `ValidatedField` to handle validation and associated errors.
 *
 * @property identifier A validated field for the user's identifier (e.g., username or email).
 * @property password A validated field for the user's password.
 */
data class SignInForm(
    val identifier: ValidatedField<String> = ValidatedField(""),
    val password: ValidatedField<String> = ValidatedField("")
) {
    /**
     * Converts the sign-in form into a `ValidatedForm` object.
     *
     * This method aggregates the validated fields into a `ValidatedForm` structure,
     * which can be used for global validation or additional processing.
     *
     * @return A `ValidatedForm` object containing the fields of the form.
     */
    fun asValidatedForm() = ValidatedForm(
        fields = mapOf(
            "identifier" to identifier,
            "password" to password
        )
    )
}
