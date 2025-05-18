package com.amadiyawa.feature_profile.data

import com.amadiyawa.feature_profile.data.repository.ProfileRepositoryImpl
import com.amadiyawa.feature_profile.domain.repository.ProfileRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            sessionRepository = get(),
            userSessionManager = get(),
            domainEventBus = get(),
            json = get(),
            ioDispatcher = get(qualifier = named("ioDispatcher"))
        )
    }
}