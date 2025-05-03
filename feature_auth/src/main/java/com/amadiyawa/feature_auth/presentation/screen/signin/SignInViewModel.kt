package com.amadiyawa.feature_auth.presentation.screen.signin

import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.data.datasource.api.model.SignInRequest
import com.amadiyawa.feature_auth.domain.enum.SocialProvider
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.domain.model.toJson
import com.amadiyawa.feature_auth.domain.model.toSignInForm
import com.amadiyawa.feature_auth.domain.model.togglePasswordVisibility
import com.amadiyawa.feature_auth.domain.model.updateAndValidateField
import com.amadiyawa.feature_auth.domain.usecase.SignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.SocialSignInUseCase
import com.amadiyawa.feature_auth.domain.validation.SignInFormValidator
import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.util.ErrorCategory
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val socialSignInUseCase: SocialSignInUseCase,
    private val validator: SignInFormValidator,
    private val sessionRepository: SessionRepository,
    errorMessageMapper: ErrorMessageMapper
) : BaseViewModel<SignInUiState, SignInAction>(
    SignInUiState.Idle(),
    errorMessageMapper = errorMessageMapper
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
            SignInAction.Submit -> handleSubmit()
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

    private fun handleSubmit() {
        val validated = validator.validate(form)
        if (!validated.isValid) {
            handleValidationError(validated)
            return
        }

        setState { SignInUiState.Loading.Authentication(form = form) }

        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            // Execute sign-in use case (which returns OperationResult)
            val result = signInUseCase(
                SignInRequest(
                    identifier = form.identifier.value,
                    password = form.password.value
                )
            )

            when (result) {
                is OperationResult.Success -> {
                    // User signed in successfully
                    val signIn = result.data

                    // Store user session
                    val saveResult = sessionRepository.saveSessionUserJson(signIn.toJson())

                    if (saveResult is OperationResult.Success) {
                        // Update state to next loading phase
                        setState { SignInUiState.Loading.SessionSaving(form = form) }

                        // Set session as active
                        val activeResult = sessionRepository.setSessionActive(true)

                        if (activeResult is OperationResult.Success) {
                            // Update state to final loading phase
                            setState { SignInUiState.Loading.SessionActivation(form = form) }

                            // All operations succeeded
                            emitEvent(SignInUiEvent.ShowSnackbar("Sign in successful"))
                            emitEvent(SignInUiEvent.NavigateToMainScreen)
                            setState { SignInUiState.Idle(form = SignInForm()) }
                        } else {
                            // Handle session activation failure
                            handleOperationFailure(
                                result = activeResult,
                                errorCategory = ErrorCategory.SESSION,
                                onStateUpdate = { message ->
                                    setState {
                                        SignInUiState.Error(
                                            form = form,
                                            message = message
                                        )
                                    }
                                },
                                createSnackbarEvent = { message ->
                                    SignInUiEvent.ShowSnackbar(message, isError = true)
                                }
                            )
                        }
                    } else {
                        // Handle save session failure
                        handleOperationFailure(
                            result = saveResult,
                            errorCategory = ErrorCategory.SESSION,
                            onStateUpdate = { message ->
                                setState {
                                    SignInUiState.Error(
                                        form = form,
                                        message = message
                                    )
                                }
                            },
                            createSnackbarEvent = { message ->
                                SignInUiEvent.ShowSnackbar(message, isError = true)
                            }
                        )
                    }
                }
                else -> {
                    // Handle sign-in failure
                    handleOperationFailure(
                        result = result,
                        errorCategory = ErrorCategory.AUTHENTICATION,
                        onStateUpdate = { message ->
                            setState {
                                SignInUiState.Error(
                                    form = form,
                                    message = message
                                )
                            }
                        },
                        createSnackbarEvent = { message ->
                            SignInUiEvent.ShowSnackbar(message, isError = true)
                        }
                    )
                }
            }
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

    private fun handleSocialSignIn(provider: SocialProvider) {
        setState {
            SignInUiState.Loading.SocialAuthentication(
                form = form,
                provider = provider
            )
        }

        socialSignInJob?.cancel()
        socialSignInJob = viewModelScope.launch {
            // Execute social sign-in use case (which returns OperationResult)
            val result = socialSignInUseCase(provider)

            when (result) {
                is OperationResult.Success -> {
                    // User signed in successfully with social provider
                    val signIn = result.data

                    // Store user session
                    val saveResult = sessionRepository.saveSessionUserJson(signIn.toJson())

                    if (saveResult is OperationResult.Success) {
                        // Update state to next loading phase
                        setState { SignInUiState.Loading.SessionSaving(form = form) }

                        // Set session as active
                        val activeResult = sessionRepository.setSessionActive(true)

                        if (activeResult is OperationResult.Success) {
                            // Update state to final loading phase
                            setState { SignInUiState.Loading.SessionActivation(form = form) }

                            // All operations succeeded
                            emitEvent(
                                SignInUiEvent.SocialSignInResult(
                                    provider = provider,
                                    success = true
                                )
                            )
                            emitEvent(SignInUiEvent.NavigateToMainScreen)
                            setState { SignInUiState.Idle(form = SignInForm()) }
                        } else {
                            // Handle session activation failure
                            handleOperationFailure(
                                result = activeResult,
                                errorCategory = ErrorCategory.SESSION,
                                onStateUpdate = { message ->
                                    setState {
                                        SignInUiState.Error(
                                            form = form,
                                            message = message
                                        )
                                    }
                                },
                                createSnackbarEvent = { message ->
                                    SignInUiEvent.ShowSnackbar(message, isError = true)
                                },
                                additionalEvents = { message ->
                                    listOf(
                                        SignInUiEvent.SocialSignInResult(
                                            provider = provider,
                                            success = false,
                                            message = message
                                        )
                                    )
                                }
                            )
                        }
                    } else {
                        // Handle save session failure
                        handleOperationFailure(
                            result = saveResult,
                            errorCategory = ErrorCategory.SESSION,
                            onStateUpdate = { message ->
                                setState {
                                    SignInUiState.Error(
                                        form = form,
                                        message = message
                                    )
                                }
                            },
                            createSnackbarEvent = { message ->
                                SignInUiEvent.ShowSnackbar(message, isError = true)
                            },
                            additionalEvents = { message ->
                                listOf(
                                    SignInUiEvent.SocialSignInResult(
                                        provider = provider,
                                        success = false,
                                        message = message
                                    )
                                )
                            }
                        )
                    }
                }
                else -> {
                    // Handle social sign-in failure
                    handleOperationFailure(
                        result = result,
                        errorCategory = ErrorCategory.AUTHENTICATION,
                        onStateUpdate = { message ->
                            setState {
                                SignInUiState.Error(
                                    form = form,
                                    message = message
                                )
                            }
                        },
                        createSnackbarEvent = { message ->
                            SignInUiEvent.ShowSnackbar(message, isError = true)
                        },
                        additionalEvents = { message ->
                            listOf(
                                SignInUiEvent.SocialSignInResult(
                                    provider = provider,
                                    success = false,
                                    message = message
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        signInJob?.cancel()
        socialSignInJob?.cancel()
    }
}