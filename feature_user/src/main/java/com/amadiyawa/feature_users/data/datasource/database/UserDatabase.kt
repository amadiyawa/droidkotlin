package com.amadiyawa.feature_users.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amadiyawa.feature_users.data.datasource.database.model.CoordinatesEntityTypeConverter
import com.amadiyawa.feature_users.data.datasource.database.model.StreetEntityTypeConverter
import com.amadiyawa.feature_users.data.datasource.database.model.TimezoneEntityTypeConverter
import com.amadiyawa.feature_users.data.datasource.database.model.UserEntityModel

@Database(entities = [UserEntityModel::class], version = 1, exportSchema = false)
internal abstract class UserDatabase : RoomDatabase() {
    abstract fun users(): UserDao
}