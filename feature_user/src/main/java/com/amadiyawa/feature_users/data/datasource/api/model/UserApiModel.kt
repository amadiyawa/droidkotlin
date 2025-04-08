package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.UserEntityModel
import com.amadiyawa.feature_users.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserApiModel(
    @SerialName("gender") val gender : String,
    @SerialName("name") val name : NameApiModel,
    @SerialName("location") val location : LocationApiModel,
    @SerialName("email") val email : String,
    @SerialName("login") val login : LoginApiModel,
    @SerialName("dob") val dob : DobApiModel,
    @SerialName("registered") val registered : RegisteredApiModel,
    @SerialName("phone") val phone : String,
    @SerialName("cell") val cell : String,
    @SerialName("id") val id : IdApiModel,
    @SerialName("picture") val picture : PictureApiModel,
    @SerialName("nat") val nat : String
)

internal fun UserApiModel.toDomainModel() = User(
    gender = this.gender,
    name = this.name.toDomainModel(),
    location = this.location.toDomainModel(),
    email = this.email,
    login = this.login.toDomainModel(),
    dob = this.dob.toDomainModel(),
    registered = this.registered.toDomainModel(),
    phone = this.phone,
    cell = this.cell,
    id = this.id.toDomainModel(),
    picture = this.picture.toDomainModel(),
    nat = this.nat
)

internal fun UserApiModel.toEntityModel() = UserEntityModel(
    uuid = this.login.uuid,
    gender = this.gender,
    name = this.name.toEntityModel(),
    location = this.location.toEntityModel(),
    email = this.email,
    login = this.login.toEntityModel(),
    dob = this.dob.toEntityModel(),
    registered = this.registered.toEntityModel(),
    phone = this.phone,
    cell = this.cell,
    id = this.id.toEntityModel(),
    picture = this.picture.toEntityModel(),
    nat = this.nat
)