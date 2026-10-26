package com.example.data

import android.content.Context
import com.example.R
import com.example.model.CompanySettings
import com.example.model.InitialData
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeTransactions(): Flow<List<TransactionRecord>> {
        val uid = requireUserId()
        val path = "users/$uid/transactions"
        return db.collection("users").document(uid).collection("transactions")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(TransactionRecord::class.java)
                    .filter { !it.deleted }
                    .sortedByDescending { it.date }
            }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    fun observeVehicles(): Flow<List<VehicleRecord>> {
        val uid = requireUserId()
        val path = "users/$uid/vehicles"
        return db.collection("users").document(uid).collection("vehicles")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(VehicleRecord::class.java).sortedBy { it.plate }
            }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    fun observeSettings(): Flow<CompanySettings> = callbackFlow {
        val uid = requireUserId()
        val path = "users/$uid/settings/config"
        val docRef = db.collection("users").document(uid).collection("settings").document("config")
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.GET, path)
                close(error)
                return@addSnapshotListener
            }
            val settings = snapshot?.toObject(CompanySettings::class.java) ?: CompanySettings(userId = uid)
            trySend(settings)
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveTransaction(tx: TransactionRecord) {
        val uid = requireUserId()
        val docId = if (tx.id.isNotBlank()) tx.id else "tx_" + UUID.randomUUID().toString().take(12)
        val path = "users/$uid/transactions/$docId"
        val docRef = db.collection("users").document(uid).collection("transactions").document(docId)

        val data = mapOf(
            "userId" to uid,
            "date" to tx.date,
            "account" to tx.account,
            "type" to tx.type,
            "amount" to tx.amount,
            "vat" to tx.vat,
            "desc" to tx.desc,
            "party" to tx.party,
            "category" to tx.category,
            "subcategory" to tx.subcategory,
            "vehicle" to tx.vehicle,
            "ref" to tx.ref,
            "chequeNo" to tx.chequeNo,
            "clearedOn" to tx.clearedOn,
            "voucherLines" to tx.voucherLines,
            "receiptUrl" to tx.receiptUrl,
            "enteredBy" to tx.enteredBy,
            "deleted" to tx.deleted,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            if (tx.createdAt == null) {
                docRef.set(data + ("createdAt" to FieldValue.serverTimestamp())).await()
            } else {
                docRef.update(data).await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            throw e
        }
    }

    suspend fun deleteTransaction(id: String) {
        val uid = requireUserId()
        val path = "users/$uid/transactions/$id"
        val docRef = db.collection("users").document(uid).collection("transactions").document(id)
        try {
            // Soft delete or hard delete; here soft delete to preserve audit history
            docRef.update("deleted", true, "updatedAt", FieldValue.serverTimestamp()).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            throw e
        }
    }

    suspend fun saveVehicle(vehicle: VehicleRecord) {
        val uid = requireUserId()
        val docId = if (vehicle.id.isNotBlank()) vehicle.id else "veh_" + UUID.randomUUID().toString().take(8)
        val path = "users/$uid/vehicles/$docId"
        val docRef = db.collection("users").document(uid).collection("vehicles").document(docId)

        val data = mapOf(
            "userId" to uid,
            "plate" to vehicle.plate,
            "owner" to vehicle.owner,
            "vtype" to vehicle.vtype,
            "model" to vehicle.model,
            "seats" to vehicle.seats,
            "reg" to vehicle.reg,
            "ins" to vehicle.ins,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            if (vehicle.createdAt == null) {
                docRef.set(data + ("createdAt" to FieldValue.serverTimestamp())).await()
            } else {
                docRef.update(data).await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            throw e
        }
    }

    suspend fun deleteVehicle(id: String) {
        val uid = requireUserId()
        val path = "users/$uid/vehicles/$id"
        try {
            db.collection("users").document(uid).collection("vehicles").document(id).delete().await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            throw e
        }
    }

    suspend fun saveSettings(settings: CompanySettings) {
        val uid = requireUserId()
        val path = "users/$uid/settings/config"
        val docRef = db.collection("users").document(uid).collection("settings").document("config")
        val data = mapOf(
            "userId" to uid,
            "companyName" to settings.companyName,
            "companyAddress" to settings.companyAddress,
            "bankOpeningBalance" to settings.bankOpeningBalance,
            "pettyOpeningBalance" to settings.pettyOpeningBalance,
            "lockDate" to settings.lockDate,
            "role" to settings.role,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(data + ("createdAt" to FieldValue.serverTimestamp())).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            throw e
        }
    }

    suspend fun seedInitialDataIfEmpty() {
        val uid = requireUserId()
        val txCollection = db.collection("users").document(uid).collection("transactions")
        val snapshot = txCollection.limit(1).get().await()
        if (snapshot.isEmpty) {
            // Seed transactions
            val defaultTx = InitialData.getDefaultTransactions(uid)
            for (tx in defaultTx) {
                saveTransaction(tx)
            }
            // Seed vehicles
            val defaultVehicles = InitialData.getDefaultVehicles(uid)
            for (veh in defaultVehicles) {
                saveVehicle(veh)
            }
            // Seed settings
            saveSettings(CompanySettings(userId = uid))
        }
    }
}
