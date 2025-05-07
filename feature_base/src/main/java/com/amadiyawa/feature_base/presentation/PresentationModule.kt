package com.amadiyawa.feature_base.presentation

import com.amadiyawa.feature_base.common.resources.AndroidStringResourceProvider
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val presentationModule = module {
    single<StringResourceProvider> { AndroidStringResourceProvider(androidContext()) }
}