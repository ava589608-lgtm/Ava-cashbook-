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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CompanySettings
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import com.example.ui.components.AppTab
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaMoneyIn
import com.example.ui.theme.AvaMoneyInBg
import com.example.ui.theme.AvaMoneyOut
import com.example.ui.theme.AvaMoneyOutBg
import com.example.ui.theme.AvaTeal
import com.example.ui.theme.AvaTealDark
import com.example.ui.theme.AvaTransfer
import com.example.ui.theme.AvaTransferBg
import java.util.Locale

@Composable
fun HomeScreen(
    transactions: List<TransactionRecord>,
    vehicles: List<VehicleRecord>,
    settings: CompanySettings,
    bankBalance: Double,
    pettyBalance: Double,
    onNavigateTab: (AppTab) -> Unit,
    onOpenEntry: (String) -> Unit,
    onSelectTransaction: (TransactionRecord) -> Unit
) {
    val totalCash = bankBalance + pettyBalance
    val unclearedCheques = transactions.filter { it.isCheque && !it.isCleared }
    val unclearedChequesTotal = unclearedCheques.sumOf { it.amount }

    val vehiclesWithAlerts = vehicles.filter { it.overallStatus != VehicleRecord.AlertLevel.VALID }

    // Month totals
    val thisMonthIn = transactions.filter { it.type == "in" }.sumOf { it.amount }
    val thisMonthOut = transactions.filter { it.type == "out" }.sumOf { it.amount }
    val thisMonthNet = thisMonthIn - thisMonthOut

    // Category breakdown
    val categoryTotals = transactions.filter { it.type == "out" }
        .groupBy { it.category.ifBlank { "Other" } }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }
        .take(5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("home_screen")
    ) {
        // Passbook Brand Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AvaTeal, AvaTealDark)
                    )
                )
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {
            Column {
                Text(
                    text = "CASH & BANK TOTAL",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "AED",
                        color = AvaBrass,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = String.format(Locale.US, "%,.2f", totalCash),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Account cards: Bank & Petty
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Bank Account Card
                    Surface(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateTab(AppTab.BOOK) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Bank Account",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "AED ${String.format(Locale.US, "%,.2f", bankBalance)}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Petty Cash Card
                    Surface(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateTab(AppTab.BOOK) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Petty Cash",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "AED ${String.format(Locale.US, "%,.2f", pettyBalance)}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Quick Actions (Money in, Money out, Transfer, Voucher)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Money In Button
                Card(
                    colors = CardDefaults.cardColors(containerColor = AvaMoneyInBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenEntry("in") }
                        .testTag("action_money_in")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = AvaMoneyIn, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Money in", color = AvaMoneyIn, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Money Out Button
                Card(
                    colors = CardDefaults.cardColors(containerColor = AvaMoneyOutBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenEntry("out") }
                        .testTag("action_money_out")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null, tint = AvaMoneyOut, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Money out", color = AvaMoneyOut, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Transfer Button
                Card(
                    colors = CardDefaults.cardColors(containerColor = AvaTransferBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenEntry("xfer") }
                        .testTag("action_transfer")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SyncAlt, contentDescription = null, tint = AvaTransfer, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Transfer", color = AvaTransfer, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Voucher Button
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF4E7)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenEntry("voucher") }
                        .testTag("action_voucher")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF94620A), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Voucher", color = Color(0xFF94620A), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Alerts Section
            if (unclearedCheques.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFD8A8)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onNavigateTab(AppTab.CHEQUES) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color(0xFFD97706))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${unclearedCheques.size} Cheques to clear",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E),
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Total: AED ${String.format(Locale.US, "%,.2f", unclearedChequesTotal)}",
                                color = Color(0xFFB45309),
                                fontSize = 12.sp
                            )
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                    }
                }
            }

            if (vehiclesWithAlerts.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onNavigateTab(AppTab.FLEET) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Fleet Alert: ${vehiclesWithAlerts.size} vehicles need attention",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B),
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Registration or Insurance expired / due within 30 days",
                                color = Color(0xFFB91C1C),
                                fontSize = 11.sp
                            )
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Month Cash Flow KPI
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Current Cash Flow", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                        Text(
                            text = "Reports",
                            color = AvaBrass,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onNavigateTab(AppTab.REPORTS) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Money in", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "AED ${String.format(Locale.US, "%,.2f", thisMonthIn)}",
                                color = AvaMoneyIn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Money out", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "AED ${String.format(Locale.US, "%,.2f", thisMonthOut)}",
                                color = AvaMoneyOut,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Net", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "AED ${String.format(Locale.US, "%,.2f", thisMonthNet)}",
                                color = if (thisMonthNet >= 0) AvaMoneyIn else AvaMoneyOut,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Latest Entries
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Latest Entries", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                        Text(
                            text = "See all (${transactions.size})",
                            color = AvaBrass,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onNavigateTab(AppTab.BOOK) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (transactions.isEmpty()) {
                        Text("No entries recorded yet", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 12.dp))
                    } else {
                        transactions.take(5).forEach { tx ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectTransaction(tx) }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Category Icon Pill
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
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (tx.type == "in") "+" else if (tx.type == "out") "−" else "⇄",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 18.sp,
                                            color = iconColor
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.desc,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${tx.date} • ${tx.account} • ${tx.category.ifBlank { "General" }}",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${if (tx.type == "in") "+" else "-"} AED ${String.format(Locale.US, "%,.2f", tx.amount)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = iconColor
                                    )
                                    if (tx.isCheque) {
                                        Surface(
                                            color = if (tx.isCleared) Color(0xFFE3F3EA) else Color(0xFFFFF3D6),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.padding(top = 2.dp)
                                        ) {
                                            Text(
                                                text = if (tx.isCleared) "Cleared" else "Uncleared",
                                                color = if (tx.isCleared) AvaMoneyIn else Color(0xFF8A5A00),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Where the money went (Expenses Breakdown)
            if (categoryTotals.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Top Expense Categories", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                        Spacer(modifier = Modifier.height(12.dp))

                        val maxCatVal = categoryTotals.first().second
                        categoryTotals.forEach { (cat, total) ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(cat, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text("AED ${String.format(Locale.US, "%,.2f", total)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AvaMoneyOut)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { (total / maxCatVal).toFloat() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp),
                                    color = AvaMoneyOut,
                                    trackColor = Color(0xFFF1F5F4),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
