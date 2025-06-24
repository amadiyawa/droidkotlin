package com.amadiyawa.feature_base.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

/**
 * Extension property to create a DataStore instance with the name "droidkotlin_prefs".
 */
private val Context.dataStore by preferencesDataStore("droidkotlin_prefs")

/**
 * Manager class for handling DataStore operations.
 *
 * @property context The context used to access the DataStore.
 * @author Amadou Iyawa
 */
class DataStoreManager(private val context: Context) {
    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val IS_USER_SIGNED_IN = booleanPreferencesKey("is_user_signed_in")
        val SIGNED_USER_DATA = stringPreferencesKey("signed_user_data")
    }

    /**
     * Saves data to the DataStore.
     *
     * @param key The key used to store the data.
     * @param value The value to be stored.
     */
    suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    /**
     * Retrieves data from the DataStore.
     *
     * @param key The key used to retrieve the data.
     * @return A Flow emitting the stored value or null if not found.
     */
    fun <T> getData(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key]
            }
    }

    /**
     * Clears data from the DataStore.
     *
     * @param key The key used to clear the data.
     */
    suspend fun <T> clearData(key: Preferences.Key<T>) {
        context.dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}