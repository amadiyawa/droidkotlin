package com.amadiyawa.feature_base.domain

import com.amadiyawa.feature_base.domain.usecase.ValidateIdentifierUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val domainModule = module {
    single { ValidateIdentifierUseCase(androidContext()) }
    single { ValidatePasswordUseCase(androidContext()) }
}