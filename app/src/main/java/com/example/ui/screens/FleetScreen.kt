package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.window.Dialog
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaMoneyIn
import com.example.ui.theme.AvaMoneyOut
import com.example.ui.theme.AvaTeal
import java.util.Locale

@Composable
fun FleetScreen(
    vehicles: List<VehicleRecord>,
    transactions: List<TransactionRecord>,
    onSaveVehicle: (VehicleRecord) -> Unit,
    onDeleteVehicle: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingVehicle by remember { mutableStateOf<VehicleRecord?>(null) }

    val expiredCount = vehicles.count { it.overallStatus == VehicleRecord.AlertLevel.EXPIRED }
    val soonCount = vehicles.count { it.overallStatus == VehicleRecord.AlertLevel.EXPIRING_SOON }
    val validCount = vehicles.size - expiredCount - soonCount

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingVehicle = null
                    showAddDialog = true
                },
                containerColor = AvaBrass,
                contentColor = Color.Black,
                modifier = Modifier.testTag("add_vehicle_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Vehicle")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("fleet_screen")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Fleet Overview Card
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
                            Text("Fleet Overview", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AvaTeal)
                            Text("${vehicles.size} Vehicles", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Surface(
                                color = Color(0xFFE3F3EA),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Valid", fontSize = 11.sp, color = AvaMoneyIn)
                                    Text("$validCount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AvaMoneyIn)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFFFF7ED),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Expiring Soon", fontSize = 11.sp, color = Color(0xFFD97706))
                                    Text("$soonCount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFFEF2F2),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Expired", fontSize = 11.sp, color = Color(0xFFDC2626))
                                    Text("$expiredCount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                }
                            }
                        }
                    }
                }
            }

            // Vehicles list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(vehicles, key = { it.id }) { veh ->
                    val vTxs = transactions.filter { it.vehicle == veh.plate }
                    val revenue = vTxs.filter { it.type == "in" }.sumOf { it.amount }
                    val expense = vTxs.filter { it.type == "out" }.sumOf { it.amount }
                    val profit = revenue - expense

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5EBE8)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                editingVehicle = veh
                                showAddDialog = true
                            }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = AvaTeal)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = veh.plate,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    )
                                }

                                // Status Badge
                                val (badgeBg, badgeColor, badgeText) = when (veh.overallStatus) {
                                    VehicleRecord.AlertLevel.EXPIRED -> Triple(Color(0xFFFEF2F2), Color(0xFFDC2626), "Expired")
                                    VehicleRecord.AlertLevel.EXPIRING_SOON -> Triple(Color(0xFFFFF7ED), Color(0xFFD97706), "Due Soon")
                                    else -> Triple(Color(0xFFE3F3EA), AvaMoneyIn, "Valid")
                                }
                                Surface(
                                    color = badgeBg,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = badgeText,
                                        color = badgeColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${veh.vtype} • ${veh.model} • ${veh.seats} Seats",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            if (veh.owner.isNotBlank()) {
                                Text(
                                    text = "Owner: ${veh.owner}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF555555)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Expirations
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Reg: ${veh.reg.ifBlank { "N/A" }}",
                                    fontSize = 11.sp,
                                    color = if (veh.regStatus == VehicleRecord.AlertLevel.EXPIRED) Color.Red else Color.Gray,
                                    fontWeight = if (veh.regStatus == VehicleRecord.AlertLevel.EXPIRED) FontWeight.Bold else FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Ins: ${veh.ins.ifBlank { "N/A" }}",
                                    fontSize = 11.sp,
                                    color = if (veh.insStatus == VehicleRecord.AlertLevel.EXPIRED) Color.Red else Color.Gray,
                                    fontWeight = if (veh.insStatus == VehicleRecord.AlertLevel.EXPIRED) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Financials
                            Surface(
                                color = Color(0xFFF7FAF9),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Rev: AED ${String.format(Locale.US, "%,.0f", revenue)}", fontSize = 11.sp, color = AvaMoneyIn)
                                    Text("Exp: AED ${String.format(Locale.US, "%,.0f", expense)}", fontSize = 11.sp, color = AvaMoneyOut)
                                    Text("Net: AED ${String.format(Locale.US, "%,.0f", profit)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (profit >= 0) AvaMoneyIn else AvaMoneyOut)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        VehicleEditDialog(
            vehicle = editingVehicle,
            onSave = {
                onSaveVehicle(it)
                showAddDialog = false
            },
            onDelete = {
                if (editingVehicle != null) {
                    onDeleteVehicle(editingVehicle!!.id)
                }
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun VehicleEditDialog(
    vehicle: VehicleRecord?,
    onSave: (VehicleRecord) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var plate by remember { mutableStateOf(vehicle?.plate ?: "") }
    var owner by remember { mutableStateOf(vehicle?.owner ?: "") }
    var vtype by remember { mutableStateOf(vehicle?.vtype ?: "") }
    var model by remember { mutableStateOf(vehicle?.model ?: "") }
    var seatsStr by remember { mutableStateOf(vehicle?.seats?.toString() ?: "50") }
    var reg by remember { mutableStateOf(vehicle?.reg ?: "") }
    var ins by remember { mutableStateOf(vehicle?.ins ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (vehicle != null) "Edit Vehicle" else "Add Vehicle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = AvaTeal
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Plate / Number") },
                    placeholder = { Text("e.g. 27804 (SHARJAH)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vtype,
                    onValueChange = { vtype = it },
                    label = { Text("Vehicle Type") },
                    placeholder = { Text("e.g. ASHOK LEYLAND") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Model / Year") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = seatsStr,
                        onValueChange = { seatsStr = it },
                        label = { Text("Seats") },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = reg,
                        onValueChange = { reg = it },
                        label = { Text("Reg Expiry") },
                        placeholder = { Text("YYYY-MM-DD") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = ins,
                        onValueChange = { ins = it },
                        label = { Text("Ins Expiry") },
                        placeholder = { Text("YYYY-MM-DD") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (vehicle != null) {
                        Button(
                            onClick = onDelete,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2), contentColor = Color.Red),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }

                    Button(
                        onClick = {
                            if (plate.isNotBlank()) {
                                onSave(
                                    (vehicle ?: VehicleRecord()).copy(
                                        plate = plate,
                                        owner = owner,
                                        vtype = vtype,
                                        model = model,
                                        seats = seatsStr.toIntOrNull() ?: 0,
                                        reg = reg,
                                        ins = ins
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Vehicle", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
