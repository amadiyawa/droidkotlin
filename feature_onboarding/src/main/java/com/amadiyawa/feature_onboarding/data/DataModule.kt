package com.amadiyawa.feature_onboarding.data

import com.amadiyawa.feature_onboarding.data.repository.OnboardingRepositoryImpl
import com.amadiyawa.feature_onboarding.domain.repository.OnboardingRepository
import org.koin.dsl.module // Koin for Dependency Injection (DI)

/**
 * This is a Koin module that provides the Dependency Injection (DI)
 * for the OnboardRepository. It binds the interface OnboardRepository
 * to its implementation OnboardRepositoryImpl, ensuring that the
 * OnboardRepositoryImpl instance is injected whenever OnboardRepository
 * is required.
 */
internal val dataModule = module {

    //Bind the OnboardRepository interface to its implementation
    single<OnboardingRepository> {
        OnboardingRepositoryImpl()  // Provides a single instance of OnboardRepositoryImpl
    }
}