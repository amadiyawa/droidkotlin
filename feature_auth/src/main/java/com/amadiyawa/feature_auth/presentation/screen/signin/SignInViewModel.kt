package com.amadiyawa.feature_auth.presentation.screen.signin

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.domain.model.toJson
import com.amadiyawa.feature_auth.domain.model.toSignInForm
import com.amadiyawa.feature_auth.domain.model.togglePasswordVisibility
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.usecase.SignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.SocialSignInUseCase
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_auth.domain.util.validation.SignInFormValidator
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val socialSignInUseCase: SocialSignInUseCase,
    private val validator: SignInFormValidator,
    private val sessionRepository: SessionRepository
) : BaseViewModel<SignInUiState, SignInAction>(
    SignInUiState.Idle()
) {

    // Jobs
    private var signInJob: Job? = null
    private var socialSignInJob: Job? = null

    // Properties
    val form: SignInForm
        get() = when (val s = state) {
            is SignInUiState.Idle -> s.form
            is SignInUiState.Loading -> s.form
            is SignInUiState.Error -> s.form
        }

    override fun dispatch(action: SignInAction) {
        logAction(action)

        when (action) {
            is SignInAction.UpdateField -> handleUpdateField(action)
            SignInAction.TogglePasswordVisibility -> handleTogglePasswordVisibility()
            SignInAction.Submit -> handleSignIn()
            SignInAction.ForgotPassword -> handleForgotPassword()
            is SignInAction.SocialSignIn -> handleSocialSignIn(action.provider)
            SignInAction.ClearErrors -> handleClearErrors()
        }
    }

    private fun handleUpdateField(action: SignInAction.UpdateField) {
        val updatedForm = form.updateAndValidateField(
            key = action.key,
            fieldValue = action.value,
            validator = validator
        )
        setState { SignInUiState.Idle(form = updatedForm) }
    }

    private fun handleTogglePasswordVisibility() {
        val updated = form.togglePasswordVisibility()
        setState { SignInUiState.Idle(form = updated) }
    }

    private fun handleClearErrors() {
        setState { SignInUiState.Idle(form = form) }
    }

    private fun handleForgotPassword() {
        launchSafely {
            emitEvent(SignInUiEvent.NavigateToForgotPassword)
        }
    }

    private fun handleSignIn() {
        val validated = validator.validate(form)
        if (!validated.isValid) {
            handleValidationError(validated)
            return
        }

        setState { SignInUiState.Loading.Authentication(form = form) }

        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            val result = signInUseCase(
                SignInRequest(
                    identifier = form.identifier.value,
                    password = form.password.value
                )
            )
            handleAuthResult(result)
        }
    }

    private fun handleSocialSignIn(provider: SocialProvider) {
        setState { SignInUiState.Loading.SocialAuthentication(form = form, provider = provider) }

        socialSignInJob?.cancel()
        socialSignInJob = viewModelScope.launch {
            when (val result = socialSignInUseCase(provider)) {
                is OperationResult.Success -> handleAuthSuccess(result.data, provider)
                is OperationResult.Error -> handleAuthError(result.message!!, provider)
                is OperationResult.Failure -> handleAuthError(result.message!!, provider)
            }
        }
    }

    private suspend fun handleAuthResult(
        result: OperationResult<AuthResult>,
        provider: SocialProvider? = null
    ) {
        when (result) {
            is OperationResult.Success -> handleAuthSuccess(result.data, provider)
            is OperationResult.Error -> handleAuthError(result.message!!)
            is OperationResult.Failure -> handleAuthError(result.message!!)
        }
    }

    private suspend fun handleAuthSuccess(authResult: AuthResult, provider: SocialProvider? = null) {
        setState { SignInUiState.Loading.SessionSaving(form = form) }
        delay(2000)

        val saveResult = sessionRepository.saveSessionUserJson(authResult.toJson())
        if (saveResult is OperationResult.Success) {
            val activeResult = sessionRepository.setSessionActive(true)

            setState { SignInUiState.Loading.SessionActivation(form = form) }
            delay(2000)
            if (activeResult is OperationResult.Success) {
                provider?.let {
                    emitEvent(SignInUiEvent.SocialSignInResult(
                        provider = it,
                        success = true
                    ))
                }
                emitEvent(SignInUiEvent.ShowSnackbar("Sign in successful"))
                emitEvent(SignInUiEvent.NavigateToMainScreen)
                setState { SignInUiState.Idle(form = SignInForm()) }
            } else if (activeResult is OperationResult.Error) {
                handleAuthError(
                    message = activeResult.message!!,
                    provider = provider
                )
            }
        } else if (saveResult is OperationResult.Error) {
            handleAuthError(
                message = saveResult.message!!,
                provider = provider
            )
        }
    }

    private fun handleAuthError(
        message: String,
        provider: SocialProvider? = null
    ) {
        setState { SignInUiState.Error(form = form, message = message) }
        emitEvent(SignInUiEvent.ShowSnackbar(message, isError = true))

        provider?.let {
            emitEvent(SignInUiEvent.SocialSignInResult(
                provider = it,
                success = false,
                message = message
            ))
        }
    }

    private fun handleValidationError(validated: ValidatedForm) {
        setState {
            SignInUiState.Error(
                form = validated.toSignInForm(),
                message = "Please correct the form errors"
            )
        }
        emitEvent(SignInUiEvent.ShowSnackbar("Please correct the form errors", isError = true))
    }

    override fun onCleared() {
        super.onCleared()
        signInJob?.cancel()
        socialSignInJob?.cancel()
    }
}