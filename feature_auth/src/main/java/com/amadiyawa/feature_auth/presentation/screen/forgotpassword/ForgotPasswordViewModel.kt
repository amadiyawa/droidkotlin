package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.domain.model.ForgotPasswordForm
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_auth.domain.util.validation.ForgotPasswordFormValidator
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ForgotPasswordViewModel(
    private val validator: ForgotPasswordFormValidator
) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordAction>(ForgotPasswordUiState.Idle()) {

    private val _uiEvent = MutableSharedFlow<ForgotPasswordUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

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
            is ForgotPasswordAction.UpdateField -> {
                val updatedForm = form.updateAndValidateField(action.key, action.value, validator)
                setState { ForgotPasswordUiState.Idle(updatedForm) }
            }
            is ForgotPasswordAction.Submit -> handleSubmit()
            is ForgotPasswordAction.ShowError -> {
                setState { ForgotPasswordUiState.Error(form, action.message) }
            }
        }
    }

    fun handleSubmit() {
        val validated = validator.validate(form)
        if (!validated.isValid) {
            dispatch(ForgotPasswordAction.ShowError("Please correct the form errors"))
            return
        }

        _isSubmitting.value = true
        setState { ForgotPasswordUiState.Loading(form = form) }

        forgotPasswordJob?.cancel()
        forgotPasswordJob = viewModelScope.launch {
            delay(2000) // simulate API call
            val signUp = SignUp.random()
            signUp.otpPurpose = OtpPurpose.FORGOT_PASSWORD
            signUp.recipient = form.identifier.value
            _uiEvent.emit(ForgotPasswordUiEvent.NavigateToOtp(signUp))
            _isSubmitting.value = false
        }
    }
}