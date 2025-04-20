package com.amadiyawa.feature_onboarding.presentation

import com.amadiyawa.feature_onboarding.presentation.screen.onboardlist.OnboardViewModelOld
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Registers the OnboardViewModel and ensures that Koin automatically injects all necessary dependencies.
 * The 'get()' function retrieves the dependency (e.g., 'GetOnboardListUseCase') from the Koin container
 * and passes it to the ViewModel.
 **/
internal val presentationModule = module {
    // Instead of using :: which passes the class directly, we use get() to retreive the values from properties or to get dependencies from Koin
    viewModel { OnboardViewModelOld(get()) }
}
