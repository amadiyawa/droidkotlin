package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.paygo.base.R

class ValidateTermsAcceptedUseCase(private val context: Context) {

    fun execute(accepted: Boolean): FieldValidationResult {
        return if (accepted) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.terms_not_accepted))
        }
    }
}
