package com.amadiyawa.feature_base.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

abstract class BaseViewModel<State : BaseState, Action : BaseAction<State>>(initialState: State) :
    ViewModel() {

    private val _uiStateFlow = MutableStateFlow(initialState)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private var stateTimeTravelDebugger: StateTimeTravelDebugger? = null

    init {
        stateTimeTravelDebugger = StateTimeTravelDebugger(this::class.java.simpleName)
    }

    private var state by Delegates.observable(initialState) { _, old, new ->
        if (old != new) {
            viewModelScope.launch {
                _uiStateFlow.value = new
            }

            stateTimeTravelDebugger?.apply {
                addStateTransition(old, new)
                logLast()
            }
        }
    }

    protected fun sendAction(action: Action) {
        stateTimeTravelDebugger?.addAction(action)
        state = action.reduce(state)
    }
}