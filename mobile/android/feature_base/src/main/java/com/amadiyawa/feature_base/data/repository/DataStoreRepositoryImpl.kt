package com.amadiyawa.feature_base.data.repository

import androidx.datastore.preferences.core.Preferences
import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the DataStoreRepository interface.
 *
 * @property dataStoreManager The DataStoreManager instance used for DataStore operations.
 */
class DataStoreRepositoryImpl(
    private val dataStoreManager: DataStoreManager
) : DataStoreRepository {

    /**
     * Saves data to the DataStore.
     *
     * @param key The key used to store the data.
     * @param value The value to be stored.
     */
    override suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        dataStoreManager.saveData(key, value)
    }

    /**
     * Retrieves data from the DataStore.
     *
     * @param key The key used to retrieve the data.
     * @return A Flow emitting the stored value or null if not found.
     */
    override fun <T> getData(key: Preferences.Key<T>): Flow<T?> {
        return dataStoreManager.getData(key)
    }

    /**
     * Clears data from the DataStore.
     *
     * @param key The key used to clear the data.
     */
    override suspend fun <T> clearData(key: Preferences.Key<T>) {
        dataStoreManager.clearData(key)
    }
}