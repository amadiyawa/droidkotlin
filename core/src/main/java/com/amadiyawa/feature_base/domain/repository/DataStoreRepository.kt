package com.amadiyawa.feature_base.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing data storage operations using DataStore.
 *
 * This interface provides methods to save, retrieve, and clear data in a type-safe manner
 * using `Preferences.Key` as the identifier for stored values.
 */
interface DataStoreRepository {
    /**
     * Saves a value associated with the given key in the DataStore.
     *
     * @param T The type of the value to be saved.
     * @param key The key used to identify the value in the DataStore.
     * @param value The value to be saved.
     */
    suspend fun <T> saveData(key: Preferences.Key<T>, value: T)

    /**
     * Retrieves a value associated with the given key from the DataStore.
     *
     * @param T The type of the value to be retrieved.
     * @param key The key used to identify the value in the DataStore.
     * @return A `Flow` emitting the value associated with the key, or `null` if not found.
     */
    fun <T> getData(key: Preferences.Key<T>): Flow<T?>

    /**
     * Clears the value associated with the given key from the DataStore.
     *
     * @param T The type of the value to be cleared.
     * @param key The key used to identify the value in the DataStore.
     */
    suspend fun <T> clearData(key: Preferences.Key<T>)
}