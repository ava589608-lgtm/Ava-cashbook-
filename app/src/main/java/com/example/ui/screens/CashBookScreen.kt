package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionRecord
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaMoneyIn
import com.example.ui.theme.AvaMoneyInBg
import com.example.ui.theme.AvaMoneyOut
import com.example.ui.theme.AvaMoneyOutBg
import com.example.ui.theme.AvaTeal
import com.example.ui.theme.AvaTransfer
import com.example.ui.theme.AvaTransferBg
import java.util.Locale

@Composable
fun CashBookScreen(
    transactions: List<TransactionRecord>,
    selectedAccount: String,
    onAccountChange: (String) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedType: String,
    onTypeChange: (String) -> Unit,
    onSelectTransaction: (TransactionRecord) -> Unit,
    onOpenVoucher: (TransactionRecord) -> Unit
) {
    val totalIn = transactions.filter { it.type == "in" }.sumOf { it.amount }
    val totalOut = transactions.filter { it.type == "out" }.sumOf { it.amount }
    val net = totalIn - totalOut

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("cash_book_screen")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Account Selection Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All" to "All Accounts", "Bank" to "Bank Account", "Petty" to "Petty Cash").forEach { (acc, label) ->
                    val isSelected = selectedAccount == acc
                    Surface(
                        color = if (isSelected) AvaTeal else Color(0xFFF1F5F4),
                        shape = RoundedCornerShape(99.dp),
                        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFD6DFDC)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onAccountChange(acc) }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color(0xFF333333),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.5.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search description, party, vehicle, ref...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_cash_book_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Type Filter Chips (All, Money In, Money Out, Transfers)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("All" to "All Types", "in" to "Money in", "out" to "Money out", "xfer" to "Transfers").forEach { (t, label) ->
                    val isSelected = selectedType == t
                    Surface(
                        color = if (isSelected) AvaBrass else Color.Transparent,
                        shape = RoundedCornerShape(99.dp),
                        border = BorderStroke(1.dp, if (isSelected) AvaBrass else Color(0xFFD6DFDC)),
                        modifier = Modifier.clickable { onTypeChange(t) }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.Black else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Summary Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Money in", fontSize = 10.sp, color = Color.Gray)
                        Text("AED ${String.format(Locale.US, "%,.2f", totalIn)}", color = AvaMoneyIn, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column {
                        Text("Money out", fontSize = 10.sp, color = Color.Gray)
                        Text("AED ${String.format(Locale.US, "%,.2f", totalOut)}", color = AvaMoneyOut, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Net Flow", fontSize = 10.sp, color = Color.Gray)
                        Text("AED ${String.format(Locale.US, "%,.2f", net)}", color = if (net >= 0) AvaMoneyIn else AvaMoneyOut, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Transactions List grouped by Date
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No matching cash book entries found.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val grouped = transactions.groupBy { it.date }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                grouped.forEach { (date, txsOnDate) ->
                    item {
                        Text(
                            text = date,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }

                    items(txsOnDate, key = { it.id }) { tx ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable { onSelectTransaction(tx) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val iconColor = when (tx.type) {
                                    "in" -> AvaMoneyIn
                                    "out" -> AvaMoneyOut
                                    else -> AvaTransfer
                                }
                                val iconBg = when (tx.type) {
                                    "in" -> AvaMoneyInBg
                                    "out" -> AvaMoneyOutBg
                                    else -> AvaTransferBg
                                }
                                Surface(
                                    color = iconBg,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (tx.type == "in") "+" else if (tx.type == "out") "−" else "⇄",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = iconColor
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.desc,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        maxLines = 1
                                    )
                                    if (tx.party.isNotBlank()) {
                                        Text(
                                            text = "Party: ${tx.party}",
                                            fontSize = 12.sp,
                                            color = AvaTeal,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${tx.account} • ${tx.category.ifBlank { "General" }}",
                                            color = Color.Gray,
                                            fontSize = 11.sp
                                        )
                                        if (tx.vehicle.isNotBlank()) {
                                            Text(
                                                text = " • ${tx.vehicle}",
                                                color = Color(0xFF2B5B84),
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${if (tx.type == "in") "+" else "-"} AED ${String.format(Locale.US, "%,.2f", tx.amount)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = iconColor
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (tx.isCheque) {
                                            Surface(
                                                color = if (tx.isCleared) Color(0xFFE3F3EA) else Color(0xFFFFF3D6),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.padding(end = 4.dp)
                                            ) {
                                                Text(
                                                    text = if (tx.isCleared) "Cleared" else "Chq ${tx.chequeNo}",
                                                    color = if (tx.isCleared) AvaMoneyIn else Color(0xFF8A5A00),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = { onOpenVoucher(tx) },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Icon(Icons.Default.Description, contentDescription = "Voucher", tint = AvaBrass, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
