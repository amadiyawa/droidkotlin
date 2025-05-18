package com.amadiyawa.feature_base.presentation.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.result.isAuthError
import com.amadiyawa.feature_base.domain.result.isNetworkError
import com.amadiyawa.feature_base.presentation.errorhandling.ErrorHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base class for ViewModels that manage UI state and actions.
 *
 * This abstract class provides a foundation for managing UI state, handling actions,
 * and emitting events in a structured way. It includes utilities for state management,
 * event emission, and safe coroutine launching.
 *
 * @param State The type of the UI state, which must extend `BaseState`.
 * @param Action The type of actions that can be dispatched to the ViewModel.
 * @param initialState The initial state of the ViewModel.
 */
abstract class BaseViewModel<State : BaseState, Action>(
    initialState: State,
    protected val errorHandler: ErrorHandler? = null
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(initialState)
    val uiStateFlow: StateFlow<State> = _uiStateFlow.asStateFlow()

    private val _events = Channel<Any>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var stateTimeTravelDebugger: StateTimeTravelDebugger? =
        StateTimeTravelDebugger(this::class.java.simpleName)

    protected var state: State
        get() = _uiStateFlow.value
        private set(value) {
            val old = _uiStateFlow.value
            if (old != value) {
                _uiStateFlow.value = value
                stateTimeTravelDebugger?.addStateTransition(old, value)
                stateTimeTravelDebugger?.logLast()
            }
        }

    /**
     * Handles the dispatching of actions to the ViewModel.
     *
     * This abstract function must be implemented by subclasses to define how
     * actions of type `Action` are processed and handled.
     *
     * @param action The action to be dispatched. It must be of the type `Action`.
     */
    abstract fun dispatch(action: Action)

    /**
     * Updates the current state of the ViewModel using a reducer function.
     *
     * This function applies the provided reducer to the current state to produce a new state.
     * If the new state is different from the old state, it updates the state, logs the state
     * transition using the state time travel debugger (if available), and emits the new state
     * to the `uiStateFlow`.
     *
     * @param reducer A function that takes the current state as input and returns the new state.
     */
    protected fun setState(reducer: (State) -> State) {
        val newState = reducer(state)
        val old = state

        if (old != newState) {
            _uiStateFlow.value = newState
            state = newState

            // Only try to log if we have a debugger and a last action
            stateTimeTravelDebugger?.let { debugger ->
                try {
                    debugger.addStateTransition(old, newState)
                    debugger.logLast()
                } catch (e: Exception) {
                    Timber.e(e, "Error in state travel debugger")
                }
            }
        }
    }

    /**
     * Logs an action to the state time travel debugger.
     *
     * This function records the provided action in the state time travel debugger
     * for debugging purposes. It ensures that the action is logged only if the
     * debugger is available.
     *
     * @param action The action to be logged. It must be of the type `Action`.
     */
    protected fun logAction(action: Action) {
        stateTimeTravelDebugger?.addAction(action as Any)
    }

    /**
     * Emits an event to the `events` flow.
     *
     * This function sends the provided event to the `Channel` backing the `events` flow.
     * It uses the `viewModelScope` to ensure the operation is performed within a coroutine.
     *
     * @param event The event to be emitted. It can be of any type.
     */
    protected fun emitEvent(event: Any) {
        viewModelScope.launch {
            _events.trySend(event)
        }
    }

    /**
     * Safely launches a coroutine within the `viewModelScope`.
     *
     * This function executes the provided suspending block within a coroutine and catches
     * any unhandled exceptions, logging them using Timber. It ensures that the coroutine
     * is safely executed without crashing the application.
     *
     * @param block A suspending lambda function to be executed.
     */
    protected fun launchSafely(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (throwable: Throwable) {
                Timber.e(throwable, "Unhandled exception")
            }
        }
    }

    protected fun <T> handleResult(
        result: OperationResult<T>,
        onSuccess: (T) -> Unit,
        onError: ((String) -> Unit)? = null,
        customErrorHandling: Boolean = false
    ) {
        when (result) {
            is OperationResult.Success -> {
                logAction("Result: Success" as Action)
                onSuccess(result.data)
            }
            is OperationResult.Failure,
            is OperationResult.Error -> {
                logError(result)

                if (customErrorHandling && onError != null) {
                    // Custom error handling
                    val errorMessage = errorHandler?.getLocalizedMessage(result)
                        ?: "Operation failed"
                    onError(errorMessage)
                } else {
                    // Default error handling
                    handleOperationError(result)
                }
            }
        }
    }

    protected open fun handleOperationError(result: OperationResult<*>) {
        when {
            result.isNetworkError() -> handleNetworkError()
            result.isAuthError() -> handleAuthError()
            else -> handleGenericError(result)
        }
    }

    protected open fun handleNetworkError() {
        val message = errorHandler?.getNetworkErrorMessage() ?: "Network error"
        emitEvent(BaseUiEvent.ShowSnackbar(message, isError = true))
        Timber.w("Network error detected")
    }

    protected open fun handleAuthError() {
        val message = errorHandler?.getAuthenticationErrorMessage() ?: "Authentication failed"
        emitEvent(BaseUiEvent.ShowSnackbar(message, isError = true))
        emitEvent(BaseUiEvent.NavigateToSignIn)
        Timber.w("Authentication error detected")
    }

    protected open fun handleGenericError(result: OperationResult<*>) {
        val message = errorHandler?.getLocalizedMessage(result) ?: "An error occurred"
        emitEvent(BaseUiEvent.ShowSnackbar(message, isError = true))
    }

    protected open fun handleUnexpectedError(throwable: Throwable) {
        val message = errorHandler?.getLocalizedMessage(
            OperationResult.Error(throwable = throwable)
        ) ?: "An unexpected error occurred"
        emitEvent(BaseUiEvent.ShowSnackbar(message, isError = true))
    }

    protected fun logError(result: OperationResult<*>, operation: String = "Operation") {
        when (result) {
            is OperationResult.Error -> {
                Timber.e(result.throwable, "$operation error: ${result.message} (code: ${result.code})")
            }
            is OperationResult.Failure -> {
                Timber.w("$operation failure: ${result.message} (code: ${result.code}, details: ${result.details})")
            }
            is OperationResult.Success -> { /* No logging needed */ }
        }
    }
}