package com.amadiyawa.feature_users.data.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * `PostcodeSerializer` is an object that implements the `KSerializer` interface with `String` as the type parameter.
 * This object is used to customize the serialization and deserialization process of the `postcode` field in the JSON data.
 *
 * The `serialize` method is used to convert the `postcode` value from the data class into a JSON string.
 * The `deserialize` method is used to convert the `postcode` value from the JSON data into a `String` in the data class.
 *
 * In the `deserialize` method, it first tries to decode the value as an `Int` and convert it to a `String`.
 * If that fails (throws an Exception), it then tries to decode it as a `String`.
 * If both attempts fail, it returns an empty string.
 */
object PostcodeSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("postcode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return try {
            decoder.decodeInt().toString()
        } catch (e: Exception) {
            try {
                decoder.decodeString()
            } catch (e: Exception) {
                ""
            }
        }
    }
}