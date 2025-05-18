package com.amadiyawa.feature_auth.presentation.screen.signin

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.domain.model.toSignInForm
import com.amadiyawa.feature_auth.domain.model.togglePasswordVisibility
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.usecase.CompleteSignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.CompleteSocialSignInUseCase
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_auth.domain.util.validation.SignInFormValidator
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.errorhandling.ErrorHandler
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SignInViewModel(
    private val completeSignInUseCase: CompleteSignInUseCase,
    private val completeSocialSignInUseCase: CompleteSocialSignInUseCase,
    private val validator: SignInFormValidator,
    errorHandler: ErrorHandler
) : BaseViewModel<SignInUiState, SignInAction>(
    SignInUiState.Idle(),
    errorHandler = errorHandler
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
            val result = completeSignInUseCase(
                SignInRequest(
                    identifier = form.identifier.value,
                    password = form.password.value
                )
            )

            handleResult(
                result = result,
                onSuccess = {
                    // Success handling
                    emitEvent(SignInUiEvent.ShowSnackbar("Sign in successful"))
                    emitEvent(SignInUiEvent.NavigateToMainScreen)
                    setState { SignInUiState.Idle(form = SignInForm()) }
                },
                onError = { errorMessage ->
                    // Custom error handling (optional)
                    setState { SignInUiState.Error(form = form, message = errorMessage) }
                },
                customErrorHandling = true
            )
        }
    }

    private fun handleSocialSignIn(provider: SocialProvider) {
        setState { SignInUiState.Loading.SocialAuthentication(form = form, provider = provider) }

        socialSignInJob?.cancel()
        socialSignInJob = viewModelScope.launch {
            val result = completeSocialSignInUseCase(provider)

            handleResult(
                result = result,
                onSuccess = {
                    // Success handling
                    emitEvent(SignInUiEvent.SocialSignInResult(
                        provider = provider,
                        success = true
                    ))
                    emitEvent(SignInUiEvent.ShowSnackbar("Sign in successful"))
                    emitEvent(SignInUiEvent.NavigateToMainScreen)
                    setState { SignInUiState.Idle(form = SignInForm()) }
                },
                onError = { errorMessage ->
                    // Custom error handling
                    setState { SignInUiState.Error(form = form, message = errorMessage) }
                    emitEvent(SignInUiEvent.SocialSignInResult(
                        provider = provider,
                        success = false,
                        message = errorMessage
                    ))
                },
                customErrorHandling = true
            )
        }
    }

    private fun handleValidationError(validated: ValidatedForm) {
        setState {
            SignInUiState.Error(
                form = validated.toSignInForm(),
                message = errorHandler?.getLocalizedMessage(
                    OperationResult.Failure(
                        code = 3000, // ValidationError code
                    )
                ) ?: "Please correct the form errors"
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