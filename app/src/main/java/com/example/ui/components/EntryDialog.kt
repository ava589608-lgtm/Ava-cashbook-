package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.InitialData
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaMoneyIn
import com.example.ui.theme.AvaMoneyOut
import com.example.ui.theme.AvaTeal
import com.example.ui.theme.AvaTransfer
import java.time.LocalDate
import java.util.Locale

@Composable
fun EntryDialog(
    initialType: String = "out",
    existingRecord: TransactionRecord? = null,
    vehicles: List<VehicleRecord> = emptyList(),
    onSave: (TransactionRecord) -> Unit,
    onDelete: (String) -> Unit = {},
    onDismiss: () -> Unit
) {
    var type by remember { mutableStateOf(existingRecord?.type ?: initialType) }
    var amountStr by remember { mutableStateOf(if (existingRecord != null) existingRecord.amount.toString() else "") }
    var account by remember { mutableStateOf(existingRecord?.account ?: "Bank") }
    var date by remember { mutableStateOf(existingRecord?.date ?: LocalDate.now().toString()) }
    var desc by remember { mutableStateOf(existingRecord?.desc ?: "") }
    var party by remember { mutableStateOf(existingRecord?.party ?: "") }
    var category by remember { mutableStateOf(existingRecord?.category ?: if (type == "in") "Income" else "Expenses") }
    var subcategory by remember { mutableStateOf(existingRecord?.subcategory ?: "") }
    var vehicle by remember { mutableStateOf(existingRecord?.vehicle ?: "") }
    var chequeNo by remember { mutableStateOf(existingRecord?.chequeNo ?: "") }
    var vatMode by remember { mutableStateOf("none") } // "none", "inc", "add"

    var catMenuExpanded by remember { mutableStateOf(false) }
    var subMenuExpanded by remember { mutableStateOf(false) }
    var vehMenuExpanded by remember { mutableStateOf(false) }

    val amount = amountStr.toDoubleOrNull() ?: 0.0
    val vatAmount = when (vatMode) {
        "inc" -> amount - (amount / 1.05)
        "add" -> amount * 0.05
        else -> 0.0
    }
    val finalAmount = if (vatMode == "add") amount + vatAmount else amount

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("entry_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (existingRecord != null) "Edit Entry" else "New Cash Book Entry",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AvaTeal
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Type Segmented Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F4), RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    listOf("in" to "Money in", "out" to "Money out", "xfer" to "Transfer").forEach { (t, label) ->
                        val isSelected = type == t
                        val activeColor = when (t) {
                            "in" -> AvaMoneyIn
                            "out" -> AvaMoneyOut
                            else -> AvaTransfer
                        }
                        Surface(
                            color = if (isSelected) activeColor else Color.Transparent,
                            shape = RoundedCornerShape(9.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    type = t
                                    if (t == "in" && category == "Expenses") category = "Income"
                                    if (t == "out" && category == "Income") category = "Expenses"
                                    if (t == "xfer") category = "Transfer"
                                }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 9.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amount
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Amount (AED)") },
                    placeholder = { Text("0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("entry_amount_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Account & Date Row
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Account", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Surface(
                                color = if (account == "Bank") AvaTeal else Color(0xFFF1F5F4),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { account = "Bank" }
                            ) {
                                Text(
                                    "Bank",
                                    color = if (account == "Bank") Color.White else Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (account == "Petty") AvaTeal else Color(0xFFF1F5F4),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { account = "Petty" }
                            ) {
                                Text(
                                    "Petty",
                                    color = if (account == "Petty") Color.White else Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date (YYYY-MM-DD)") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Presets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    val presets = if (type == "in") listOf("Alico Payment", "Exalogic Income", "Cash Deposit", "Charter Service")
                                  else listOf("ADNOC Diesel", "Spare Parts", "Bus Rent", "Salary Advance", "Salik Recharge", "Office Expense")
                    presets.forEach { preset ->
                        Surface(
                            color = Color(0xFFF1F5F4),
                            shape = RoundedCornerShape(99.dp),
                            border = BorderStroke(1.dp, Color(0xFFE0E5E3)),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable {
                                    desc = preset
                                    if (preset.contains("Diesel")) {
                                        category = "Diesel"
                                        subcategory = "ADNOC Wallet"
                                    } else if (preset.contains("Rent")) {
                                        category = "Rent"
                                    } else if (preset.contains("Salary")) {
                                        category = "Salaries"
                                    } else if (preset.contains("Salik")) {
                                        category = "AVA"
                                        subcategory = "AVA Salik"
                                    }
                                }
                        ) {
                            Text(preset, fontSize = 11.sp, color = Color(0xFF333333), modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    placeholder = { Text("e.g. Diesel for 27804, customer payment") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("entry_desc_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category & Subcategory
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { catMenuExpanded = true },
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = catMenuExpanded,
                            onDismissRequest = { catMenuExpanded = false }
                        ) {
                            InitialData.categories.keys.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        subcategory = InitialData.categories[cat]?.firstOrNull() ?: ""
                                        catMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    val subcats = InitialData.categories[category] ?: emptyList()
                    if (subcats.isNotEmpty()) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = subcategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Sub-category") },
                                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { subMenuExpanded = true },
                                shape = RoundedCornerShape(12.dp)
                            )
                            DropdownMenu(
                                expanded = subMenuExpanded,
                                onDismissRequest = { subMenuExpanded = false }
                            ) {
                                subcats.forEach { sub ->
                                    DropdownMenuItem(
                                        text = { Text(sub) },
                                        onClick = {
                                            subcategory = sub
                                            subMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = subcategory,
                            onValueChange = { subcategory = it },
                            label = { Text("Sub-category") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Vehicle Selector & Party
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1.1f)) {
                        OutlinedTextField(
                            value = vehicle.ifBlank { "None (General)" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vehicle") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { vehMenuExpanded = true },
                            shape = RoundedCornerShape(12.dp)
                        )
                        DropdownMenu(
                            expanded = vehMenuExpanded,
                            onDismissRequest = { vehMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("None (General Fleet)") },
                                onClick = {
                                    vehicle = ""
                                    vehMenuExpanded = false
                                }
                            )
                            vehicles.forEach { v ->
                                DropdownMenuItem(
                                    text = { Text(v.plate) },
                                    onClick = {
                                        vehicle = v.plate
                                        vehMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = party,
                        onValueChange = { party = it },
                        label = { Text(if (type == "in") "Received from" else "Paid to") },
                        placeholder = { Text("Optional") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cheque no
                OutlinedTextField(
                    value = chequeNo,
                    onValueChange = { chequeNo = it },
                    label = { Text("Cheque No. (Optional)") },
                    placeholder = { Text("e.g. 001858") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (existingRecord != null) {
                        OutlinedButton(
                            onClick = { onDelete(existingRecord.id) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }

                    Button(
                        onClick = {
                            if (amount > 0 && desc.isNotBlank()) {
                                val autoRef = existingRecord?.ref?.ifBlank { null }
                                    ?: if (chequeNo.isNotBlank()) "CHQ-$chequeNo"
                                    else "TX-${date.replace("-", "")}-${(100..999).random()}"

                                val record = (existingRecord ?: TransactionRecord()).copy(
                                    date = date,
                                    account = account,
                                    type = type,
                                    amount = finalAmount,
                                    vat = vatAmount,
                                    desc = desc,
                                    party = party,
                                    category = category,
                                    subcategory = subcategory,
                                    vehicle = vehicle,
                                    ref = autoRef,
                                    chequeNo = chequeNo,
                                    enteredBy = "Mobile"
                                )
                                onSave(record)
                            }
                        },
                        enabled = amount > 0 && desc.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_entry_button")
                    ) {
                        Text(
                            text = if (existingRecord != null) "Update Entry" else "Save Entry",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
