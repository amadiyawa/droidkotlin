package com.amadiyawa.feature_base.data

import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.data.repository.DataStoreRepositoryImpl
import com.amadiyawa.feature_base.domain.repository.DataStoreRepository
import org.koin.dsl.module

internal val dataModule = module {
    single { DataStoreManager(context = get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(dataStoreManager = get()) }
}