package com.amadiyawa.feature_profile.domain

import com.amadiyawa.feature_profile.domain.usecase.CheckUserPermissionsUseCase
import com.amadiyawa.feature_profile.domain.usecase.GetUserProfileUseCase
import com.amadiyawa.feature_profile.domain.usecase.RefreshUserProfileUseCase
import com.amadiyawa.feature_profile.domain.usecase.SignOutUserUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val domainModule = module {
    factory {
        GetUserProfileUseCase(
            profileRepository = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }

    factory {
        RefreshUserProfileUseCase(
            profileRepository = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }

    factory {
        CheckUserPermissionsUseCase(
            profileRepository = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }

    factory {
        SignOutUserUseCase(
            userSessionManager = get(),
            domainEventBus = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }
}