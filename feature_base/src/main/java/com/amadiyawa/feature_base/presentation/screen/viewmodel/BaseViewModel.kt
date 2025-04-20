package com.amadiyawa.feature_base.presentation.screen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State : BaseState, Action>(
    initialState: State
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(initialState)
    val uiStateFlow: StateFlow<State> = _uiStateFlow.asStateFlow()

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

    abstract fun dispatch(action: Action)

    protected fun setState(reducer: (State) -> State) {
        state = reducer(state)
    }

    protected fun logAction(action: Action) {
        stateTimeTravelDebugger?.addAction(action as Any)
    }
}