package com.amadiyawa.feature_auth.domain

import com.amadiyawa.feature_auth.domain.usecase.CompleteSignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.CompleteSocialSignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.ForgotPasswordUseCase
import com.amadiyawa.feature_auth.domain.usecase.OtpVerificationUseCase
import com.amadiyawa.feature_auth.domain.usecase.ResendOtpUseCase
import com.amadiyawa.feature_auth.domain.usecase.SignInUseCase
import com.amadiyawa.feature_auth.domain.usecase.SocialSignInUseCase
import com.amadiyawa.feature_auth.domain.util.validation.ForgotPasswordFormValidator
import com.amadiyawa.feature_auth.domain.util.validation.SignUpFormValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    factory {
        SignInUseCase(
            authRepository = get()
        )
    }

    factory {
        SocialSignInUseCase(
            authRepository = get()
        )
    }

    factory {
        CompleteSignInUseCase(
            signInUseCase = get(),
            sessionRepository = get(),
            domainEventBus = get()
        )
    }

    factory {
        CompleteSocialSignInUseCase(
            socialSignInUseCase = get(),
            sessionRepository = get(),
            domainEventBus = get()
        )
    }
    singleOf(::ForgotPasswordUseCase)
    singleOf(::OtpVerificationUseCase)
    singleOf(::ResendOtpUseCase)

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
        ForgotPasswordFormValidator(
            validateEmailOrPhone = get()
        )
    }
}