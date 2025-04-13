package com.amadiyawa.feature_auth.presentation.screen.signin

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.domain.model.SignInRequest
import com.amadiyawa.feature_auth.domain.usecase.SignInUseCase
import com.amadiyawa.feature_auth.presentation.state.AuthStatus
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.usecase.ValidateIdentifierUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SignInViewModel(
    private val validateIdentifierUseCase: ValidateIdentifierUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val signInUseCase: SignInUseCase
) : BaseViewModel<SignInViewModel.UiState, SignInViewModel.Action>(UiState.Loading) {

    private val _form = MutableStateFlow(SignInForm())
    val form: StateFlow<SignInForm> = _form

    val validatedForm: StateFlow<ValidatedForm> = _form
        .map { it.asValidatedForm() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _form.value.asValidatedForm())

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting

    private var signInJob: Job? = null

    fun onEnter() {
        sendAction(Action.FormLoadSuccess)
    }

    fun onIdentifierChanged(input: String) {
        val result = validateIdentifierUseCase.execute(input)
        _form.update {
            it.copy(identifier = ValidatedField(
                value = input,
                validation = result
            ))
        }
    }

    fun onPasswordChanged(input: String) {
        val result = validatePasswordUseCase.execute(input)
        _form.update {
            it.copy(
                password = ValidatedField(
                    value = input,
                    validation = result
                )
            )
        }
    }

    fun onPasswordVisibilityToggle() {
        _form.update {
            it.copy(
                password = ValidatedField(
                    value = it.password.value,
                    validation = it.password.validation,
                    isValueHidden = !it.password.isValueHidden
                )
            )
        }
    }

    fun onIdentifierClear() {
        _form.update {
            it.copy(
                identifier = ValidatedField(value = "")
            )
        }
    }

    fun signIn() {
        val form = _form.value
        val validation = form.asValidatedForm()

        if (!validation.isValid) return

        _isSubmitting.value = true

        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            val result = signInUseCase(
                SignInRequest(
                    identifier = form.identifier.value,
                    password = form.password.value
                )
            )

            _isSubmitting.value = false

            val action = when (result) {
                is OperationResult.Success -> Action.SignIn(AuthStatus.Authenticated)
                is OperationResult.Failure -> Action.SignIn(AuthStatus.Invalid(result.message))
                is OperationResult.Error   -> Action.SignIn(AuthStatus.Error(result.throwable))
            }

            sendAction(action)
        }
    }

    internal sealed interface Action : BaseAction<UiState> {
        data object FormLoadSuccess : Action {
            override fun reduce(state: UiState) = UiState.Form(AuthStatus.Idle)
        }

        data class SignIn(val status: AuthStatus) : Action {
            override fun reduce(state: UiState): UiState {
                return when {
                    state is UiState.Form -> state.copy(status = status)
                    else -> state
                }
            }
        }
    }

    @Immutable
    internal sealed interface UiState : BaseState {
        data object Loading : UiState
        data class Form(val status: AuthStatus) : UiState
    }
}