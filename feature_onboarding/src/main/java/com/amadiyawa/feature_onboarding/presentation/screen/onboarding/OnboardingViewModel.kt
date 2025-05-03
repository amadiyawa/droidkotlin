package com.amadiyawa.feature_onboarding.presentation.screen.onboarding

import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import com.amadiyawa.feature_onboarding.domain.usecase.GetOnboardListUseCase
import timber.log.Timber

internal class OnboardingViewModel(
    private val getOnboardListUseCase: GetOnboardListUseCase,
    errorMessageMapper: ErrorMessageMapper
) : BaseViewModel<OnboardingUiState, OnboardingAction>(
    OnboardingUiState(),
    errorMessageMapper
) {

    init {
        dispatch(OnboardingAction.LoadScreens)
    }

    override fun dispatch(action: OnboardingAction) {
        logAction(action)
        when (action) {
            is OnboardingAction.LoadScreens -> loadScreens()
            is OnboardingAction.NextScreen -> goToNextScreen()
            is OnboardingAction.PreviousScreen -> goToPreviousScreen()
            is OnboardingAction.SkipOnboarding -> completeOnboarding()
            is OnboardingAction.CompleteOnboarding -> completeOnboarding()
            is OnboardingAction.GoToScreen -> goToScreen(action.index)
        }
    }

    private fun loadScreens() {
        launchSafely {
            setState { it.copy(isLoading = true, error = null) }

            getOnboardListUseCase().also { result ->
                Timber.d("getOnboardListUseCase result: $result")

                setState { state ->
                    when (result) {
                        is OperationResult.Success -> {
                            if (result.data.isEmpty()) {
                                emitEvent(OnboardingUiEvent.ShowError("No onboarding screens found"))
                                state.copy(isLoading = false, error = "No onboarding screens found")
                            } else {
                                state.copy(
                                    screens = result.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        is OperationResult.Failure -> {
                            val errorMessage = result.message ?: "Failed to load onboarding screens"
                            emitEvent(OnboardingUiEvent.ShowError(errorMessage))
                            state.copy(isLoading = false, error = errorMessage)
                        }

                        is OperationResult.Error -> {
                            val exception = result.throwable
                            val errorMessage = exception?.message ?: "An unexpected error occurred"
                            Timber.e(exception, "Error loading onboarding screens")
                            emitEvent(OnboardingUiEvent.ShowError(errorMessage))
                            state.copy(isLoading = false, error = errorMessage)
                        }
                    }
                }
            }
        }
    }

    private fun goToNextScreen() {
        val currentState = state
        if (currentState.isLastScreen) {
            completeOnboarding()
        } else {
            setState { it.copy(currentScreenIndex = it.currentScreenIndex + 1) }
        }
    }

    private fun goToPreviousScreen() {
        setState { currentState ->
            if (!currentState.isFirstScreen) {
                currentState.copy(currentScreenIndex = currentState.currentScreenIndex - 1)
            } else {
                currentState
            }
        }
    }

    private fun goToScreen(index: Int) {
        setState { currentState ->
            if (index in 0 until currentState.screens.size) {
                currentState.copy(currentScreenIndex = index)
            } else {
                Timber.w("Attempted to navigate to invalid screen index: $index")
                currentState
            }
        }
    }

    private fun completeOnboarding() {
        // Simply emit navigation event to move to the next module
        emitEvent(OnboardingUiEvent.NavigateToAuth)
    }
}