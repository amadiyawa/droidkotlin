package com.amadiyawa.feature_billing.presentation.screen.billinglist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.presentation.compose.composable.ButtonIconType
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_base.presentation.compose.composable.ErrorScreen
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodySmall
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
import com.amadiyawa.feature_base.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_base.presentation.compose.composable.ToolbarParams
import com.amadiyawa.feature_billing.R
import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.model.BillStatus
import com.amadiyawa.feature_billing.presentation.state.BillFilter
import com.amadiyawa.feature_billing.presentation.state.InvoiceAction
import com.amadiyawa.feature_billing.presentation.state.InvoiceEvent
import com.amadiyawa.feature_billing.presentation.state.SortOrder
import com.amadiyawa.feature_billing.presentation.viewmodel.InvoiceViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceListScreen(
    onInvoiceClick: (String) -> Unit,
    viewModel: InvoiceViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // State for filter/sort bottom sheet
    var showFilterSheet by remember { mutableStateOf(false) }
    var isSearchVisible by remember { mutableStateOf(false) }
    val paymentSuccess = stringResource(R.string.payment_success)
    val dataRefreshed = stringResource(R.string.data_refreshed)

    // Handle events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is InvoiceEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is InvoiceEvent.NavigateToDetail -> {
                    onInvoiceClick(event.billId)
                }
                is InvoiceEvent.PaymentSuccess -> {
                    snackbarHostState.showSnackbar(paymentSuccess)
                }
                InvoiceEvent.DataRefreshed -> {
                    snackbarHostState.showSnackbar(dataRefreshed)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                params = ToolbarParams(
                    title = stringResource(R.string.bills)
                ),
                actions = {
                    CircularButton(
                        params = CircularButtonParams(
                            iconType = ButtonIconType.Vector(Icons.Default.Search),
                            onClick = { isSearchVisible = !isSearchVisible },
                            description = stringResource(R.string.search),
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            iconTint = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularButton(
                        params = CircularButtonParams(
                            iconType = ButtonIconType.Vector(Icons.Default.FilterList),
                            onClick = { showFilterSheet = true },
                            description = stringResource(R.string.filter),
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            iconTint = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        // Set up pull to refresh
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                viewModel.handleAction(InvoiceAction.RefreshData)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Search bar
                AnimatedVisibility(
                    visible = isSearchVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { query ->
                            viewModel.handleAction(InvoiceAction.SearchBills(query))
                        },
                        placeholder = { Text(stringResource(R.string.search_placeholder)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(R.string.search)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                }

                // Stats summary
                if (!uiState.isLoading && uiState.bills.isNotEmpty()) {
                    BillsSummary(
                        bills = uiState.bills,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Content
                when {
                    uiState.isLoading -> {
                        LoadingAnimation(visible = true)
                    }

                    uiState.error != null -> {
                        ErrorScreen(
                            error = uiState.error ?: "Unknown error occurred",
                            onRetry = { viewModel.handleAction(InvoiceAction.LoadBills) }
                        )
                    }

                    uiState.filteredBills.isEmpty() -> {
                        EmptyScreen(
                            title = stringResource(R.string.no_bills_found),
                            message = stringResource(R.string.no_bills_help)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                count = uiState.filteredBills.size,
                                key = { index -> uiState.filteredBills[index].id }
                            ) { index ->
                                val bill = uiState.filteredBills[index]
                                BillItem(
                                    bill = bill,
                                    onClick = {
                                        viewModel.handleAction(InvoiceAction.SelectBill(bill.id))
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Filter/Sort bottom sheet
    if (showFilterSheet) {
        FilterSortBottomSheet(
            currentFilter = uiState.activeFilter,
            currentSort = uiState.sortOrder,
            onFilterSelected = { filter ->
                viewModel.handleAction(InvoiceAction.SetFilter(filter))
            },
            onSortSelected = { sort ->
                viewModel.handleAction(InvoiceAction.SetSortOrder(sort))
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
private fun BillsSummary(
    bills: List<Bill>,
    modifier: Modifier = Modifier
) {
    val totalBills = bills.size
    val pendingBills = bills.count { it.status == BillStatus.PENDING }
    val overdueBills = bills.count { it.status == BillStatus.OVERDUE }
    val totalAmount = bills.sumOf { it.amount }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                label = stringResource(R.string.summary_total),
                value = totalBills.toString(),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            SummaryItem(
                label = stringResource(R.string.summary_pending),
                value = pendingBills.toString(),
                color = MaterialTheme.colorScheme.primary
            )
            SummaryItem(
                label = stringResource(R.string.summary_overdue),
                value = overdueBills.toString(),
                color = MaterialTheme.colorScheme.error
            )
            SummaryItem(
                label = stringResource(R.string.summary_amount),
                value = String.format(Locale.getDefault(), "%.2f", totalAmount),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextTitleLarge(
            text = value,
            color = color,
            fontWeight = FontWeight.Bold,
        )
        TextBodySmall(
            text = label,
            color = color.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun BillItem(
    bill: Bill,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row - Bill ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    TextTitleMedium(
                        text = stringResource(R.string.bill_title, bill.id),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextBodyMedium(
                        text = stringResource(R.string.client_label, bill.clientId),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(status = bill.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amount and Due Date Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TextBodySmall(
                        text = stringResource(R.string.amount_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextTitleMedium(
                        text = stringResource(R.string.fcfa_format, bill.amount),
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    TextBodySmall(
                        text = stringResource(R.string.due_date_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextBodyMedium(
                        text = bill.dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Billing Period Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TextBodySmall(
                        text = stringResource(R.string.period_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextBodyMedium(
                        text = stringResource(
                            R.string.period_format,
                            bill.periodStart.format(DateTimeFormatter.ofPattern("MMM dd")),
                            bill.periodEnd.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Consumption and Meter Reading Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TextBodySmall(
                        text = stringResource(R.string.consumption_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextBodyMedium(
                        text = stringResource(R.string.consumption_format, bill.consumption),
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    TextBodySmall(
                        text = stringResource(R.string.meter_reading_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextBodyMedium(
                        text = stringResource(R.string.meter_format, bill.meterReading)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: BillStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, text) = when (status) {
        BillStatus.PAID -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            stringResource(R.string.status_paid)
        )
        BillStatus.PENDING -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            stringResource(R.string.status_pending)
        )
        BillStatus.OVERDUE -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            stringResource(R.string.status_overdue)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSortBottomSheet(
    currentFilter: BillFilter,
    currentSort: SortOrder,
    onFilterSelected: (BillFilter) -> Unit,
    onSortSelected: (SortOrder) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_and_sort),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Filter section
            Text(
                text = stringResource(R.string.filter_by_status),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            BillFilter.entries.forEach { filter ->
                FilterChip(
                    selected = filter == currentFilter,
                    onClick = {
                        onFilterSelected(filter)
                        onDismiss()
                    },
                    label = { Text(getFilterLabel(filter)) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort section
            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SortOrder.entries.forEach { sort ->
                FilterChip(
                    selected = sort == currentSort,
                    onClick = {
                        onSortSelected(sort)
                        onDismiss()
                    },
                    label = { Text(getSortLabel(sort)) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun getFilterLabel(filter: BillFilter): String {
    return when (filter) {
        BillFilter.ALL -> stringResource(R.string.summary_total)
        BillFilter.PENDING -> stringResource(R.string.summary_pending)
        BillFilter.PAID -> stringResource(R.string.status_paid)
        BillFilter.OVERDUE -> stringResource(R.string.summary_overdue)
    }
}

@Composable
private fun getSortLabel(sort: SortOrder): String {
    return when (sort) {
        SortOrder.DATE_ASC -> stringResource(R.string.sort_date_asc)
        SortOrder.DATE_DESC -> stringResource(R.string.sort_date_desc)
        SortOrder.AMOUNT_ASC -> stringResource(R.string.sort_amount_asc)
        SortOrder.AMOUNT_DESC -> stringResource(R.string.sort_amount_desc)
    }
}