package com.amadiyawa.feature_base.domain

import com.amadiyawa.feature_base.domain.usecase.ValidateCmPhoneNumberUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { ValidateCmPhoneNumberUseCase() }
    single { ValidatePasswordUseCase() }
}