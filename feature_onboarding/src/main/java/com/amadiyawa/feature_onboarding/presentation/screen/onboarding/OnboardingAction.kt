package com.amadiyawa.feature_onboarding.presentation.screen.onboarding

import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

sealed class OnboardingAction : BaseAction {
    object LoadScreens : OnboardingAction()
    object NextScreen : OnboardingAction()
    object PreviousScreen : OnboardingAction()
    object SkipOnboarding : OnboardingAction()
    object CompleteOnboarding : OnboardingAction()
    data class GoToScreen(val index: Int) : OnboardingAction()
}