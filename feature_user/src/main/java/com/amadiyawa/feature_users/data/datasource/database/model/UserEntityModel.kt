package com.amadiyawa.feature_users.data.datasource.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.amadiyawa.feature_users.domain.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "users")
@TypeConverters(
    NameEntityTypeConverter::class,
    LocationEntityTypeConverter::class,
    LoginEntityTypeConverter::class,
    DobEntityTypeConverter::class,
    RegisteredEntityTypeConverter::class,
    IdEntityTypeConverter::class,
    PictureEntityTypeConverter::class
)
internal data class UserEntityModel(
    @PrimaryKey
    val uuid: String,
    val gender: String,
    val name: NameEntityModel,
    val location: LocationEntityModel,
    val email: String,
    val login: LoginEntityModel,
    val dob: DobEntityModel,
    val registered: RegisteredEntityModel,
    val phone: String,
    val cell: String,
    val id: IdEntityModel,
    val picture: PictureEntityModel,
    val nat: String
)

internal fun UserEntityModel.toDomainModel() = User(
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

internal class NameEntityTypeConverter {
    @TypeConverter
    fun nameEntityToString(nameEntityModel: NameEntityModel): String {
        return Json.encodeToString(nameEntityModel)
    }

    @TypeConverter
    fun stringToNameString(nameEntityModel: String): NameEntityModel {
        return Json.decodeFromString(nameEntityModel)
    }
}

internal class LocationEntityTypeConverter {
    @TypeConverter
    fun locationEntityToString(locationEntityModel: LocationEntityModel): String {
        return Json.encodeToString(locationEntityModel)
    }

    @TypeConverter
    fun stringToLocationEntity(locationEntityModel: String): LocationEntityModel {
        return Json.decodeFromString(locationEntityModel)
    }
}

internal class LoginEntityTypeConverter {
    @TypeConverter
    fun loginEntityToString(loginEntityModel: LoginEntityModel): String {
        return Json.encodeToString(loginEntityModel)
    }

    @TypeConverter
    fun stringToLoginEntity(loginEntityModel: String): LoginEntityModel {
        return Json.decodeFromString(loginEntityModel)
    }
}

internal class DobEntityTypeConverter {
    @TypeConverter
    fun dobEntityToString(dobEntityModel: DobEntityModel): String {
        return Json.encodeToString(dobEntityModel)
    }

    @TypeConverter
    fun stringToDobEntity(dobEntityModel: String): DobEntityModel {
        return Json.decodeFromString(dobEntityModel)
    }
}

internal class RegisteredEntityTypeConverter {
    @TypeConverter
    fun registeredEntityToString(registeredEntityModel: RegisteredEntityModel): String {
        return Json.encodeToString(registeredEntityModel)
    }

    @TypeConverter
    fun stringToRegisteredEntity(registeredEntityModel: String): RegisteredEntityModel {
        return Json.decodeFromString(registeredEntityModel)
    }
}

internal class IdEntityTypeConverter {
    @TypeConverter
    fun idEntityToString(idEntityModel: IdEntityModel): String {
        return Json.encodeToString(idEntityModel)
    }

    @TypeConverter
    fun stringToIdEntity(idEntityModel: String): IdEntityModel {
        return Json.decodeFromString(idEntityModel)
    }
}

internal class PictureEntityTypeConverter {
    @TypeConverter
    fun pictureEntityToString(pictureEntityModel: PictureEntityModel): String {
        return Json.encodeToString(pictureEntityModel)
    }

    @TypeConverter
    fun stringToPictureEntity(pictureEntityModel: String): PictureEntityModel {
        return Json.decodeFromString(pictureEntityModel)
    }
}