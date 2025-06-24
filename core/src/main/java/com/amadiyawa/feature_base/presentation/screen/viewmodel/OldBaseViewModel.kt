package com.amadiyawa.feature_base.presentation.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

abstract class OldBaseViewModel<State : BaseState, Action : OldBaseAction<State>>(initialState: State) :
    ViewModel() {

    private val _uiStateFlow = MutableStateFlow(initialState)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private var oldStateTimeTravelDebugger: OldStateTimeTravelDebugger? = null

    init {
        oldStateTimeTravelDebugger = OldStateTimeTravelDebugger(this::class.java.simpleName)
    }

    private var state by Delegates.observable(initialState) { _, old, new ->
        if (old != new) {
            viewModelScope.launch {
                _uiStateFlow.value = new
            }

            oldStateTimeTravelDebugger?.apply {
                addStateTransition(old, new)
                logLast()
            }
        }
    }

    protected fun sendAction(action: Action) {
        oldStateTimeTravelDebugger?.addAction(action)
        state = action.reduce(state)
    }
}