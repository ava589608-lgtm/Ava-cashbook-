package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.FirestoreRepository
import com.example.model.TransactionRecord
import com.example.model.VehicleRecord
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AvaRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun testCreateAndReadTransaction() = runBlocking {
    val uid = signInTestUser("admin@avarental.ae")
    val repo = FirestoreRepository(firestore)

    val testTx = TransactionRecord(
      id = "tx_test_101",
      userId = uid,
      date = "2026-10-25",
      account = "Bank",
      type = "out",
      amount = 450.0,
      vat = 22.5,
      desc = "Emergency tire replacement",
      category = "Maintenance",
      vehicle = "27804 (SHARJAH)"
    )

    withTimeout(10000L) {
      repo.saveTransaction(testTx)
    }

    val transactions = withTimeout(10000L) {
      repo.observeTransactions().first()
    }

    assertTrue(transactions.any { it.id == "tx_test_101" })
    val retrieved = transactions.first { it.id == "tx_test_101" }
    assertEquals(450.0, retrieved.amount, 0.001)
    assertEquals("Maintenance", retrieved.category)
  }

  @Test
  fun testCreateAndReadVehicle() = runBlocking {
    val uid = signInTestUser("fleet@avarental.ae")
    val repo = FirestoreRepository(firestore)

    val testVehicle = VehicleRecord(
      id = "veh_test_01",
      userId = uid,
      plate = "DXB-99882",
      owner = "AVA PASSENGERS TRANSPORT",
      vtype = "TOYOTA COASTER",
      seats = 30,
      reg = "2027-08-15",
      ins = "2027-09-15"
    )

    withTimeout(10000L) {
      repo.saveVehicle(testVehicle)
    }

    val vehicles = withTimeout(10000L) {
      repo.observeVehicles().first()
    }

    assertTrue(vehicles.any { it.id == "veh_test_01" })
    val retrieved = vehicles.first { it.id == "veh_test_01" }
    assertEquals("DXB-99882", retrieved.plate)
    assertEquals(30, retrieved.seats)
  }
}
