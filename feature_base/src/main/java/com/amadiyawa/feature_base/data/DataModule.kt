package com.amadiyawa.feature_base.data

import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.data.repository.AndroidErrorLocalizer
import com.amadiyawa.feature_base.data.repository.DataStoreRepositoryImpl
import com.amadiyawa.feature_base.data.repository.SessionRepositoryImpl
import com.amadiyawa.feature_base.domain.repository.DataStoreRepository
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import org.koin.dsl.module

internal val dataModule = module {
    single { DataStoreManager(context = get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(dataStoreManager = get()) }
    single<SessionRepository> {
        SessionRepositoryImpl(dataStoreRepository = get(), errorLocalizer = get())
    }
    single<ErrorLocalizer> { AndroidErrorLocalizer(context = get()) }
}