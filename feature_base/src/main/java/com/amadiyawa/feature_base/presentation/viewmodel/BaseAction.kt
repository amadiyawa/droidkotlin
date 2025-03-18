package com.amadiyawa.feature_base.presentation.viewmodel

/**
 * The BaseAction interface represents an action that can be performed on a state.
 *
 * This is a generic interface, which means it can work with any type of state.
 * The state could represent the state of an application or a part of an application.
 *
 * @param State The type of the state that this action operates on.
 */
interface BaseAction<State> {

    /**
     * The reduce function takes the current state of the application, performs some operation on it,
     * and returns the new state.
     *
     * This is a common pattern in state management, particularly in Redux-style architectures.
     *
     * @param state The current state of the application.
     * @return The new state of the application after the action has been performed.
     */
    fun reduce(state: State): State
}