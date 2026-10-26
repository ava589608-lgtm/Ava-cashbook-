package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirestoreRepository
import com.example.model.CompanySettings
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class VehicleFinanceSummary(
    val plate: String,
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val net: Double = 0.0
)

data class FilterCriteria(
    val query: String = "",
    val account: String = "All",
    val type: String = "All",
    val category: String = "All",
    val vehicle: String = "All"
)

class CashBookViewModel(private val repository: FirestoreRepository) : ViewModel() {

    val transactions: StateFlow<List<TransactionRecord>> = repository.observeTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val vehicles: StateFlow<List<VehicleRecord>> = repository.observeVehicles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val settings: StateFlow<CompanySettings> = repository.observeSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), CompanySettings())

    // Filters
    val searchQuery = MutableStateFlow("")
    val accountFilter = MutableStateFlow("All") // "All", "Bank", "Petty"
    val typeFilter = MutableStateFlow("All") // "All", "in", "out", "xfer"
    val categoryFilter = MutableStateFlow("All")
    val vehicleFilter = MutableStateFlow("All")
    val dateFrom = MutableStateFlow("")
    val dateTo = MutableStateFlow("")

    private val filterCriteria = combine(
        searchQuery,
        accountFilter,
        typeFilter,
        categoryFilter,
        vehicleFilter
    ) { q, acc, typ, cat, veh ->
        FilterCriteria(q, acc, typ, cat, veh)
    }

    val filteredTransactions: StateFlow<List<TransactionRecord>> = combine(
        transactions,
        filterCriteria
    ) { txs, criteria ->
        txs.filter { tx ->
            val matchQuery = if (criteria.query.isBlank()) true else {
                tx.desc.contains(criteria.query, ignoreCase = true) ||
                tx.party.contains(criteria.query, ignoreCase = true) ||
                tx.category.contains(criteria.query, ignoreCase = true) ||
                tx.vehicle.contains(criteria.query, ignoreCase = true) ||
                tx.ref.contains(criteria.query, ignoreCase = true) ||
                tx.chequeNo.contains(criteria.query, ignoreCase = true)
            }
            val matchAccount = when (criteria.account) {
                "Bank" -> tx.account == "Bank"
                "Petty" -> tx.account == "Petty"
                else -> true
            }
            val matchType = if (criteria.type == "All") true else tx.type == criteria.type
            val matchCategory = if (criteria.category == "All") true else tx.category == criteria.category
            val matchVehicle = if (criteria.vehicle == "All") true else tx.vehicle == criteria.vehicle
            matchQuery && matchAccount && matchType && matchCategory && matchVehicle
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun saveTransaction(record: TransactionRecord, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveTransaction(record)
            onDone()
        }
    }

    fun deleteTransaction(id: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteTransaction(id)
            onDone()
        }
    }

    fun markChequeCleared(tx: TransactionRecord, clearedDate: String = LocalDate.now().toString()) {
        viewModelScope.launch {
            repository.saveTransaction(tx.copy(clearedOn = clearedDate))
        }
    }

    fun saveVehicle(vehicle: VehicleRecord, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveVehicle(vehicle)
            onDone()
        }
    }

    fun deleteVehicle(id: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteVehicle(id)
            onDone()
        }
    }

    fun saveSettings(newSettings: CompanySettings, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveSettings(newSettings)
            onDone()
        }
    }

    fun clearFilters() {
        searchQuery.value = ""
        accountFilter.value = "All"
        typeFilter.value = "All"
        categoryFilter.value = "All"
        vehicleFilter.value = "All"
        dateFrom.value = ""
        dateTo.value = ""
    }

    // Calculations
    fun getBankBalance(txs: List<TransactionRecord>, cfg: CompanySettings): Double {
        val totalIn = txs.filter { it.account == "Bank" && it.type == "in" }.sumOf { it.amount }
        val totalOut = txs.filter { it.account == "Bank" && it.type == "out" }.sumOf { it.amount }
        return cfg.bankOpeningBalance + totalIn - totalOut
    }

    fun getPettyBalance(txs: List<TransactionRecord>, cfg: CompanySettings): Double {
        val totalIn = txs.filter { it.account == "Petty" && it.type == "in" }.sumOf { it.amount }
        val totalOut = txs.filter { it.account == "Petty" && it.type == "out" }.sumOf { it.amount }
        return cfg.pettyOpeningBalance + totalIn - totalOut
    }

    fun getMonthStats(txs: List<TransactionRecord>, monthStr: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))): Triple<Double, Double, Double> {
        val monthTxs = txs.filter { it.date.startsWith(monthStr) }
        val totalIn = monthTxs.filter { it.type == "in" }.sumOf { it.amount }
        val totalOut = monthTxs.filter { it.type == "out" }.sumOf { it.amount }
        val net = totalIn - totalOut
        return Triple(totalIn, totalOut, net)
    }

    fun getUnclearedCheques(txs: List<TransactionRecord>): List<TransactionRecord> {
        return txs.filter { it.isCheque && !it.isCleared }
    }

    fun getVehicleSummaries(txs: List<TransactionRecord>, vehs: List<VehicleRecord>): List<VehicleFinanceSummary> {
        return vehs.map { v ->
            val vTxs = txs.filter { it.vehicle == v.plate }
            val inc = vTxs.filter { it.type == "in" }.sumOf { it.amount }
            val exp = vTxs.filter { it.type == "out" }.sumOf { it.amount }
            VehicleFinanceSummary(plate = v.plate, income = inc, expense = exp, net = inc - exp)
        }
    }

    fun exportCsvData(): String {
        val txs = transactions.value
        val sb = StringBuilder()
        sb.append("Date,Ref,Account,Type,Amount (AED),VAT (AED),Description,Party,Category,Subcategory,Vehicle,Cheque No.,Cleared On\n")
        for (t in txs) {
            val desc = t.desc.replace("\"", "\"\"")
            val party = t.party.replace("\"", "\"\"")
            sb.append("${t.date},${t.ref},${t.account},${t.type},${t.amount},${t.vat},\"$desc\",\"$party\",${t.category},${t.subcategory},${t.vehicle},${t.chequeNo},${t.clearedOn}\n")
        }
        return sb.toString()
    }
}
