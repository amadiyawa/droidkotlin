package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.lifecycle.viewModelScope
import com.amadiyawa.droidkotlin.base.R
import com.amadiyawa.feature_auth.data.dto.request.OtpVerificationRequest
import com.amadiyawa.feature_auth.data.dto.request.ResendOtpRequest
import com.amadiyawa.feature_auth.data.mapper.AuthDataMapper.toDomain
import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_auth.domain.model.OtpVerificationResult
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_auth.domain.usecase.OtpVerificationUseCase
import com.amadiyawa.feature_auth.domain.usecase.ResendOtpUseCase
import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.result.fold
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class OtpVerificationViewModel(
    private val otpVerificationUseCase: OtpVerificationUseCase,
    private val resendOtpUseCase: ResendOtpUseCase,
    private val stringProvider: StringResourceProvider,
    private val verificationResult: VerificationResult
) : BaseViewModel<OtpUiState, OtpAction>(OtpUiState.Idle(OtpForm())) {

    private var countdownJob: Job? = null

    init {
        dispatch(OtpAction.Initialize(
            verificationId = verificationResult.verificationId,
            purpose = verificationResult.purpose.name,
            recipient = verificationResult.recipient
        ))
    }

    override fun dispatch(action: OtpAction) {
        logAction(action)

        when (action) {
            is OtpAction.Initialize -> handleInitialize(action)
            is OtpAction.UpdateDigit -> handleUpdateDigit(action)
            is OtpAction.Submit -> handleSubmit()
            is OtpAction.ResendOtp -> handleResendOtp()
            is OtpAction.HandleVerificationSuccess -> handleSuccess(action.result)
            is OtpAction.HandleVerificationError -> handleError(action.error)
            is OtpAction.StartCountdown -> startCountdown()
            is OtpAction.CountdownTick -> handleCountdownTick(action)
        }
    }

    private fun handleInitialize(action: OtpAction.Initialize) {
        setState { currentState ->
            updateFormInState(currentState) { _ ->
                OtpForm(
                    digits = List(OtpForm.OTP_LENGTH) { ValidatedField("") },
                    verificationId = action.verificationId,
                    purpose = enumValueOf(action.purpose),
                    recipient = action.recipient,
                )
            }
        }
        startCountdown()
    }

    private fun handleUpdateDigit(action: OtpAction.UpdateDigit) {
        setState { currentState ->
            updateFormInState(currentState) { form ->
                form.updateDigit(
                    index = action.index,
                    value = action.value,
                    stringProvider = stringProvider
                )
            }
        }

        // Auto-submit if complete and valid
        val currentForm = state.form
        if (currentForm.isComplete && currentForm.isValid) {
            dispatch(OtpAction.Submit)
        }
    }

    private fun handleSubmit() {
        val currentForm = state.form

        // Validate the complete code
        val validationResult = currentForm.validateFullCode(stringProvider)
        if (!validationResult.isValid) {
            setState { currentState ->
                OtpUiState.Error(
                    form = currentState.form,
                    errorMessage = validationResult.errorMessage
                        ?: stringProvider.getString(R.string.invalid_otp)
                )
            }
            emitEvent(OtpUiEvent.ShowError(
                validationResult.errorMessage
                    ?: stringProvider.getString(R.string.invalid_otp)
            ))
            return
        }

        // Set loading state
        setState { currentState ->
            OtpUiState.Loading(currentState.form)
        }

        // Perform verification
        launchSafely {
            val request = OtpVerificationRequest(
                verificationId = currentForm.verificationId,
                code = currentForm.code,
                purpose = currentForm.purpose.name
            )

            otpVerificationUseCase(request).fold(
                onSuccess = { result ->
                    dispatch(OtpAction.HandleVerificationSuccess(result))
                },
                onFailure = { error ->
                    dispatch(OtpAction.HandleVerificationError(error))
                },
                onError = { error ->
                    setState { currentState ->
                        OtpUiState.Error(
                            form = currentState.form,
                            errorMessage = error.message!!
                        )
                    }
                    emitEvent(OtpUiEvent.ShowError(error.message!!))
                }
            )
        }
    }

    private fun handleSuccess(result: OtpVerificationResult) {
        setState { currentState ->
            OtpUiState.Idle(currentState.form)
        }

        when (state.form.purpose) {
            OtpPurpose.SIGN_UP -> {
                emitEvent(OtpUiEvent.NavigateToHome(result.authResponse!!.toDomain()))
            }
            OtpPurpose.PASSWORD_RESET -> {
                emitEvent(OtpUiEvent.NavigateToResetPassword(result.resetToken!!))
            }
        }
    }

    private fun handleError(error: OperationResult.Failure) {
        val errorMessage = error.message ?: "Unknown error occurred"

        setState { currentState ->
            OtpUiState.Error(
                form = currentState.form,
                errorMessage = errorMessage
            )
        }

        emitEvent(OtpUiEvent.ShowError(message = errorMessage))
    }

    private fun handleResendOtp() {
        // First check if resend is available
        val currentForm = state.form
        if (currentForm.resendState !is ResendState.Available) {
            return
        }

        // Update state to indicate resending
        setState { currentState ->
            OtpUiState.Loading(currentState.form, isResending = true)
        }

        // Perform resend operation
        launchSafely {
            val request = ResendOtpRequest(
                verificationId = currentForm.verificationId,
                purpose = currentForm.purpose.name
            )

            resendOtpUseCase(request).fold(
                onSuccess = { result ->
                    // Update verificationId if it changed
                    setState { currentState ->
                        updateFormInState(currentState) { form ->
                            form.copy(verificationId = result.verificationId)
                        }
                    }

                    // Start the countdown timer
                    dispatch(OtpAction.StartCountdown)

                    // Update UI state
                    setState { currentState ->
                        when (currentState) {
                            is OtpUiState.Loading -> OtpUiState.Idle(currentState.form)
                            else -> currentState
                        }
                    }
                },
                onFailure = { error ->
                    setState { currentState ->
                        OtpUiState.Error(
                            form = currentState.form,
                            errorMessage = error.message!!
                        )
                    }
                    emitEvent(OtpUiEvent.ShowError(error.message!!))
                },
                onError = { error ->
                    setState { currentState ->
                        OtpUiState.Error(
                            form = currentState.form,
                            errorMessage = error.message!!
                        )
                    }
                    emitEvent(OtpUiEvent.ShowError(error.message!!))
                }
            )
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            var seconds = verificationResult.expiresIn
            while (seconds > 0) {
                // Log the countdown tick action before updating state
                dispatch(OtpAction.CountdownTick(seconds))

                setState { currentState ->
                    updateFormInState(currentState) { form ->
                        form.copy(resendState = ResendState.Countdown(seconds))
                    }
                }

                // Emit event for UI feedback
                emitEvent(OtpUiEvent.UpdateResendAvailability(
                    canResend = false,
                    countdown = seconds
                ))

                delay(1000)
                seconds--
            }

            // For the final state when countdown completes
            dispatch(OtpAction.CountdownTick(0))

            // When countdown completes
            setState { currentState ->
                updateFormInState(currentState) { form ->
                    form.copy(resendState = ResendState.Available)
                }
            }

            // Emit event for UI feedback
            emitEvent(OtpUiEvent.UpdateResendAvailability(
                canResend = true,
                countdown = null
            ))
        }
    }

    private fun updateFormInState(state: OtpUiState, formUpdater: (OtpForm) -> OtpForm): OtpUiState {
        return when (state) {
            is OtpUiState.Idle -> state.copy(form = formUpdater(state.form))
            is OtpUiState.Loading -> state.copy(form = formUpdater(state.form))
            is OtpUiState.Error -> state.copy(form = formUpdater(state.form))
        }
    }

    private fun handleCountdownTick(action: OtpAction.CountdownTick) {
        setState { currentState ->
            updateFormInState(currentState) { form ->
                if (action.secondsRemaining > 0) {
                    form.copy(resendState = ResendState.Countdown(action.secondsRemaining))
                } else {
                    form.copy(resendState = ResendState.Available)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}