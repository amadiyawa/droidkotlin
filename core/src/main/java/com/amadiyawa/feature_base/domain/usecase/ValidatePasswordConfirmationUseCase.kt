package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.droidkotlin.core.R

class ValidatePasswordConfirmationUseCase(private val context: Context) {

    fun execute(password: String, confirmPassword: String): FieldValidationResult {
        return if (password == confirmPassword) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.passwords_do_not_match))
        }
    }
}
