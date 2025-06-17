package com.amadiyawa.feature_auth.presentation.screen.signin

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.domain.usecase.CompleteSignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.CompleteSocialSignInUseCase
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.errorhandling.ErrorHandler
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class SignInViewModel(
    private val completeSignInUseCase: CompleteSignInUseCase,
    private val completeSocialSignInUseCase: CompleteSocialSignInUseCase,
    private val stringProvider: StringResourceProvider,
    errorHandler: ErrorHandler
) : BaseViewModel<SignInContract.State, SignInContract.Action>(
    initialState = SignInContract.State.Idle(),
    errorHandler = errorHandler
) {

    // Jobs for cancellation
    private var signInJob: Job? = null
    private var socialSignInJob: Job? = null

    // Current form reference for easy access (following your pattern)
    val form: SignInForm
        get() = when (val s = state) {
            is SignInContract.State.Idle -> s.form
            is SignInContract.State.Loading -> s.form
            is SignInContract.State.Error -> s.form
            is SignInContract.State.Success -> s.form
        }

    /**
     * Handle user actions dispatched from the UI (following your dispatch pattern).
     */
    override fun dispatch(action: SignInContract.Action) {
        logAction(action)

        when (action) {
            is SignInContract.Action.Initialize -> handleInitialize(action.defaultIdentifier)
            is SignInContract.Action.UpdateField -> handleUpdateField(action.field, action.value)
            is SignInContract.Action.UpdateFields -> handleUpdateFields(action.updates)
            is SignInContract.Action.TogglePasswordVisibility -> handleTogglePasswordVisibility()
            is SignInContract.Action.ToggleRememberMe -> handleToggleRememberMe()
            is SignInContract.Action.Submit -> handleSubmit()
            is SignInContract.Action.Retry -> handleRetry()
            is SignInContract.Action.ForgotPassword -> handleForgotPassword()
            is SignInContract.Action.SocialSignIn -> handleSocialSignIn(action.provider)
            is SignInContract.Action.ClearErrors -> handleClearErrors()
            is SignInContract.Action.NavigateBack -> handleNavigateBack()
            is SignInContract.Action.NavigateToSignUp -> handleNavigateToSignUp()
        }
    }

    /**
     * Initialize the screen with optional pre-filled data.
     */
    private fun handleInitialize(defaultIdentifier: String) {
        Timber.d("Initializing SignIn screen with identifier: $defaultIdentifier")

        val smartForm = if (defaultIdentifier.isNotBlank()) {
            SignInForm.withAutoDetectedIdentifier(defaultIdentifier)
        } else {
            SignInForm()
        }

        setState {
            SignInContract.State.Idle(
                form = smartForm,
                defaultIdentifier = defaultIdentifier
            )
        }
    }

    /**
     * Update a single form field (following your existing pattern).
     */
    private fun handleUpdateField(field: String, value: FieldValue) {
        when (value) {
            is FieldValue.Text -> {
                val updatedForm = form.updateField(field, value.value, stringProvider)
                setState { SignInContract.State.Idle(form = updatedForm) }
            }
            else -> {
                Timber.w("Unsupported field value type: ${value::class.simpleName}")
            }
        }
    }

    /**
     * Update multiple form fields at once.
     */
    private fun handleUpdateFields(updates: Map<String, String>) {
        val updatedForm = form.updateFields(updates, stringProvider)
        setState { SignInContract.State.Idle(form = updatedForm) }
    }

    /**
     * Toggle password visibility (show/hide).
     */
    private fun handleTogglePasswordVisibility() {
        val updatedForm = form.togglePasswordVisibility()
        setState { SignInContract.State.Idle(form = updatedForm) }
    }

    /**
     * Toggle remember me state.
     */
    private fun handleToggleRememberMe() {
        val updatedForm = form.toggleRememberMe()
        setState { SignInContract.State.Idle(form = updatedForm) }
    }

    /**
     * Submit the sign-in form (following your existing pattern).
     */
    private fun handleSubmit() {
        val validatedForm = form.validateForm(stringProvider)

        if (!validatedForm.canSubmit) {
            handleValidationError(validatedForm)
            return
        }

        setState { SignInContract.State.Loading.Authentication(form = validatedForm) }

        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            val result = completeSignInUseCase(
                SignInRequest(
                    identifier = validatedForm.identifier.value,
                    password = validatedForm.password.value
                )
            )

            handleResult(
                result = result,
                onSuccess = {
                    // Success handling (following your pattern)
                    setState { SignInContract.State.Success(form = validatedForm) }
                    emitEvent(SignInContract.Event.ShowSnackbar("Sign in successful"))
                    emitEvent(SignInContract.Event.NavigateToMainScreen)
                },
                onError = { errorMessage ->
                    // Custom error handling (following your pattern)
                    setState {
                        SignInContract.State.Error(
                            form = validatedForm,
                            message = errorMessage
                        )
                    }
                },
                customErrorHandling = true
            )
        }
    }

    /**
     * Handle social sign-in (following your existing pattern).
     */
    private fun handleSocialSignIn(provider: SocialProvider) {
        setState { SignInContract.State.Loading.SocialAuthentication(form = form, provider = provider) }

        socialSignInJob?.cancel()
        socialSignInJob = viewModelScope.launch {
            val result = completeSocialSignInUseCase(provider)

            handleResult(
                result = result,
                onSuccess = {
                    // Success handling
                    setState { SignInContract.State.Success(form = form) }
                    emitEvent(SignInContract.Event.SocialSignInResult(provider, true))
                    emitEvent(SignInContract.Event.ShowSnackbar("Sign in successful"))
                    emitEvent(SignInContract.Event.NavigateToMainScreen)
                },
                onError = { errorMessage ->
                    // Custom error handling
                    setState {
                        SignInContract.State.Error(
                            form = form,
                            message = errorMessage
                        )
                    }
                    emitEvent(SignInContract.Event.SocialSignInResult(
                        provider, false, errorMessage
                    ))
                },
                customErrorHandling = true
            )
        }
    }

    /**
     * Retry the last failed operation.
     */
    private fun handleRetry() {
        val currentState = state
        if (currentState is SignInContract.State.Error && currentState.form.canSubmit) {
            handleSubmit()
        }
    }

    /**
     * Clear all errors and return to idle state (following your pattern).
     */
    private fun handleClearErrors() {
        setState { SignInContract.State.Idle(form = form) }
    }

    /**
     * Navigate to forgot password screen (following your pattern).
     */
    private fun handleForgotPassword() {
        launchSafely {
            emitEvent(SignInContract.Event.NavigateToForgotPassword)
        }
    }

    /**
     * Navigate back to previous screen.
     */
    private fun handleNavigateBack() {
        launchSafely {
            emitEvent(SignInContract.Event.NavigateBack)
        }
    }

    /**
     * Navigate to sign-up screen.
     */
    private fun handleNavigateToSignUp() {
        launchSafely {
            emitEvent(SignInContract.Event.NavigateToSignUp)
        }
    }

    /**
     * Handle validation error (following your existing pattern).
     */
    private fun handleValidationError(validatedForm: SignInForm) {
        setState {
            SignInContract.State.Error(
                form = validatedForm,
                message = errorHandler?.getLocalizedMessage(
                    OperationResult.Failure(
                        code = 3000, // ValidationError code
                    )
                ) ?: "Please correct the form errors"
            )
        }
        emitEvent(SignInContract.Event.ShowSnackbar("Please correct the form errors", isError = true))
    }

    override fun onCleared() {
        super.onCleared()
        signInJob?.cancel()
        socialSignInJob?.cancel()
    }
}