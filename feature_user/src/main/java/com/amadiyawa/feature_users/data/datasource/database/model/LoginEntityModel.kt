package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Login
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginEntityModel(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

internal fun LoginEntityModel.toDomainModel() = Login(
    uuid = this.uuid,
    username = this.username,
    password = this.password,
    salt = this.salt,
    md5 = this.md5,
    sha1 = this.sha1,
    sha256 = this.sha256
)