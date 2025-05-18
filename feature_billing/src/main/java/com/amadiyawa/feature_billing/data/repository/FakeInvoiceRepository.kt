package com.amadiyawa.feature_billing.data.repository

import com.amadiyawa.feature_base.domain.manager.UserSessionManager
import com.amadiyawa.feature_base.domain.util.UserRole
import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.model.BillStatus
import com.amadiyawa.feature_billing.domain.model.Payment
import com.amadiyawa.feature_billing.domain.model.PaymentMethod
import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

class FakeInvoiceRepository(
    private val userSessionManager: UserSessionManager
) : InvoiceRepository {

    private val allBills = MutableStateFlow<List<Bill>>(emptyList())
    private val payments = MutableStateFlow<List<Payment>>(emptyList())

    init {
        // Initialize bills based on the current user
        initializeBills()
    }

    private fun initializeBills() {
        val currentUserId = userSessionManager.currentUserId.value
        val currentRole = userSessionManager.currentRole.value

        allBills.value = generateFakeBills(currentUserId, currentRole)
        payments.value = generateFakePayments()
    }

    override fun getBills(): Flow<List<Bill>> = allBills.map { bills ->
        val currentUserId = userSessionManager.currentUserId.value
        val currentRole = userSessionManager.currentRole.value

        // If user changed, regenerate bills
        if (bills.isEmpty() || (currentRole == UserRole.CLIENT && bills.none { it.clientId == currentUserId })) {
            initializeBills()
            allBills.value
        } else {
            when (currentRole) {
                UserRole.CLIENT -> {
                    // Clients only see their own bills
                    bills.filter { it.clientId == currentUserId }
                }
                UserRole.AGENT,
                UserRole.ADMIN -> {
                    // Admin and Agent can see all bills
                    bills
                }
                else -> emptyList()
            }
        }
    }

    override fun getBillById(id: String): Flow<Bill?> = allBills.map { billList ->
        val currentUserId = userSessionManager.currentUserId.value
        val currentRole = userSessionManager.currentRole.value

        val bill = billList.find { it.id == id }

        when (currentRole) {
            UserRole.CLIENT -> {
                // Clients can only view their own bills
                if (bill?.clientId == currentUserId) bill else null
            }
            UserRole.AGENT,
            UserRole.ADMIN -> {
                // Admin and Agent can view any bill
                bill
            }
            else -> null
        }
    }

    override fun getPaymentsForBill(billId: String): Flow<List<Payment>> = payments.map { paymentList ->
        val currentUserId = userSessionManager.currentUserId.value
        val currentRole = userSessionManager.currentRole.value

        // First check if user has access to this bill
        val bill = allBills.value.find { it.id == billId }

        when (currentRole) {
            UserRole.CLIENT -> {
                // Clients can only see payments for their own bills
                if (bill?.clientId == currentUserId) {
                    paymentList.filter { it.billId == billId }
                } else {
                    emptyList()
                }
            }
            UserRole.AGENT,
            UserRole.ADMIN -> {
                // Admin and Agent can see all payments
                paymentList.filter { it.billId == billId }
            }
            else -> emptyList()
        }
    }

    override suspend fun generatePayment(
        billId: String,
        amount: Double,
        method: PaymentMethod
    ): Result<Payment> {
        return try {
            val bill = allBills.value.find { it.id == billId }
                ?: throw Exception("Bill not found")

            // Check if user has permission to make payment for this bill
            val currentUserId = userSessionManager.currentUserId.value
            val currentRole = userSessionManager.currentRole.value

            val hasPermission = when (currentRole) {
                UserRole.CLIENT -> bill.clientId == currentUserId
                UserRole.AGENT, UserRole.ADMIN -> true
                else -> false
            }

            if (!hasPermission) {
                throw Exception("Unauthorized to make payment for this bill")
            }

            val newPayment = Payment(
                id = UUID.randomUUID().toString(),
                billId = billId,
                amount = amount,
                date = LocalDateTime.now(),
                method = method,
                reference = "PAY-${System.currentTimeMillis()}"
            )

            // Update payments list
            payments.value = payments.value + newPayment

            // Update bill status
            updateBillStatus(billId, BillStatus.PAID)

            Result.success(newPayment)
        } catch (e: Exception) {
            Timber.e(e, "Error generating payment")
            Result.failure(e)
        }
    }

    override suspend fun updateBillStatus(billId: String, status: BillStatus): Result<Unit> {
        return try {
            val currentRole = userSessionManager.currentRole.value

            val hasPermission = when (currentRole) {
                UserRole.CLIENT -> false // Clients cannot update bill status
                UserRole.AGENT, UserRole.ADMIN -> true
                else -> false
            }

            if (!hasPermission) {
                throw Exception("Unauthorized to update bill status")
            }

            allBills.value = allBills.value.map { b ->
                if (b.id == billId) b.copy(status = status) else b
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating bill status")
            Result.failure(e)
        }
    }

    override suspend fun refreshData(): Result<Unit> {
        return try {
            initializeBills()
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing data")
            Result.failure(e)
        }
    }

    private fun generateFakeBills(currentUserId: String?, currentRole: UserRole?): List<Bill> {
        val today = LocalDate.now()

        return when (currentRole) {
            UserRole.CLIENT -> {
                // For clients, generate bills only for the current user
                if (currentUserId != null) {
                    List(20) { index ->
                        createBill(index, currentUserId, today)
                    }
                } else {
                    emptyList()
                }
            }
            UserRole.AGENT, UserRole.ADMIN -> {
                // For admin/agent, generate bills for multiple clients
                val clientIds = listOf("U001", "U002", "U003", "U004", "U005")
                List(50) { index ->
                    val clientId = clientIds[index % clientIds.size]
                    createBill(index, clientId, today)
                }
            }
            else -> emptyList()
        }
    }

    private fun createBill(index: Int, clientId: String, today: LocalDate): Bill {
        val dueDate = today.plusDays(Random.nextLong(-30, 30))
        val periodStart = dueDate.minusMonths(1)
        val periodEnd = dueDate.minusDays(1)
        val consumption = Random.nextDouble(10.0, 500.0)
        val amount = consumption * 0.5 // Simple pricing model

        val status = when {
            dueDate.isBefore(today) && Random.nextBoolean() -> BillStatus.OVERDUE
            index % 3 == 0 -> BillStatus.PAID
            else -> BillStatus.PENDING
        }

        return Bill(
            id = "BILL-${100 + index}",
            clientId = clientId,
            amount = amount,
            dueDate = dueDate,
            periodStart = periodStart,
            periodEnd = periodEnd,
            meterReading = 1000.0 + consumption,
            consumption = consumption,
            status = status
        )
    }

    private fun generateFakePayments(): List<Payment> {
        val paidBills = allBills.value.filter { it.status == BillStatus.PAID }

        return paidBills.map { bill ->
            val paymentDate = bill.dueDate.atTime(
                Random.nextInt(9, 17),
                Random.nextInt(0, 59)
            )

            Payment(
                id = UUID.randomUUID().toString(),
                billId = bill.id,
                amount = bill.amount,
                date = paymentDate,
                method = PaymentMethod.entries[Random.nextInt(PaymentMethod.entries.size)],
                reference = "REF-${System.currentTimeMillis() - Random.nextLong(0, 100000000)}"
            )
        }
    }
}