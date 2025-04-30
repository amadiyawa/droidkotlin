package com.amadiyawa.feature_onboarding.presentation.screen.onboarding

import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState
import com.amadiyawa.feature_onboarding.domain.model.OnboardingScreen

data class OnboardingUiState(
    val currentScreenIndex: Int = 0,
    val screens: List<OnboardingScreen> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : BaseState {
    val currentScreen: OnboardingScreen?
        get() = screens.getOrNull(currentScreenIndex)

    val isLastScreen: Boolean
        get() = currentScreenIndex == screens.size - 1

    val isFirstScreen: Boolean
        get() = currentScreenIndex == 0
}