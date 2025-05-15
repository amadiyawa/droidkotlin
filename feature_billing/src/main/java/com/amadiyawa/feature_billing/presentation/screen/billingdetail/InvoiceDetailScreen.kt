package com.amadiyawa.feature_billing.presentation.screen.billingdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodySmall
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleSmall
import com.amadiyawa.feature_base.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_base.presentation.compose.composable.ToolbarParams
import com.amadiyawa.feature_billing.R
import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.model.BillStatus
import com.amadiyawa.feature_billing.domain.model.Payment
import com.amadiyawa.feature_billing.domain.model.PaymentMethod
import com.amadiyawa.feature_billing.presentation.state.InvoiceAction
import com.amadiyawa.feature_billing.presentation.state.InvoiceEvent
import com.amadiyawa.feature_billing.presentation.viewmodel.InvoiceViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items

@Composable
fun InvoiceDetailScreen(
    invoiceId: String,
    onBackClick: () -> Unit,
    viewModel: InvoiceViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPaymentDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val paymentStatusDetail = stringResource(R.string.payment_success_detail)

    // Load bill details when screen opens
    LaunchedEffect(invoiceId) {
        viewModel.handleAction(InvoiceAction.SelectBill(invoiceId))
    }

    // Handle events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is InvoiceEvent.PaymentSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = paymentStatusDetail,
                        duration = SnackbarDuration.Short
                    )
                    showPaymentDialog = false
                }
                is InvoiceEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                params = ToolbarParams(
                    title = stringResource(R.string.bill_detail),
                    showBackButton = true,
                    onBackPressed = onBackClick
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingAnimation(visible = true)
            }
            uiState.selectedBill == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.bill_not_found))
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Bill Status Section
                    item {
                        BillStatusCard(bill = uiState.selectedBill!!)
                    }

                    // Bill Details Section
                    item {
                        BillDetailsCard(bill = uiState.selectedBill!!)
                    }

                    // Consumption Details Section
                    item {
                        ConsumptionCard(bill = uiState.selectedBill!!)
                    }

                    // Payment Action Section (if not paid)
                    if (uiState.selectedBill!!.status != BillStatus.PAID) {
                        item {
                            PaymentActionCard(
                                bill = uiState.selectedBill!!,
                                onPayClick = { showPaymentDialog = true }
                            )
                        }
                    }

                    // Payment History Section
                    if (uiState.payments.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.payment_history),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(uiState.payments) { payment ->
                            PaymentHistoryItem(payment = payment)
                        }
                    }
                }
            }
        }

        // Payment Dialog
        if (showPaymentDialog && uiState.selectedBill != null) {
            PaymentMethodDialog(
                bill = uiState.selectedBill!!,
                onDismiss = { showPaymentDialog = false },
                onConfirm = { paymentMethod ->
                    viewModel.handleAction(
                        InvoiceAction.GeneratePayment(
                            billId = invoiceId,
                            amount = uiState.selectedBill!!.amount,
                            method = paymentMethod
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun BillStatusCard(bill: Bill) {
    val (backgroundColor, icon, title, subtitle) = when (bill.status) {
        BillStatus.PAID -> Tuple4(
            MaterialTheme.colorScheme.primaryContainer,
            Icons.Default.CheckCircle,
            stringResource(R.string.bill_paid_title),
            stringResource(R.string.bill_paid_subtitle)
        )
        BillStatus.PENDING -> Tuple4(
            MaterialTheme.colorScheme.secondaryContainer,
            Icons.Default.Info,
            stringResource(R.string.bill_pending_title),
            stringResource(R.string.bill_pending_subtitle, bill.dueDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
        )
        BillStatus.OVERDUE -> Tuple4(
            MaterialTheme.colorScheme.errorContainer,
            Icons.Default.Warning,
            stringResource(R.string.bill_overdue_title),
            stringResource(R.string.bill_overdue_subtitle)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                TextTitleMedium(
                    text = title,
                    fontWeight = FontWeight.Bold
                )
                TextBodyMedium(text = subtitle)
            }
        }
    }
}

@Composable
private fun BillDetailsCard(bill: Bill) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.bill_information),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DetailRow(
                label = stringResource(R.string.bill_id_label),
                value = bill.id
            )
            DetailRow(
                label = stringResource(R.string.client_id_label),
                value = bill.clientId
            )
            DetailRow(
                label = stringResource(R.string.billing_period_label),
                value = stringResource(
                    R.string.period_format,
                    bill.periodStart.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    bill.periodEnd.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                )
            )
            DetailRow(
                label = stringResource(R.string.due_date_label),
                value = bill.dueDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            DetailRow(
                label = stringResource(R.string.total_amount_label),
                value = stringResource(R.string.fcfa_format, bill.amount),
                isHighlighted = true
            )
        }
    }
}

@Composable
private fun ConsumptionCard(bill: Bill) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.consumption_details),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConsumptionItem(
                    label = stringResource(R.string.meter_reading_label),
                    value = stringResource(R.string.meter_format, bill.meterReading),
                    modifier = Modifier.weight(1f)
                )
                ConsumptionItem(
                    label = stringResource(R.string.consumption_label),
                    value = stringResource(R.string.consumption_format, bill.consumption),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ConsumptionItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextBodySmall(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextTitleLarge(
            text = value,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun PaymentActionCard(
    bill: Bill,
    onPayClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.payment_required),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.amount_due, bill.amount),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onPayClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(stringResource(R.string.pay_now))
            }
        }
    }
}

@Composable
private fun PaymentHistoryItem(payment: Payment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TextTitleSmall(
                    text = stringResource(R.string.payment_reference, payment.reference),
                    fontWeight = FontWeight.Medium
                )
                TextBodySmall(
                    text = payment.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextBodySmall(
                    text = getPaymentMethodLabel(payment.method),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = stringResource(R.string.fcfa_format, payment.amount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodDialog(
    bill: Bill,
    onDismiss: () -> Unit,
    onConfirm: (PaymentMethod) -> Unit
) {
    var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.select_payment_method))
        },
        text = {
            Column {
                TextTitleSmall(
                    text = stringResource(R.string.amount_to_pay, bill.amount),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                PaymentMethod.entries.forEach { method ->
                    FilterChip(
                        selected = selectedMethod == method,
                        onClick = { selectedMethod = method },
                        label = { Text(getPaymentMethodLabel(method)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedMethod?.let { onConfirm(it) }
                },
                enabled = selectedMethod != null
            ) {
                Text(stringResource(R.string.confirm_payment))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextBodyMedium(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (isHighlighted) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun getPaymentMethodLabel(method: PaymentMethod): String {
    return when (method) {
        PaymentMethod.MOBILE_MONEY -> stringResource(R.string.payment_mobile_money)
        PaymentMethod.BANK_TRANSFER -> stringResource(R.string.payment_bank_transfer)
        PaymentMethod.CASH -> stringResource(R.string.payment_cash)
        PaymentMethod.ONLINE -> stringResource(R.string.payment_online)
    }
}

// Helper data class for status card
private data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)