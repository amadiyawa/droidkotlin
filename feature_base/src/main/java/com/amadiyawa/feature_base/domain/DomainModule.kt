package com.amadiyawa.feature_base.domain

import com.amadiyawa.feature_base.domain.event.DomainEventBus
import com.amadiyawa.feature_base.domain.manager.UserSessionManager
import com.amadiyawa.feature_base.domain.usecase.ValidateEmailOrPhoneUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateEmailUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateFullNameUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordConfirmationUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePhoneUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateTermsAcceptedUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateUsernameUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val domainModule = module {
    single { ValidateFullNameUseCase(androidContext()) }
    single { ValidateUsernameUseCase(androidContext()) }
    single { ValidateEmailUseCase(androidContext()) }
    single { ValidatePhoneUseCase(androidContext()) }
    single { ValidateTermsAcceptedUseCase(androidContext()) }
    single {
        ValidateEmailOrPhoneUseCase(
            validateEmail = get(),
            validatePhone = get(),
            context = androidContext()
        )
    }
    single { ValidatePasswordUseCase(androidContext()) }
    single { ValidatePasswordConfirmationUseCase(androidContext()) }

    // Add dispatchers for dependency injection
    factory(qualifier = named("ioDispatcher")) { Dispatchers.IO }
    factory(qualifier = named("defaultDispatcher")) { Dispatchers.Default }
    factory(qualifier = named("mainDispatcher")) { Dispatchers.Main }

    // Add UserSessionManager
    single {
        UserSessionManager(
            sessionRepository = get(),
            domainEventBus = get(),
            json = get(),
            defaultDispatcher = get(qualifier = named("defaultDispatcher"))
        )
    }

    single { DomainEventBus() }
}