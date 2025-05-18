package com.amadiyawa.feature_profile.presentation

import com.amadiyawa.feature_base.presentation.navigation.FeatureNavigationApi
import com.amadiyawa.feature_base.presentation.navigation.NavigationRegistry
import com.amadiyawa.feature_profile.presentation.navigation.ProfileNavigationApi
import com.amadiyawa.feature_profile.presentation.screen.profilemain.ProfileMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import timber.log.Timber

internal val presentationModule = module {
    // Log module loading
    Timber.d("Loading profile feature module")

    // Register the navigation API
    single {
        ProfileNavigationApi()
    } bind FeatureNavigationApi::class

    // Register the feature in the navigation registry
    // This is done as a side effect when the module is loaded
    factory(named("profileFeatureRegistration")) {
        val registry = get<NavigationRegistry>()
        val api = get<ProfileNavigationApi>()
        registry.registerFeature(api)
        Timber.d("Registered ProfileNavigationApi with NavigationRegistry")

        // Return a dummy value to satisfy Koin
        true
    }

    viewModel {
        ProfileMainViewModel(
            getUserProfileUseCase = get(),
            refreshUserProfileUseCase = get(),
            checkUserPermissionsUseCase = get(),
            signOutUserUseCase = get(),
            errorHandler = get()
        )
    }
}