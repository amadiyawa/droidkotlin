package com.amadiyawa.feature_auth.domain

import com.amadiyawa.feature_auth.domain.usecase.SignInUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    singleOf(::SignInUseCase)
}