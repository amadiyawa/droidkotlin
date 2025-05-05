package com.amadiyawa.feature_onboarding.presentation.screen.onboarding

import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import com.amadiyawa.feature_onboarding.domain.usecase.GetOnboardingUseCase
import kotlinx.coroutines.flow.first
import timber.log.Timber

internal class OnboardingViewModel(
    private val getOnboardingUseCase: GetOnboardingUseCase,
    private val dataStoreManager: DataStoreManager
) : BaseViewModel<OnboardingUiState, OnboardingAction>(OnboardingUiState()) {

    init {
        launchSafely {
            val isOnboardingComplete = dataStoreManager
                .getData(DataStoreManager.ONBOARDING_COMPLETED)
                .first() == true

            if (isOnboardingComplete) {
                emitEvent(OnboardingUiEvent.NavigateToAuth)
            } else {
                dispatch(OnboardingAction.LoadScreens)
            }
        }
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

            getOnboardingUseCase().also { result ->
                Timber.d("getOnboardListUseCase result: $result")

                setState { state ->
                    when (result) {
                        is OperationResult.Success -> {
                            state.copy(
                                screens = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is OperationResult.Error -> {
                            emitEvent(OnboardingUiEvent.ShowError(result.message!!))
                            state.copy(isLoading = false, error = result.message!!)
                        }
                        is OperationResult.Failure -> state
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
        launchSafely  {
            // Save onboarding completion status in DataStore
            dataStoreManager.saveData(DataStoreManager.ONBOARDING_COMPLETED, true)

            // Simply emit navigation event to move to the next module
            emitEvent(OnboardingUiEvent.NavigateToAuth)
        }
    }
}