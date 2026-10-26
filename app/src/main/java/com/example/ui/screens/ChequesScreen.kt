package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionRecord
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaMoneyIn
import com.example.ui.theme.AvaMoneyOut
import com.example.ui.theme.AvaTeal
import java.time.LocalDate
import java.util.Locale

@Composable
fun ChequesScreen(
    transactions: List<TransactionRecord>,
    bankBalance: Double,
    onMarkCleared: (TransactionRecord, String) -> Unit,
    onSelectTransaction: (TransactionRecord) -> Unit,
    onOpenVoucher: (TransactionRecord) -> Unit
) {
    var filterMode by remember { mutableStateOf("pending") } // "pending", "cleared", "all"

    val allCheques = transactions.filter { it.isCheque }
    val unclearedCheques = allCheques.filter { !it.isCleared }
    val totalUncleared = unclearedCheques.sumOf { it.amount }

    // If an outgoing cheque is pending, the bank statement currently has MORE money than our books show
    val expectedStatementBalance = bankBalance + totalUncleared

    val displayList = when (filterMode) {
        "pending" -> unclearedCheques
        "cleared" -> allCheques.filter { it.isCleared }
        else -> allCheques
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("cheques_screen")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Cheques to clear", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                        Text(
                            text = "AED ${String.format(Locale.US, "%,.2f", totalUncleared)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = AvaMoneyOut
                        )
                    }

                    Text(
                        text = "${unclearedCheques.size} cheques waiting to be cleared at bank",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reconciliation Grid
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            color = Color(0xFFF7FAF9),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Your books", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = "AED ${String.format(Locale.US, "%,.2f", bankBalance)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Surface(
                            color = Color(0xFFF7FAF9),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Bank statement should show", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = "AED ${String.format(Locale.US, "%,.2f", expectedStatementBalance)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AvaTeal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "pending" to "Pending (${unclearedCheques.size})",
                    "cleared" to "Cleared (${allCheques.size - unclearedCheques.size})",
                    "all" to "All (${allCheques.size})"
                ).forEach { (m, label) ->
                    val isSelected = filterMode == m
                    Surface(
                        color = if (isSelected) AvaTeal else Color(0xFFF1F5F4),
                        shape = RoundedCornerShape(99.dp),
                        modifier = Modifier.clickable { filterMode = m }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Cheques List
        if (displayList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AvaMoneyIn,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (filterMode == "pending") "All cheques are cleared! Your books match the bank." else "No cheques found.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(displayList, key = { it.id }) { tx ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable { onSelectTransaction(tx) }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = if (tx.isCleared) Color(0xFFE3F3EA) else Color(0xFFFFF3D6),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "CHQ #${tx.chequeNo.ifBlank { tx.ref }}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (tx.isCleared) AvaMoneyIn else Color(0xFF92400E),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = tx.date,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }

                                Text(
                                    text = "AED ${String.format(Locale.US, "%,.2f", tx.amount)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = if (tx.type == "in") AvaMoneyIn else AvaMoneyOut
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = tx.desc,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            if (tx.party.isNotBlank()) {
                                Text(
                                    text = "Payee: ${tx.party}",
                                    fontSize = 12.sp,
                                    color = AvaTeal,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            if (tx.vehicle.isNotBlank()) {
                                Text(
                                    text = "Vehicle: ${tx.vehicle}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Bottom actions row: Mark Cleared & View Voucher
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (tx.isCleared) {
                                    Text(
                                        text = "Cleared on: ${tx.clearedOn}",
                                        fontSize = 11.sp,
                                        color = AvaMoneyIn,
                                        fontWeight = FontWeight.Medium
                                    )
                                } else {
                                    Button(
                                        onClick = { onMarkCleared(tx, LocalDate.now().toString()) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AvaMoneyIn),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Mark as cleared", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                IconButton(
                                    onClick = { onOpenVoucher(tx) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = "View Voucher", tint = AvaBrass)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
