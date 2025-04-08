package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.LoginEntityModel
import com.amadiyawa.feature_users.domain.model.Login
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginApiModel(
    @SerialName("uuid") val uuid: String,
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("salt") val salt: String,
    @SerialName("md5") val md5: String,
    @SerialName("sha1") val sha1: String,
    @SerialName("sha256") val sha256: String
)

internal fun LoginApiModel.toDomainModel() = Login(
    uuid = this.uuid,
    username = this.username,
    password = this.password,
    salt = this.salt,
    md5 = this.md5,
    sha1 = this.sha1,
    sha256 = this.sha256
)

internal fun LoginApiModel.toEntityModel() = LoginEntityModel(
    uuid = this.uuid,
    username = this.username,
    password = this.password,
    salt = this.salt,
    md5 = this.md5,
    sha1 = this.sha1,
    sha256 = this.sha256
)