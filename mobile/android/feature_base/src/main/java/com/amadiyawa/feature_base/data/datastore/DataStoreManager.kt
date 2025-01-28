package com.amadiyawa.feature_base.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val IS_USER_SIGNED_IN = booleanPreferencesKey("is_user_signed_in")
        val SIGNED_USER_DATA = stringPreferencesKey("signed_user_data")
    }

    suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> getData(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    suspend fun <T> clearData(key: Preferences.Key<T>) {
        context.dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}