package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.AuthManager
import com.example.data.FirestoreRepository
import com.example.model.TransactionRecord
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppTab
import com.example.ui.components.AppTopBar
import com.example.ui.components.EntryDialog
import com.example.ui.components.VoucherDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CashBookScreen
import com.example.ui.screens.ChequesScreen
import com.example.ui.screens.FleetScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaCashTheme
import com.example.viewmodel.CashBookViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authManager = AuthManager(this)
        authManager.attemptAutoSignIn(lifecycleScope)

        setContent {
            AvaCashTheme {
                val currentUser by authManager.currentUser.collectAsStateWithLifecycle()

                if (currentUser == null) {
                    AuthScreen(
                        authManager = authManager,
                        onAuthSuccess = {}
                    )
                } else {
                    val repository = remember { FirestoreRepository(this) }
                    val viewModel: CashBookViewModel = viewModel(
                        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                return CashBookViewModel(repository) as T
                            }
                        }
                    )

                    MainCashBookApp(
                        viewModel = viewModel,
                        userEmail = currentUser?.email,
                        onSignOut = {
                            lifecycleScope.launch {
                                authManager.signOut(lifecycleScope) {}
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainCashBookApp(
    viewModel: CashBookViewModel,
    userEmail: String?,
    onSignOut: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(AppTab.HOME) }

    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val filteredTransactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val vehicles by viewModel.vehicles.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val accountFilter by viewModel.accountFilter.collectAsStateWithLifecycle()
    val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()

    val bankBalance = viewModel.getBankBalance(transactions, settings)
    val pettyBalance = viewModel.getPettyBalance(transactions, settings)

    // Dialog States
    var showEntryDialog by remember { mutableStateOf(false) }
    var entryDialogInitialType by remember { mutableStateOf("out") }
    var editingTransaction by remember { mutableStateOf<TransactionRecord?>(null) }
    var viewingVoucherTransaction by remember { mutableStateOf<TransactionRecord?>(null) }

    Scaffold(
        topBar = {
            AppTopBar(
                role = settings.role,
                userEmail = userEmail
            )
        },
        bottomBar = {
            AppBottomBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        floatingActionButton = {
            // Staff can only create entries if allowed; Admin can always create
            if (settings.role != "Check") {
                FloatingActionButton(
                    onClick = {
                        editingTransaction = null
                        entryDialogInitialType = if (settings.role == "Staff") "out" else "out"
                        showEntryDialog = true
                    },
                    containerColor = AvaBrass,
                    contentColor = Color.Black,
                    modifier = Modifier.testTag("new_entry_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Entry")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> {
                    HomeScreen(
                        transactions = transactions,
                        vehicles = vehicles,
                        settings = settings,
                        bankBalance = bankBalance,
                        pettyBalance = pettyBalance,
                        onNavigateTab = { currentTab = it },
                        onOpenEntry = { t ->
                            editingTransaction = null
                            entryDialogInitialType = when (t) {
                                "in" -> "in"
                                "out" -> "out"
                                "xfer" -> "xfer"
                                else -> "out"
                            }
                            showEntryDialog = true
                        },
                        onSelectTransaction = { tx ->
                            editingTransaction = tx
                            showEntryDialog = true
                        }
                    )
                }

                AppTab.BOOK -> {
                    CashBookScreen(
                        transactions = filteredTransactions,
                        selectedAccount = accountFilter,
                        onAccountChange = { viewModel.accountFilter.value = it },
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.searchQuery.value = it },
                        selectedType = typeFilter,
                        onTypeChange = { viewModel.typeFilter.value = it },
                        onSelectTransaction = { tx ->
                            editingTransaction = tx
                            showEntryDialog = true
                        },
                        onOpenVoucher = { tx ->
                            viewingVoucherTransaction = tx
                        }
                    )
                }

                AppTab.CHEQUES -> {
                    ChequesScreen(
                        transactions = transactions,
                        bankBalance = bankBalance,
                        onMarkCleared = { tx, date ->
                            viewModel.markChequeCleared(tx, date)
                        },
                        onSelectTransaction = { tx ->
                            editingTransaction = tx
                            showEntryDialog = true
                        },
                        onOpenVoucher = { tx ->
                            viewingVoucherTransaction = tx
                        }
                    )
                }

                AppTab.FLEET -> {
                    FleetScreen(
                        vehicles = vehicles,
                        transactions = transactions,
                        onSaveVehicle = { veh ->
                            viewModel.saveVehicle(veh)
                        },
                        onDeleteVehicle = { id ->
                            viewModel.deleteVehicle(id)
                        }
                    )
                }

                AppTab.REPORTS -> {
                    ReportsScreen(
                        transactions = transactions,
                        vehicles = vehicles,
                        bankBalance = bankBalance,
                        pettyBalance = pettyBalance,
                        onExportCsv = { viewModel.exportCsvData() }
                    )
                }

                AppTab.SETTINGS -> {
                    SettingsScreen(
                        settings = settings,
                        userEmail = userEmail,
                        onSaveSettings = { newCfg ->
                            viewModel.saveSettings(newCfg)
                        },
                        onExportCsv = { viewModel.exportCsvData() },
                        onSignOut = onSignOut
                    )
                }
            }
        }
    }

    // Entry / Edit Dialog
    if (showEntryDialog) {
        EntryDialog(
            initialType = entryDialogInitialType,
            existingRecord = editingTransaction,
            vehicles = vehicles,
            onSave = { record ->
                viewModel.saveTransaction(record) {
                    showEntryDialog = false
                    editingTransaction = null
                }
            },
            onDelete = { id ->
                viewModel.deleteTransaction(id) {
                    showEntryDialog = false
                    editingTransaction = null
                }
            },
            onDismiss = {
                showEntryDialog = false
                editingTransaction = null
            }
        )
    }

    // Voucher Dialog
    if (viewingVoucherTransaction != null) {
        VoucherDialog(
            transaction = viewingVoucherTransaction!!,
            settings = settings,
            onDismiss = { viewingVoucherTransaction = null }
        )
    }
}
