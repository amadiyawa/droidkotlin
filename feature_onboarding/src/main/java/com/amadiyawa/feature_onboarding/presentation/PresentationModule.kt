package com.amadiyawa.feature_onboarding.presentation

import com.amadiyawa.feature_onboarding.presentation.screen.onboarding.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::OnboardingViewModel)
}