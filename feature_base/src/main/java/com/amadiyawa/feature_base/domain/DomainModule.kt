package com.amadiyawa.feature_base.domain

import com.amadiyawa.feature_base.domain.usecase.ValidateEmailOrPhoneUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateEmailUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateFullNameUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateIdentifierUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateOtpUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordConfirmationUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePhoneUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateTermsAcceptedUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateUsernameUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val domainModule = module {
    single { ValidateFullNameUseCase(androidContext()) }
    single { ValidateUsernameUseCase(androidContext()) }
    single { ValidateEmailUseCase(androidContext()) }
    single { ValidatePhoneUseCase(androidContext()) }
    single {
        ValidateIdentifierUseCase(
            validateEmail = get(),
            validateUsername = get(),
            validatePhone = get(),
            context = androidContext()
        )
    }
    single {
        ValidateEmailOrPhoneUseCase(
            validateEmail = get(),
            validatePhone = get(),
            context = androidContext()
        )
    }
    single { ValidatePasswordUseCase(androidContext()) }
    single { ValidatePasswordConfirmationUseCase(androidContext()) }
    single { ValidateTermsAcceptedUseCase(androidContext()) }
    single { ValidateOtpUseCase(androidContext()) }
}