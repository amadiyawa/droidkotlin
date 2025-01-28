package com.amadiyawa.feature_base.data.repository

import androidx.datastore.preferences.core.Preferences
import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl(
    private val dataStoreManager: DataStoreManager
) : DataStoreRepository {

    override suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        dataStoreManager.saveData(key, value)
    }

    override fun <T> getData(key: Preferences.Key<T>): Flow<T?> {
        return dataStoreManager.getData(key)
    }

    override suspend fun <T> clearData(key: Preferences.Key<T>) {
        dataStoreManager.clearData(key)
    }
}