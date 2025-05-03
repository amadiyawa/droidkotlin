package com.amadiyawa.feature_base.presentation.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.util.ErrorCategory
import com.amadiyawa.paygo.base.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

abstract class BaseViewModel<State : BaseState, Action>(
    initialState: State,
    private val errorMessageMapper: ErrorMessageMapper
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

    protected val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    abstract fun dispatch(action: Action)

    // In BaseViewModel
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

    protected fun logAction(action: Action) {
        stateTimeTravelDebugger?.addAction(action as Any)
    }

    protected fun emitEvent(event: Any) {
        viewModelScope.launch {
            _events.trySend(event)
        }
    }

    protected fun launchSafely(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (throwable: Throwable) {
                Timber.e(throwable, "Unhandled exception")
                // You can add error handling logic here if needed
            }
        }
    }

    protected fun handleOperationFailure(
        result: OperationResult<*>,
        defaultMessageResId: Int = R.string.error_unknown,
        errorCategory: ErrorCategory? = null,
        onStateUpdate: (String) -> Unit,
        createSnackbarEvent: (String) -> Any? = { null },
        additionalEvents: (String) -> List<Any> = { emptyList() }
    ) {
        val errorMessage = when (result) {
            is OperationResult.Failure ->
                errorMessageMapper.getMessage(
                    result.code,
                    defaultMessageResId,
                    errorCategory
                )
            is OperationResult.Error ->
                result.throwable?.message ?:
                errorMessageMapper.getMessage(null, defaultMessageResId)
            else ->
                errorMessageMapper.getMessage(null, defaultMessageResId)
        }

        // Log the error
        when (result) {
            is OperationResult.Failure -> {
                Timber.e("Operation failed with code: ${result.code}, message: $errorMessage")
            }
            is OperationResult.Error -> {
                Timber.e(result.throwable, "Operation failed with exception: $errorMessage")
            }
            else -> {
                Timber.e("Operation failed: $errorMessage")
            }
        }

        // Update state
        onStateUpdate(errorMessage)

        // Create snackbar event
        val snackbarEvent = createSnackbarEvent(errorMessage)
        if (snackbarEvent != null) {
            emitEvent(snackbarEvent)
        }

        // Emit additional events
        additionalEvents(errorMessage).forEach { event ->
            emitEvent(event)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stateTimeTravelDebugger = null
    }
}