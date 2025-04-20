package com.amadiyawa.feature_auth.domain

import com.amadiyawa.feature_auth.domain.usecase.SignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.SignUpUseCase
import com.amadiyawa.feature_auth.domain.validation.OtpFormValidator
import com.amadiyawa.feature_auth.domain.validation.SignUpFormValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    singleOf(::SignInUseCase)
    singleOf(::SignUpUseCase)
    single {
        SignUpFormValidator(
            validateFullName = get(),
            validateUsername = get(),
            validateEmail = get(),
            validatePhone = get(),
            validatePassword = get(),
            validatePasswordConfirmation = get(),
            validateTermsAccepted = get()
        )
    }
    single {
        OtpFormValidator(
            validateOtpUseCase = get()
        )
    }
}