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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CompanySettings
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaTeal
import java.util.Locale

@Composable
fun SettingsScreen(
    settings: CompanySettings,
    userEmail: String?,
    onSaveSettings: (CompanySettings) -> Unit,
    onExportCsv: () -> String,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var companyName by remember { mutableStateOf(settings.companyName) }
    var companyAddr by remember { mutableStateOf(settings.companyAddress) }
    var bankObStr by remember { mutableStateOf(settings.bankOpeningBalance.toString()) }
    var pettyObStr by remember { mutableStateOf(settings.pettyOpeningBalance.toString()) }
    var lockDate by remember { mutableStateOf(settings.lockDate) }
    var selectedRole by remember { mutableStateOf(settings.role) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("settings_screen")
    ) {
        // User profile Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Account & Device Role", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Signed in as: ${userEmail ?: "Company Administrator"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Role selection chips (Admin, Check, Staff)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Admin" to "Admin (Full)", "Check" to "Check (View)", "Staff" to "Staff (Petty)").forEach { (r, label) ->
                        val isSelected = selectedRole == r
                        Surface(
                            color = if (isSelected) AvaTeal else Color(0xFFF1F5F4),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedRole = r
                                    onSaveSettings(settings.copy(role = r))
                                }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Voucher Header Settings
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Company & Voucher Header", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = companyAddr,
                    onValueChange = { companyAddr = it },
                    label = { Text("Address & Contact") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onSaveSettings(
                            settings.copy(
                                companyName = companyName,
                                companyAddress = companyAddr
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Header")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Opening Balances
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Opening Balances (AED)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = bankObStr,
                        onValueChange = { bankObStr = it },
                        label = { Text("Bank Opening") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                        value = pettyObStr,
                        onValueChange = { pettyObStr = it },
                        label = { Text("Petty Opening") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val b = bankObStr.toDoubleOrNull() ?: settings.bankOpeningBalance
                        val p = pettyObStr.toDoubleOrNull() ?: settings.pettyOpeningBalance
                        onSaveSettings(settings.copy(bankOpeningBalance = b, pettyOpeningBalance = p))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Opening Balances")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Books Closing Lock
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = AvaBrass, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Close Books Date", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                }

                Text(
                    text = "Entries on or before this date are frozen to prevent unauthorized changes after month-end reconciliation.",
                    color = Color.Gray,
                    fontSize = 11.5.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                OutlinedTextField(
                    value = lockDate,
                    onValueChange = { lockDate = it },
                    label = { Text("Closing Date (YYYY-MM-DD)") },
                    placeholder = { Text("e.g. 2026-09-30") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        onSaveSettings(settings.copy(lockDate = lockDate))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Closing Date")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Data Backup & Sign Out
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Backup & Security", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val csv = onExportCsv()
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/csv"
                            putExtra(Intent.EXTRA_TEXT, csv)
                            putExtra(Intent.EXTRA_SUBJECT, "AVA Cash Book Complete Backup")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Download Backup"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaBrass, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Complete Cash Book CSV", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onSignOut,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB33D27)),
                    border = BorderStroke(1.dp, Color(0xFFB33D27)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "AVA Cash Book v3.2.0 • Android Enterprise\nAVA Passengers Transport by Rental Buses LLC",
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
