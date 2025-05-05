package com.amadiyawa.feature_auth.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

/**
 * Represents the result of an authentication operation.
 *
 * This data class contains information about the authenticated user,
 * the authentication tokens, and optional metadata.
 *
 * @property user The authenticated user details.
 * @property token The authentication tokens associated with the user.
 * @property metadata Optional metadata as a map of key-value pairs,
 * where the value can be of any type.
 */
@Serializable
data class AuthResult(
    val user: User,
    val token: AuthTokens,
    val metadata: Map<String, @Serializable(AnySerializer::class) Any>? = null,
)

/**
 * Converts the `AuthResult` object to its JSON string representation.
 *
 * This function uses the Kotlinx Serialization library to encode the `AuthResult` instance
 * into a JSON string. It is configured to ignore unknown keys and encode default values.
 *
 * @return A JSON string representation of the `AuthResult` object.
 */
fun AuthResult.toJson(): String {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    return json.encodeToString(AuthResult.serializer(), this)
}

/**
 * A custom serializer for handling `Any` type values.
 *
 * This object provides serialization and deserialization logic for values of type `Any`.
 * It supports primitive types such as `String`, `Number`, and `Boolean`, and encodes
 * other types as their string representation.
 */
object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    /**
     * Serializes a value of type `Any` into its corresponding JSON representation.
     *
     * @param encoder The encoder used to write the serialized value.
     * @param value The value to be serialized.
     */
    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is String -> encoder.encodeString(value)
            is Number -> encoder.encodeDouble(value.toDouble())
            is Boolean -> encoder.encodeBoolean(value)
            else -> encoder.encodeString(value.toString())
        }
    }

    /**
     * Deserializes a JSON value into an `Any` type.
     *
     * @param decoder The decoder used to read the serialized value.
     * @return The deserialized value as an `Any` type.
     */
    override fun deserialize(decoder: Decoder): Any {
        return decoder.decodeString()
    }
}