package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.validation.OtpFormValidator
import com.amadiyawa.feature_base.domain.usecase.ValidateOtpUseCase
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class OtpVerificationViewModel(
    private val validateOtpUseCase: ValidateOtpUseCase,
    private val otpFormValidator: OtpFormValidator
) : BaseViewModel<OtpUiState, OtpAction>(OtpUiState.Idle(OtpForm())) {

    private val _signUp = MutableStateFlow(SignUp.random())
    val signUp: StateFlow<SignUp> = _signUp

    private val _uiEvent = MutableSharedFlow<OtpUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private var countdownJob: Job? = null

    val form: OtpForm
        get() = when (val s = state) {
            is OtpUiState.Idle -> s.form
            is OtpUiState.Loading -> s.form
            is OtpUiState.Error -> s.form
        }

    override fun dispatch(action: OtpAction) {
        logAction(action)

        when (action) {
            is OtpAction.Initialize -> {
                _signUp.value = action.signUp
                startCountdown()
                setState { OtpUiState.Idle(form = form) }
            }

            is OtpAction.UpdateDigit -> {
                val updatedForm = form.updateDigitWithValidation(
                    index = action.index,
                    value = action.value,
                    validator = validateOtpUseCase::validateDigit,
                )
                setState { OtpUiState.Idle(form = updatedForm) }
            }

            is OtpAction.Submit -> {
                countdownJob?.cancel()
                handleSubmit()
            }

            is OtpAction.ShowError -> {
                setState { OtpUiState.Error(form = form, message = action.message) }
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            while (_signUp.value.expiresIn > 0) {
                delay(1000)
                _signUp.update { it.copy(expiresIn = it.expiresIn - 1) }
            }
        }
    }

    private fun handleSubmit() {
        val validatedForm = otpFormValidator.validate(form)
        if (!validatedForm.isValid) {
            dispatch(OtpAction.ShowError("OTP is not complete or contains invalid digits."))
            return
        }

        _isSubmitting.value = true
        setState { OtpUiState.Loading(form) }

        viewModelScope.launch {
            delay(2000)

            val otp = form.getCode()
            val result = validateOtpUseCase.validateMatch(input = otp, expected = otp)

            if (result.isValid) {
                _uiEvent.emit(OtpUiEvent.NavigateToNextScreen)
            } else {
                setState { OtpUiState.Error(form, message = result.errorMessage.orEmpty()) }
            }
            _isSubmitting.value = false
        }
    }
}