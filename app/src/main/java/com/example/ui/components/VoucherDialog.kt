package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CompanySettings
import com.example.model.InitialData
import com.example.model.TransactionRecord
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaTeal
import java.util.Locale

@Composable
fun VoucherDialog(
    transaction: TransactionRecord,
    settings: CompanySettings,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val title = when {
        transaction.category.equals("Rent", ignoreCase = true) ||
        transaction.desc.contains("rent", ignoreCase = true) -> "RENT PAYMENT VOUCHER"
        transaction.category.equals("Salaries", ignoreCase = true) ||
        transaction.desc.contains("salary", ignoreCase = true) -> "SALARY SLIP"
        else -> "PAYMENT VOUCHER"
    }

    val voucherNo = if (transaction.ref.isNotBlank()) transaction.ref else "PV-${transaction.date.replace("-", "")}"
    val paymentMethod = if (transaction.isCheque) "Cheque" else if (transaction.account == "Petty") "Cash" else "Bank Transfer"
    val words = InitialData.amountToWords(transaction.amount)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(vertical = 16.dp)
                .testTag("voucher_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top close & share row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Voucher Document",
                        fontWeight = FontWeight.Bold,
                        color = AvaTeal,
                        fontSize = 15.sp
                    )
                    Row {
                        IconButton(onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                val text = """
                                    $title
                                    Company: ${settings.companyName}
                                    Voucher No: $voucherNo
                                    Date: ${transaction.date}
                                    Paid To: ${transaction.party.ifBlank { "N/A" }}
                                    Amount: AED ${String.format(Locale.US, "%,.2f", transaction.amount)}
                                    Words: $words
                                    Description: ${transaction.desc}
                                    Vehicle: ${transaction.vehicle.ifBlank { "N/A" }}
                                    Payment Mode: $paymentMethod
                                    Cheque No: ${transaction.chequeNo.ifBlank { "N/A" }}
                                """.trimIndent()
                                putExtra(Intent.EXTRA_TEXT, text)
                                putExtra(Intent.EXTRA_SUBJECT, "$title - $voucherNo")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Voucher"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = AvaTeal)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Printable Voucher Paper container
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFCFA)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF1E4D3B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        // Company Header
                        Text(
                            text = settings.companyName,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF1E4D3B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = settings.companyAddress,
                            fontSize = 10.sp,
                            color = Color(0xFF555555),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Voucher Title Banner
                        Surface(
                            color = Color(0xFF1E4D3B),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Metadata Grid
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("VOUCHER NO.", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(voucherNo, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("DATE", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(transaction.date, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("PAYMENT MODE", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(paymentMethod, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1.5f)) {
                                Text("PAID TO / BENEFICIARY", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(transaction.party.ifBlank { "N/A" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E4D3B))
                            }
                            if (transaction.vehicle.isNotBlank()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("VEHICLE PLATE", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(transaction.vehicle, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                }
                            }
                            if (transaction.chequeNo.isNotBlank()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("CHEQUE NO.", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(transaction.chequeNo, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = Color(0xFFE0E0E0))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Description line item
                        Text("DESCRIPTION & PARTICULARS", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = transaction.desc,
                            fontSize = 13.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        if (transaction.category.isNotBlank()) {
                            Text(
                                text = "Category: ${transaction.category} ${if (transaction.subcategory.isNotBlank()) "(${transaction.subcategory})" else ""}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFE0E0E0))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Amount section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TOTAL AMOUNT", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                            Text(
                                "AED ${String.format(Locale.US, "%,.2f", transaction.amount)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color(0xFF1E4D3B)
                            )
                        }

                        if (transaction.vat > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("5% VAT Included", fontSize = 11.sp, color = Color.Gray)
                                Text("AED ${String.format(Locale.US, "%,.2f", transaction.vat)}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Amount in words
                        Surface(
                            color = Color(0xFFF1F5F3),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = words,
                                fontSize = 11.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = Color(0xFF333333),
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(36.dp))

                        // Signatures block
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Divider(color = Color.DarkGray, thickness = 1.dp)
                                Text("PREPARED BY", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                                Text(transaction.enteredBy.ifBlank { "Office" }, fontSize = 10.sp, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Divider(color = Color.DarkGray, thickness = 1.dp)
                                Text("CHECKED BY", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                                Text("Accountant", fontSize = 10.sp, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Divider(color = Color.DarkGray, thickness = 1.dp)
                                Text("RECEIVED BY", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                                Text("Signature", fontSize = 10.sp, color = Color.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AvaTeal),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}
