package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaMoneyIn
import com.example.ui.theme.AvaMoneyOut
import com.example.ui.theme.AvaTeal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReportsScreen(
    transactions: List<TransactionRecord>,
    vehicles: List<VehicleRecord>,
    bankBalance: Double,
    pettyBalance: Double,
    onExportCsv: () -> String
) {
    val context = LocalContext.current
    var selectedPeriod by remember { mutableStateOf("this_month") }

    val today = LocalDate.now()
    val thisMonthStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    val lastMonthStr = today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"))
    val threeMonthsAgoStr = today.minusMonths(3).toString()
    val thisYearStr = today.year.toString()

    val filteredList = when (selectedPeriod) {
        "this_month" -> transactions.filter { it.date.startsWith(thisMonthStr) }
        "last_month" -> transactions.filter { it.date.startsWith(lastMonthStr) }
        "last_3_months" -> transactions.filter { it.date >= threeMonthsAgoStr }
        "this_year" -> transactions.filter { it.date.startsWith(thisYearStr) }
        else -> transactions
    }

    val totalIncome = filteredList.filter { it.type == "in" }.sumOf { it.amount }
    val totalExpense = filteredList.filter { it.type == "out" }.sumOf { it.amount }
    val netProfit = totalIncome - totalExpense
    val vatAmount = filteredList.sumOf { it.vat }

    val categoryExpenses = filteredList.filter { it.type == "out" }
        .groupBy { it.category.ifBlank { "Other" } }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("reports_screen")
    ) {
        // Period Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "this_month" to "This Month",
                "last_month" to "Last Month",
                "last_3_months" to "3 Months",
                "this_year" to "This Year",
                "all" to "All Time"
            ).forEach { (p, label) ->
                val isSelected = selectedPeriod == p
                Surface(
                    color = if (isSelected) AvaTeal else Color(0xFFF1F5F4),
                    shape = RoundedCornerShape(99.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedPeriod = p }
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 7.dp, horizontal = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Key Performance Indicators Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Financial Summary",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AvaTeal
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Income", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "AED ${String.format(Locale.US, "%,.2f", totalIncome)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AvaMoneyIn
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Expenses", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "AED ${String.format(Locale.US, "%,.2f", totalExpense)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AvaMoneyOut
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Net Profit", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "AED ${String.format(Locale.US, "%,.2f", netProfit)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (netProfit >= 0) AvaMoneyIn else AvaMoneyOut
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color(0xFFF7FAF9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated 5% VAT (Net)", fontSize = 12.sp, color = Color.Gray)
                        Text("AED ${String.format(Locale.US, "%,.2f", vatAmount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Account Balances
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Account Balances", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Bank Account", fontSize = 13.sp)
                    Text("AED ${String.format(Locale.US, "%,.2f", bankBalance)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Petty Cash", fontSize = 13.sp)
                    Text("AED ${String.format(Locale.US, "%,.2f", pettyBalance)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Liquidity", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("AED ${String.format(Locale.US, "%,.2f", bankBalance + pettyBalance)}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = AvaTeal)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Expenses By Category
        if (categoryExpenses.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Expenses by Category", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                    Spacer(modifier = Modifier.height(10.dp))

                    val maxVal = categoryExpenses.first().second
                    categoryExpenses.forEach { (cat, total) ->
                        val pct = if (totalExpense > 0) (total / totalExpense * 100).toInt() else 0
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("$cat ($pct%)", fontSize = 12.sp)
                                Text("AED ${String.format(Locale.US, "%,.2f", total)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (total / maxVal).toFloat() },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = AvaBrass,
                                trackColor = Color(0xFFF1F5F4)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Profit Per Vehicle Table
        if (vehicles.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Profit per Vehicle", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                    Spacer(modifier = Modifier.height(10.dp))

                    vehicles.forEach { veh ->
                        val vTxs = filteredList.filter { it.vehicle == veh.plate }
                        val inc = vTxs.filter { it.type == "in" }.sumOf { it.amount }
                        val exp = vTxs.filter { it.type == "out" }.sumOf { it.amount }
                        val net = inc - exp

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(veh.plate, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Rev: AED ${String.format(Locale.US, "%,.0f", inc)} • Exp: AED ${String.format(Locale.US, "%,.0f", exp)}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Text(
                                text = "AED ${String.format(Locale.US, "%,.0f", net)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (net >= 0) AvaMoneyIn else AvaMoneyOut
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Download CSV Report Button
        Button(
            onClick = {
                val csvContent = onExportCsv()
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/csv"
                    putExtra(Intent.EXTRA_TEXT, csvContent)
                    putExtra(Intent.EXTRA_SUBJECT, "AVA Cash Book Report - $selectedPeriod")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Export Report CSV"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("export_csv_button")
        ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Download / Share CSV Report", fontWeight = FontWeight.Bold)
        }
    }
}
