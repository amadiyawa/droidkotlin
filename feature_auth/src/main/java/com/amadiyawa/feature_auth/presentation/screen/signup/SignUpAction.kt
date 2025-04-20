package com.amadiyawa.feature_auth.presentation.screen.signup

import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_base.domain.enum.RecipientType
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

/**
 * Represents the actions that can be performed on the Sign-Up screen.
 *
 * This sealed interface defines a set of actions that can be triggered
 * during user interaction with the Sign-Up screen. Each action corresponds
 * to a specific operation or state change in the Sign-Up process.
 */
sealed interface SignUpAction : BaseAction {
    data class UpdateForm(val form: SignUpForm) : SignUpAction
    data class UpdateField(val key: String, val value: FieldValue) : SignUpAction
    data object TogglePasswordVisibility : SignUpAction
    data object Submit : SignUpAction
    data class ShowError(val message: String) : SignUpAction
    data class ShowValidationErrors(val form: SignUpForm) : SignUpAction
    data class UpdatePreferredRecipient(val type: RecipientType) : SignUpAction
}