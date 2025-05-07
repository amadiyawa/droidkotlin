package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.data.dto.request.ForgotPasswordRequest
import com.amadiyawa.feature_auth.domain.model.ForgotPasswordForm
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.usecase.ForgotPasswordUseCase
import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_auth.domain.util.validation.ForgotPasswordFormValidator
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ForgotPasswordViewModel(
    private val validator: ForgotPasswordFormValidator,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordAction>(ForgotPasswordUiState.Idle()) {

    val form: ForgotPasswordForm
        get() = when (val s = state) {
            is ForgotPasswordUiState.Idle -> s.form
            is ForgotPasswordUiState.Loading -> s.form
            is ForgotPasswordUiState.Error -> s.form
        }

    private var forgotPasswordJob: Job? = null

    override fun dispatch(action: ForgotPasswordAction) {
        logAction(action)

        when (action) {
            is ForgotPasswordAction.UpdateField -> handleUpdateField(action)
            is ForgotPasswordAction.Submit -> handleSubmit()
            is ForgotPasswordAction.ShowError -> {
                setState { ForgotPasswordUiState.Error(form, action.message) }
            }
        }
    }

    private fun handleUpdateField(action: ForgotPasswordAction.UpdateField) {
        val updatedForm = form.updateAndValidateField(
            key = action.key,
            fieldValue = action.value,
            validator = validator
        )
        setState { ForgotPasswordUiState.Idle(updatedForm) }
    }

    fun handleSubmit() {
        val validated = validator.validate(form)
        if (!validated.isValid) {
            dispatch(ForgotPasswordAction.ShowError("Please correct the form errors"))
            return
        }

        setState { ForgotPasswordUiState.Loading(form = form) }

        forgotPasswordJob?.cancel()
        forgotPasswordJob = viewModelScope.launch {
            val requestData = ForgotPasswordRequest(recipient = form.identifier.value)
            when (val result = forgotPasswordUseCase(requestData)) {
                is OperationResult.Success -> handleAuthSuccess(result.data)
                is OperationResult.Error -> handleAuthError(result.message!!)
                is OperationResult.Failure -> handleAuthError(result.message!!)
            }

            setState { ForgotPasswordUiState.Idle(form) }
        }
    }

    private fun handleAuthSuccess(data: VerificationResult) {
        data.purpose = OtpPurpose.PASSWORD_RESET
        emitEvent(ForgotPasswordUiEvent.NavigateToOtp(data))
    }

    private fun handleAuthError(message: String) {
        setState { ForgotPasswordUiState.Error(form, message) }
        emitEvent(ForgotPasswordUiEvent.ShowSnackbar(message))
    }
}