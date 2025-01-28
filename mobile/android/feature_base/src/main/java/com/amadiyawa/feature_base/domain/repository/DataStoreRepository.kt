package com.amadiyawa.feature_base.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun <T> saveData(key: Preferences.Key<T>, value: T)
    fun <T> getData(key: Preferences.Key<T>): Flow<T?>
    suspend fun <T> clearData(key: Preferences.Key<T>)
}