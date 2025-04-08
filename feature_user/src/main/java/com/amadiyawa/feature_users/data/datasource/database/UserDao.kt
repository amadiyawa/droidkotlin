package com.amadiyawa.feature_users.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amadiyawa.feature_users.data.datasource.database.model.UserEntityModel

@Dao
internal interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntityModel>

    @Query("SELECT * FROM users WHERE uuid = :uuid")
    suspend fun getUserByUuid(uuid: String): UserEntityModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntityModel>)
}