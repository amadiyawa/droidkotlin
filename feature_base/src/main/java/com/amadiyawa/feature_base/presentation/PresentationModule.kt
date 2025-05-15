package com.amadiyawa.feature_base.presentation

import com.amadiyawa.feature_base.common.resources.AndroidStringResourceProvider
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import com.amadiyawa.feature_base.presentation.navigation.NavigationRegistry
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import timber.log.Timber

internal val presentationModule = module {
    single<StringResourceProvider> { AndroidStringResourceProvider(androidContext()) }

    // Create singleton instance of NavigationRegistry
    single {
        NavigationRegistry().apply {
            Timber.d("NavigationRegistry initialized")
        }
    }
}