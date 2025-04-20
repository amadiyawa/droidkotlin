package com.amadiyawa.feature_base.presentation.screen.viewmodel

import timber.log.Timber
import kotlin.reflect.full.memberProperties

class StateTimeTravelDebugger(
    private val viewClassName: String
) {

    private val stateTimeline = mutableListOf<StateTransition>()
    private var lastViewAction: Any? = null

    fun addAction(viewAction: Any) {
        lastViewAction = viewAction
    }

    fun addStateTransition(oldState: BaseState, newState: BaseState) {
        val viewAction = checkNotNull(lastViewAction) {
            "lastViewAction is null. Please call addAction() before addStateTransition()"
        }

        stateTimeline.add(StateTransition(oldState, viewAction, newState))
        lastViewAction = null
    }

    private fun getMessage(): String = getMessage(stateTimeline)

    private fun getMessage(transitions: List<StateTransition>): String {
        if (transitions.isEmpty()) return "$viewClassName has no state transitions\n"

        return buildString {
            transitions.forEach { transition ->
                appendLine("🔁 Action: $viewClassName.${transition.action::class.simpleName}")
                propertyNames.forEach { propertyName ->
                    append(getLogLine(transition.oldState, transition.newState, propertyName))
                }
            }
        }
    }

    fun logAll() {
        Timber.d(getMessage())
    }

    fun logLast() {
        stateTimeline.lastOrNull()?.let { last ->
            Timber.d(getMessage(listOf(last)))
        }
    }

    private val propertyNames: List<String>
        get() = stateTimeline
            .firstOrNull()
            ?.oldState
            ?.javaClass
            ?.kotlin
            ?.memberProperties
            ?.map { it.name }
            ?: emptyList()

    private fun getLogLine(oldState: BaseState, newState: BaseState, propertyName: String): String {
        val oldValue = getPropertyValue(oldState, propertyName)
        val newValue = getPropertyValue(newState, propertyName)
        val indent = "\t"

        return if (oldValue != newValue) {
            "$indent*$propertyName: $oldValue ➜ $newValue\n"
        } else {
            "$indent$propertyName: $newValue\n"
        }
    }

    private fun getPropertyValue(state: BaseState, propertyName: String): String {
        return state::class.memberProperties
            .firstOrNull { it.name == propertyName }
            ?.getter
            ?.call(state)
            ?.toString()
            ?.ifBlank { "\"\"" }
            ?: ""
    }

    private data class StateTransition(
        val oldState: BaseState,
        val action: Any,
        val newState: BaseState
    )
}
