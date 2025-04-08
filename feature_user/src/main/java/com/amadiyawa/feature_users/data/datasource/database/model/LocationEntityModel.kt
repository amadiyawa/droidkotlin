package com.amadiyawa.feature_users.data.datasource.database.model

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.amadiyawa.feature_users.domain.model.Location
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@TypeConverters(
    StreetEntityTypeConverter::class,
    CoordinatesEntityTypeConverter::class,
    TimezoneEntityTypeConverter::class
)
internal data class LocationEntityModel(
    val street: StreetEntityModel,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: CoordinatesEntityModel,
    val timezone: TimezoneEntityModel
)

internal fun LocationEntityModel.toDomainModel() = Location(
    street = this.street.toDomainModel(),
    city = this.city,
    state = this.state,
    country = this.country,
    postcode = this.postcode,
    coordinates = this.coordinates.toDomainModel(),
    timezone = this.timezone.toDomainModel()
)

internal class StreetEntityTypeConverter {
    @TypeConverter
    fun streetEntityToString(streetEntityModel: StreetEntityModel): String {
        return Json.encodeToString(streetEntityModel)
    }

    @TypeConverter
    fun stringToStreetEntity(streetEntityModel: String): StreetEntityModel {
        return Json.decodeFromString(streetEntityModel)
    }
}

internal class CoordinatesEntityTypeConverter {
    @TypeConverter
    fun coordinateEntityToString(coordinateEntityModel: CoordinatesEntityModel): String {
        return Json.encodeToString(coordinateEntityModel)
    }

    @TypeConverter
    fun stringToCoordinateEntity(coordinateEntityModel: String): CoordinatesEntityModel {
        return Json.decodeFromString(coordinateEntityModel)
    }
}

internal class TimezoneEntityTypeConverter {
    @TypeConverter
    fun timezoneEntityToString(timezoneEntityModel: TimezoneEntityModel): String {
        return Json.encodeToString(timezoneEntityModel)
    }

    @TypeConverter
    fun stringToTimezoneEntity(timezoneEntityModel: String): TimezoneEntityModel {
        return Json.decodeFromString(timezoneEntityModel)
    }
}