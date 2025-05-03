package com.amadiyawa.feature_auth.presentation.screen.resetpassword

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.domain.model.ResetPasswordForm
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.validation.ResetPasswordValidator
import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ResetPasswordViewModel(
    private val validator: ResetPasswordValidator,
    errorMessageMapper: ErrorMessageMapper
) : BaseViewModel<ResetPasswordUiState, ResetPasswordAction>(
    ResetPasswordUiState.Idle(),
    errorMessageMapper
) {

    private val _uiEvent = MutableSharedFlow<ResetPasswordUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    val form: ResetPasswordForm
        get() = when (val s = state) {
            is ResetPasswordUiState.Idle -> s.form
            is ResetPasswordUiState.Loading -> s.form
            is ResetPasswordUiState.Error -> s.form
        }

    private var resetPasswordJob: Job? = null

    override fun dispatch(action: ResetPasswordAction) {
        logAction(action)

        when (action) {
            is ResetPasswordAction.UpdateField -> {
                val updatedForm = form.updateAndValidateField(action.key, action.value, validator)
                setState { ResetPasswordUiState.Idle(form = updatedForm) }
            }
            is ResetPasswordAction.Submit -> handleSubmit()
            is ResetPasswordAction.ShowError -> {
                setState { ResetPasswordUiState.Error(form, action.message) }
            }
        }
    }

    fun handleSubmit() {
        val validated = validator.validate(form)
        if (!validated.isValid) {
            dispatch(ResetPasswordAction.ShowError("Please correct the form errors"))
            return
        }

        _isSubmitting.value = true
        setState { ResetPasswordUiState.Loading(form = form) }

        resetPasswordJob?.cancel()
        resetPasswordJob = viewModelScope.launch {
            delay(2000) // simulate API call
            val signUp = SignUp.random()
            _uiEvent.emit(ResetPasswordUiEvent.NavigateToSignIn(signUp.recipient))
            _isSubmitting.value = false
        }
    }
}