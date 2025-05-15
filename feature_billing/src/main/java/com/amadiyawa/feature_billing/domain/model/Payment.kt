package com.amadiyawa.feature_billing.domain.model

import java.time.LocalDateTime

data class Payment(
    val id: String,
    val billId: String,
    val amount: Double,
    val date: LocalDateTime,
    val method: PaymentMethod,
    val reference: String
)

enum class PaymentMethod {
    MOBILE_MONEY,
    BANK_TRANSFER,
    CASH,
    ONLINE
}