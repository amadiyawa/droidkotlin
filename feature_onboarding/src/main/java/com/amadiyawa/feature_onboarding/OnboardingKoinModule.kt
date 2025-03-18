package com.amadiyawa.feature_onboarding

import com.amadiyawa.feature_onboarding.data.dataModule
import com.amadiyawa.feature_onboarding.domain.domainModule
import com.amadiyawa.feature_onboarding.presentation.presentationModule

val featureOnboardingModule = listOf(
    dataModule,
    domainModule,
    presentationModule
)