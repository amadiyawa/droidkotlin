package com.amadiyawa.feature_auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * Data class representing the response for a verification request.
 *
 * This class is used to deserialize the JSON response from the server
 * into a Kotlin object. It contains information about the verification ID,
 * the recipient of the verification, and the expiration time of the verification.
 *
 * @property verificationId The unique identifier for the verification process.
 * @property recipient The recipient of the verification (e.g., email or phone number).
 * @property expiresIn The time in seconds until the verification expires.
 */
@Serializable
data class VerificationResponse(
    @SerialName("verificationId")
    val verificationId: String,

    @SerialName("recipient")
    val recipient: String,

    @SerialName("expiresIn")
    val expiresIn: Int,

    @SerialName("purpose")
    val purpose: String = "SIGN_UP",

    @SerialName("metadata")
    val metadata: Map<String, String> = emptyMap(),
) {
    companion object {
        private val charPool: List<Char> =
            ('a'..'z') + ('A'..'Z') + ('0'..'9')

        fun random(purpose: String): VerificationResponse {
            return if (Random.nextBoolean()) {
                randomEmailVerification(purpose)
            } else {
                randomSmsVerification(purpose)
            }
        }

        fun randomEmailVerification(purpose: String): VerificationResponse {
            val domains = listOf("gmail.com", "yahoo.com", "proton.me", "company.io")
            val username = (1..10).map { charPool.random() }.joinToString("")
            val domain = domains.random()
            val fullEmail = "$username@$domain"

            return VerificationResponse(
                verificationId = "${purpose.lowercase()}_${generateRandomToken(12)}",
                recipient = maskEmail(fullEmail),
                expiresIn = Random.nextInt(300, 3601) // 5-60 minutes
            )
        }

        fun randomSmsVerification(purpose: String): VerificationResponse {
            val countryCode = listOf("+1", "+44", "+33", "+49", "+81").random()
            val phoneNumber = (1..10).joinToString("") { Random.nextInt(0, 9).toString() }

            return VerificationResponse(
                verificationId = "${purpose.lowercase()}_${generateRandomToken(8)}",
                recipient = maskPhone("$countryCode$phoneNumber"),
                expiresIn = Random.nextInt(120, 301) // 2-5 minutes
            )
        }

        private fun generateRandomToken(length: Int): String {
            return (1..length).map { charPool.random() }.joinToString("")
        }

        private fun maskEmail(email: String): String {
            val (name, domain) = email.split('@')
            val maskedName = name.take(1) + "***" + name.takeLast(1).takeIf { it.isNotEmpty() }
            val (domainName, tld) = domain.split('.')
            val maskedDomain = domainName.take(1) + "***"
            return "$maskedName@$maskedDomain.$tld"
        }

        private fun maskPhone(phone: String): String {
            return if (phone.startsWith('+')) {
                phone.take(3) + "****" + phone.takeLast(3)
            } else {
                "****" + phone.takeLast(4)
            }
        }
    }
}