package com.amadiyawa.feature_billing.domain.model

import java.time.LocalDate

data class Bill(
    val id: String,
    val clientId: String,
    val amount: Double,
    val dueDate: LocalDate,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val meterReading: Double,
    val consumption: Double,
    val status: BillStatus
)

enum class BillStatus {
    PENDING,
    PAID,
    OVERDUE
}