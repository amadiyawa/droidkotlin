package com.amadiyawa.feature_onboarding.domain

import com.amadiyawa.feature_onboarding.domain.usecase.GetOnboardListUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module //Koin Module

/**
 *  This module is responsible for defining the dependency injection for the domain
 *  layer, specifically for the `GetOnboardListUseCase`. It ensures that the use case
 *  has the necessary repository (`OnboardRepository`) injected and manages its lifecycle
 *  as a singleton. This allows the use case to be easily accessed and used throughout
 *  the application.
 */
internal val domainModule = module {
    singleOf(::GetOnboardListUseCase)
}