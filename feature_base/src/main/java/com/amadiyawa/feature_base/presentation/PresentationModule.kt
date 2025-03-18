package com.amadiyawa.feature_base.presentation

import com.amadiyawa.feature_base.presentation.util.ResourceProvider
import org.koin.dsl.module

internal val presentationModule = module {
    single { ResourceProvider(get()) }
}