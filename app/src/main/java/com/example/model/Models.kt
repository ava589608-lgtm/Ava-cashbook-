package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@IgnoreExtraProperties
data class TransactionRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val date: String = "",
    val account: String = "Bank", // "Bank" or "Petty"
    val type: String = "out",     // "in", "out", "xfer"
    val amount: Double = 0.0,
    val vat: Double = 0.0,
    val desc: String = "",
    val party: String = "",
    val category: String = "",
    val subcategory: String = "",
    val vehicle: String = "",
    val ref: String = "",
    val chequeNo: String = "",
    val clearedOn: String = "",
    val voucherLines: String = "",
    val receiptUrl: String = "",
    val enteredBy: String = "",
    val deleted: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    val netOfVat: Double get() = amount - vat

    val isCheque: Boolean get() = chequeNo.isNotBlank() || ref.startsWith("CHQ") || ref.startsWith("PV")
    val isCleared: Boolean get() = clearedOn.isNotBlank()
}

@IgnoreExtraProperties
data class VehicleRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plate: String = "",
    val owner: String = "",
    val vtype: String = "",
    val model: String = "",
    val seats: Int = 0,
    val reg: String = "", // YYYY-MM-DD
    val ins: String = "", // YYYY-MM-DD
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    enum class AlertLevel {
        VALID, EXPIRING_SOON, EXPIRED, UNKNOWN
    }

    private fun checkDate(dateStr: String): AlertLevel {
        if (dateStr.isBlank()) return AlertLevel.UNKNOWN
        return try {
            val date = LocalDate.parse(dateStr)
            val today = LocalDate.now()
            val days = ChronoUnit.DAYS.between(today, date)
            when {
                days < 0 -> AlertLevel.EXPIRED
                days <= 30 -> AlertLevel.EXPIRING_SOON
                else -> AlertLevel.VALID
            }
        } catch (_: Exception) {
            AlertLevel.UNKNOWN
        }
    }

    val regStatus: AlertLevel get() = checkDate(reg)
    val insStatus: AlertLevel get() = checkDate(ins)
    val overallStatus: AlertLevel get() = when {
        regStatus == AlertLevel.EXPIRED || insStatus == AlertLevel.EXPIRED -> AlertLevel.EXPIRED
        regStatus == AlertLevel.EXPIRING_SOON || insStatus == AlertLevel.EXPIRING_SOON -> AlertLevel.EXPIRING_SOON
        else -> AlertLevel.VALID
    }
}

@IgnoreExtraProperties
data class CompanySettings(
    @DocumentId val id: String = "config",
    val userId: String = "",
    val companyName: String = "AVA PASSENGERS TRANSPORT BY RENTAL BUSES LLC",
    val companyAddress: String = "P.O. Box 91977, Dubai, UAE | Phone: 00971 4 3439666 | Email: Contact@avarental.ae",
    val bankOpeningBalance: Double = 50418.86,
    val pettyOpeningBalance: Double = 0.0,
    val lockDate: String = "",
    val role: String = "Admin", // "Admin", "Check", "Staff"
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class VoucherLineItem(
    val description: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val quantity: Double = 1.0,
    val unitRate: Double = 0.0,
    val amount: Double = 0.0
)
