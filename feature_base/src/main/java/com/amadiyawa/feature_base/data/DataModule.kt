package com.amadiyawa.feature_base.data

import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.data.mapper.ErrorMessageMapperImpl
import com.amadiyawa.feature_base.data.repository.DataStoreRepositoryImpl
import com.amadiyawa.feature_base.data.repository.SessionRepositoryImpl
import com.amadiyawa.feature_base.data.resources.ResourceProviderImpl
import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.domain.repository.DataStoreRepository
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.resources.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val dataModule = module {
    single { DataStoreManager(context = get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(dataStoreManager = get()) }
    single<SessionRepository> { SessionRepositoryImpl(dataStoreRepository = get()) }
    single<ResourceProvider> { ResourceProviderImpl(context = androidContext()) }
    single<ErrorMessageMapper> { ErrorMessageMapperImpl(resourceProvider = get()) }
}