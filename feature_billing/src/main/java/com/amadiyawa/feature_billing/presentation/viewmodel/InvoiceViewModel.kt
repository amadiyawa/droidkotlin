package com.amadiyawa.feature_billing.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.model.BillStatus
import com.amadiyawa.feature_billing.domain.model.PaymentMethod
import com.amadiyawa.feature_billing.domain.usecase.GeneratePaymentUseCase
import com.amadiyawa.feature_billing.domain.usecase.GetBillByIdUseCase
import com.amadiyawa.feature_billing.domain.usecase.GetBillsUseCase
import com.amadiyawa.feature_billing.domain.usecase.GetPaymentsForBillUseCase
import com.amadiyawa.feature_billing.domain.usecase.RefreshInvoiceDataUseCase
import com.amadiyawa.feature_billing.domain.usecase.UpdateBillStatusUseCase
import com.amadiyawa.feature_billing.presentation.state.BillFilter
import com.amadiyawa.feature_billing.presentation.state.InvoiceAction
import com.amadiyawa.feature_billing.presentation.state.InvoiceEvent
import com.amadiyawa.feature_billing.presentation.state.InvoiceUiState
import com.amadiyawa.feature_billing.presentation.state.SortOrder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class InvoiceViewModel(
    private val getBillsUseCase: GetBillsUseCase,
    private val getBillByIdUseCase: GetBillByIdUseCase,
    private val getPaymentsForBillUseCase: GetPaymentsForBillUseCase,
    private val generatePaymentUseCase: GeneratePaymentUseCase,
    private val updateBillStatusUseCase: UpdateBillStatusUseCase,
    private val refreshInvoiceDataUseCase: RefreshInvoiceDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvoiceUiState())
    val uiState: StateFlow<InvoiceUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<InvoiceEvent>()
    val events = _events.asSharedFlow()

    init {
        loadBills()
    }

    fun handleAction(action: InvoiceAction) {
        when (action) {
            is InvoiceAction.LoadBills -> loadBills()
            is InvoiceAction.SetFilter -> setFilter(action.filter)
            is InvoiceAction.SearchBills -> searchBills(action.query)
            is InvoiceAction.SelectBill -> selectBill(action.billId)
            is InvoiceAction.SetSortOrder -> setSortOrder(action.sortOrder)
            is InvoiceAction.RefreshData -> refreshData()
            is InvoiceAction.ClearSelection -> clearSelection()
            is InvoiceAction.GeneratePayment -> generatePayment(action.billId, action.amount, action.method)
        }
    }

    private fun loadBills() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getBillsUseCase().collectLatest { bills ->
                    _uiState.update { currentState ->
                        val filteredBills = filterBills(bills, currentState.activeFilter, currentState.searchQuery)
                        val sortedBills = sortBills(filteredBills, currentState.sortOrder)
                        currentState.copy(
                            isLoading = false,
                            bills = bills,
                            filteredBills = sortedBills,
                            error = null
                        )
                    }
                }
            } catch (error: Exception) {
                Timber.e(error, "Error loading bills")
                _uiState.update { it.copy(isLoading = false, error = error.message) }
                _events.emit(InvoiceEvent.ShowError("Failed to load bills: ${error.message}"))
            }
        }
    }

    private fun selectBill(billId: String) {
        viewModelScope.launch {
            try {
                // Fetch bill data once
                val bill = getBillByIdUseCase(billId).first()

                if (bill != null) {
                    _uiState.update { it.copy(selectedBill = bill) }

                    // Fetch payments once
                    val payments = getPaymentsForBillUseCase(billId).first()
                    _uiState.update { it.copy(payments = payments) }

                    // Navigate to detail screen
                    _events.emit(InvoiceEvent.NavigateToDetail(billId))
                } else {
                    _events.emit(InvoiceEvent.ShowError("Bill not found"))
                }
            } catch (e: Exception) {
                _events.emit(InvoiceEvent.ShowError("Error loading bill: ${e.message}"))
            }
        }
    }

    private fun setFilter(filter: BillFilter) {
        _uiState.update { currentState ->
            val filteredBills = filterBills(currentState.bills, filter, currentState.searchQuery)
            val sortedBills = sortBills(filteredBills, currentState.sortOrder)
            currentState.copy(
                activeFilter = filter,
                filteredBills = sortedBills
            )
        }
    }

    private fun searchBills(query: String) {
        _uiState.update { currentState ->
            val filteredBills = filterBills(currentState.bills, currentState.activeFilter, query)
            val sortedBills = sortBills(filteredBills, currentState.sortOrder)
            currentState.copy(
                searchQuery = query,
                filteredBills = sortedBills
            )
        }
    }

    private fun generatePayment(billId: String, amount: Double, method: PaymentMethod) {
        viewModelScope.launch {
            generatePaymentUseCase(billId, amount, method)
                .onSuccess {
                    _events.emit(InvoiceEvent.PaymentSuccess(billId))
                    loadBills() // Refresh the list
                }
                .onFailure { error ->
                    _events.emit(InvoiceEvent.ShowError("Payment failed: ${error.message}"))
                }
        }
    }

    private fun setSortOrder(sortOrder: SortOrder) {
        _uiState.update { currentState ->
            val sortedBills = sortBills(currentState.filteredBills, sortOrder)
            currentState.copy(
                sortOrder = sortOrder,
                filteredBills = sortedBills
            )
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            refreshInvoiceDataUseCase()
                .onSuccess {
                    _events.emit(InvoiceEvent.DataRefreshed)
                    loadBills()
                }
                .onFailure { error ->
                    _events.emit(InvoiceEvent.ShowError("Failed to refresh data: ${error.message}"))
                }

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun clearSelection() {
        _uiState.update { it.copy(selectedBill = null, payments = emptyList()) }
    }

    private fun filterBills(
        bills: List<Bill>,
        filter: BillFilter,
        query: String
    ): List<Bill> {
        // First apply status filter
        val statusFiltered = when (filter) {
            BillFilter.ALL -> bills
            BillFilter.PENDING -> bills.filter { it.status == BillStatus.PENDING }
            BillFilter.PAID -> bills.filter { it.status == BillStatus.PAID }
            BillFilter.OVERDUE -> bills.filter { it.status == BillStatus.OVERDUE }
        }

        // Then apply search query
        return if (query.isBlank()) {
            statusFiltered
        } else {
            statusFiltered.filter {
                it.id.contains(query, ignoreCase = true) ||
                        it.clientId.contains(query, ignoreCase = true)
            }
        }
    }

    private fun sortBills(
        bills: List<Bill>,
        sortOrder: SortOrder
    ): List<Bill> {
        return when (sortOrder) {
            SortOrder.DATE_ASC -> bills.sortedBy { it.dueDate }
            SortOrder.DATE_DESC -> bills.sortedByDescending { it.dueDate }
            SortOrder.AMOUNT_ASC -> bills.sortedBy { it.amount }
            SortOrder.AMOUNT_DESC -> bills.sortedByDescending { it.amount }
        }
    }
}