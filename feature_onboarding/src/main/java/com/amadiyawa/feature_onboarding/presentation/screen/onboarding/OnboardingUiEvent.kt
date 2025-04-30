package com.amadiyawa.feature_onboarding.presentation.screen.onboarding

sealed class OnboardingUiEvent {
    object NavigateToAuth : OnboardingUiEvent()
    data class ShowError(val message: String) : OnboardingUiEvent()
}