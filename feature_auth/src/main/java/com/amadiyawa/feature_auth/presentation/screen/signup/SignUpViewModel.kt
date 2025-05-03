package com.amadiyawa.feature_auth.presentation.screen.signup

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.common.util.getSignUpPreferredRecipient
import com.amadiyawa.feature_auth.domain.enum.OtpPurpose
import com.amadiyawa.feature_auth.domain.mapper.toSignUpForm
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.usecase.SignUpUseCase
import com.amadiyawa.feature_auth.domain.validation.SignUpFormValidator
import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val validator: SignUpFormValidator,
    errorMessageMapper: ErrorMessageMapper
) : BaseViewModel<SignUpUiState, SignUpAction>(
    SignUpUiState.Idle(),
    errorMessageMapper
) {

    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    val form: SignUpForm
        get() = when (val s = state) {
            is SignUpUiState.Idle -> s.form
            is SignUpUiState.Loading -> s.form
            is SignUpUiState.Error -> s.form
        }

    override fun dispatch(action: SignUpAction) {
        logAction(action)

        when (action) {
            is SignUpAction.UpdateForm -> setState { SignUpUiState.Idle(form = action.form) }

            is SignUpAction.UpdateField -> {
                val updatedForm = form.updateAndValidateField(action.key, action.value, validator)
                setState { SignUpUiState.Idle(form = updatedForm) }
            }

            SignUpAction.TogglePasswordVisibility -> {
                val updated = form.togglePasswordVisibility()
                setState { SignUpUiState.Idle(form = updated) }
            }

            SignUpAction.Submit -> handleSubmit()

            is SignUpAction.ShowError -> {
                setState { SignUpUiState.Error(form = form, message = action.message) }
            }

            is SignUpAction.ShowValidationErrors -> {
                setState { SignUpUiState.Error(form = action.form, message = "Corrige les erreurs du formulaire") }
            }

            is SignUpAction.UpdatePreferredRecipient -> {
                val updated = form.copy(preferredRecipient = action.type)
                setState { SignUpUiState.Idle(form = updated) }
            }
        }
    }

    private fun handleSubmit() {
        val result = validator.validate(form)
        if (!result.isValid) {
            dispatch(SignUpAction.ShowValidationErrors(result.toSignUpForm()))
            return
        }

        _isSubmitting.value = true
        setState { SignUpUiState.Loading(form = form) }

        viewModelScope.launch {
            delay(2000) // simulate API call
            val signUp = SignUp.random()
            signUp.otpPurpose = OtpPurpose.SIGN_UP
            signUp.recipient = getSignUpPreferredRecipient(form)
            _uiEvent.emit(SignUpUiEvent.NavigateToOtp(signUp))
            _isSubmitting.value = false
        }
    }
}