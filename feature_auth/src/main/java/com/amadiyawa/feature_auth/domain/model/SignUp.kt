package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_base.common.util.generateRandomHash
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class SignUp(
    val verificationId: String,
    val recipient: String,
    var expiresIn: Int
) {
    companion object {
        fun random(): SignUp {
            val randomVerificationId = generateRandomHash(12)
            val randomRecipient = "+2376******${Random.nextInt(10, 99)}"
            val randomExpiresIn = 150

            return SignUp(
                verificationId = randomVerificationId,
                recipient = randomRecipient,
                expiresIn = randomExpiresIn
            )
        }
    }
}