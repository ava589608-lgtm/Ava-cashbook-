package com.example.model

object InitialData {
    val categories: Map<String, List<String>> = mapOf(
        "Diesel" to listOf(
            "ADNOC Wallet",
            "Diesel - UAQ (Three Star)",
            "Diesel - Sharjah (Three Star)",
            "Diesel - Ajman (Al Bahar)"
        ),
        "Income" to listOf(
            "ALICO Project Income",
            "Data UAQ Income",
            "Headline Income",
            "Ajman Furniture Income",
            "Exalogic Income"
        ),
        "Rent" to listOf(
            "14-Seater Sharjah - Yousuf Khan",
            "14-Seater Sharjah - Sabir Khan",
            "30-Seater Abu Dhabi - Speed Badshah",
            "60-Seater Abu Dhabi - Habibullah",
            "60-Seater Abu Dhabi - Azad",
            "Bab Al Skoon Hilux",
            "Bab Al Skoon Transport",
            "Nad Cars Services Workshop",
            "Basheer Rent",
            "Emirates Rent",
            "Fawad Ali Rasool Khan",
            "Jabal Rayan Passengers Transport",
            "Muhammad Noor Akbar Fazal Haq",
            "Nasir Ullah",
            "Waheed Ur Rehman Akram Khan",
            "General Fleet Rent"
        ),
        "Salaries" to listOf(
            "Ebrahim Salary",
            "Ebrahim Deposit",
            "Mubarak Theyyampattil",
            "Shahzeb / Shahzeed",
            "Haseeb",
            "Usman Khan",
            "Ahsan Nadeem",
            "Fazal Hussain",
            "Huzaifa Nisahuddin",
            "Ilyas",
            "Basheer",
            "Ali"
        ),
        "Permits" to listOf(
            "RAK Permit",
            "Sharjah Permit",
            "Dubai Permit",
            "Parking Permit / Fees"
        ),
        "Maintenance" to listOf(
            "New Tires",
            "Used Tires",
            "Batteries",
            "Spare Parts",
            "Al Nad"
        ),
        "Expenses" to listOf(
            "Camry Expense",
            "Hammer Expense",
            "General Fleet Maintenance"
        ),
        "AVA" to listOf(
            "AVA Office Rent",
            "AVA Trade License",
            "AVA Visas",
            "AVA Telephone",
            "AVA Bus",
            "AVA Typing",
            "AVA VAT",
            "AVA Fines",
            "AVA Salik"
        ),
        "Vehicle hire (subcontract)" to listOf(
            "Bus hire",
            "Pick-up hire",
            "Salik recharge"
        ),
        "Cheque payments" to listOf(
            "To be assigned"
        ),
        "Returned cheque" to emptyList(),
        "Bank charges" to listOf(
            "Remittance charge",
            "Maintenance fee",
            "Teller charges",
            "Cheque book",
            "Delivery charge"
        ),
        "Owner drawings" to emptyList(),
        "Capital introduced" to emptyList(),
        "Transfer" to emptyList()
    )

    fun getDefaultVehicles(userId: String): List<VehicleRecord> = listOf(
        VehicleRecord(id = "v_27804", userId = userId, plate = "27804 (SHARJAH)", owner = "EMIRARTATS TRANSPORT", vtype = "TOYOTA HI ACE", model = "2024 (Toyota Hi Ace)", seats = 13, reg = "2027-09-17", ins = "2027-10-17"),
        VehicleRecord(id = "v_77468", userId = userId, plate = "U / 77468", owner = "CLICK LINE PASSENGERS TRANSPORT", vtype = "ASHOK LEYLAND FALCON", model = "2006 (Ashok Leyland)", seats = 67, reg = "2026-07-13", ins = "2026-08-13"),
        VehicleRecord(id = "v_16788", userId = userId, plate = "V / 16788", owner = "CLICK LINE PASSENGERS TRANSPORT", vtype = "ASHOK LEYLAND", model = "2012 (Ashok Leyland)", seats = 67, reg = "2027-03-05", ins = "2027-04-05"),
        VehicleRecord(id = "v_77604", userId = userId, plate = "EE / 77604", owner = "CLICK LINE PASSENGERS TRANSPORT", vtype = "DAEWOO 106 BS", model = "2010 (Daewoo 106 BS)", seats = 57, reg = "2026-10-19", ins = "2026-11-19"),
        VehicleRecord(id = "v_56229", userId = userId, plate = "O / 56229", owner = "SHELL PASSENGER TRANSPORT", vtype = "ASHOK LEYLAND", model = "2013 (Ashok Leyland)", seats = 64, reg = "2026-10-16", ins = "2026-11-16"),
        VehicleRecord(id = "v_30419", userId = userId, plate = "M / 30419", owner = "CLICK LINE PASSENGERS TRANSPORT", vtype = "DAEWOO 106 BS", model = "2010 (Daewoo 106 BS)", seats = 56, reg = "2027-01-15", ins = "2027-02-15"),
        VehicleRecord(id = "v_79067", userId = userId, plate = "C / 79067", owner = "SHELL PASSENGER TRANSPORT", vtype = "ASHOK LEYLAND", model = "2011 (Ashok Leyland)", seats = 64, reg = "2027-04-13", ins = "2027-05-13"),
        VehicleRecord(id = "v_93137", userId = userId, plate = "M / 93137", owner = "SHELL PASSENGER TRANSPORT", vtype = "ASHOK LEYLAND", model = "2012 (Ashok Leyland)", seats = 67, reg = "2027-01-14", ins = "2027-02-14"),
        VehicleRecord(id = "v_34993", userId = userId, plate = "E / 34993 (DUBAI)", owner = "A V A PASSENGERS TRANSPORT", vtype = "ASHOK LEYLAND FAL", model = "2007 (Ashok Leyland)", seats = 84, reg = "2025-05-15", ins = "2025-06-15"),
        VehicleRecord(id = "v_79364", userId = userId, plate = "AA / 79364 (DUBAI)", owner = "A V A PASSENGERS TRANSPORT", vtype = "ASHOK LEYLAND FAL", model = "2004 (Ashok Leyland)", seats = 84, reg = "2026-07-25", ins = "2026-08-25")
    )

    fun getDefaultTransactions(userId: String): List<TransactionRecord> = listOf(
        TransactionRecord(id = "tx_01", userId = userId, date = "2026-10-22", account = "Bank", type = "out", amount = 1344.00, vat = 0.0, desc = "Zh", category = "Capital introduced", ref = "TX-20261022-501", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_02", userId = userId, date = "2026-10-19", account = "Bank", type = "in", amount = 394437.00, vat = 18782.71, desc = "Alico payment", category = "Income", subcategory = "ALICO Project Income", ref = "TX-20261019-814", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_03", userId = userId, date = "2026-10-19", account = "Bank", type = "out", amount = 1900.00, vat = 0.0, desc = "AUH 5day", category = "Rent", subcategory = "60-Seater Abu Dhabi - Habibullah", ref = "CHQ-01860", chequeNo = "001860", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_04", userId = userId, date = "2026-10-17", account = "Bank", type = "out", amount = 10000.00, vat = 0.0, desc = "Pettycash", category = "Transfer", ref = "TX-20261017-539", chequeNo = "01861", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_05", userId = userId, date = "2026-10-15", account = "Bank", type = "in", amount = 13125.00, vat = 625.0, desc = "Exalogic", category = "Income", subcategory = "Exalogic Income", ref = "TX-20261015-842", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_06", userId = userId, date = "2026-10-15", account = "Bank", type = "out", amount = 10092.00, vat = 0.0, desc = "Izath Sha July 27-31-augst", category = "Rent", subcategory = "14-Seater Sharjah - Yousuf Khan", ref = "CHQ-1859", chequeNo = "1859", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_07", userId = userId, date = "2026-10-15", account = "Bank", type = "in", amount = 1123.00, vat = 0.0, desc = "Insurance", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", ref = "TX-20261015-368", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_08", userId = userId, date = "2026-10-13", account = "Bank", type = "out", amount = 23466.00, vat = 1117.43, desc = "Diesel UAQ September", category = "Diesel", subcategory = "ADNOC Wallet", vehicle = "27804 (SHARJAH)", ref = "CHQ-01858", chequeNo = "01858", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_09", userId = userId, date = "2026-10-09", account = "Bank", type = "out", amount = 15500.00, vat = 0.0, desc = "Al nad rent", category = "Rent", subcategory = "Nad Cars Services Workshop", ref = "TX-20261009-963", chequeNo = "01851", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_10", userId = userId, date = "2026-10-09", account = "Bank", type = "out", amount = 9600.00, vat = 0.0, desc = "60 seater AUH", category = "Rent", subcategory = "14-Seater Sharjah - Yousuf Khan", ref = "TX-20261009-379", chequeNo = "01851", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_11", userId = userId, date = "2026-10-09", account = "Bank", type = "out", amount = 13757.29, vat = 0.0, desc = "14str shj - dxb + SALIK + 1-ADDED TRIP + 60 SEATER DXB CAMP", party = "FAWAD ALI RASOOL KHAN", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", vehicle = "27804 (SHARJAH)", ref = "PV-10/2026-09", chequeNo = "001856", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_12", userId = userId, date = "2026-10-09", account = "Bank", type = "out", amount = 10096.00, vat = 0.0, desc = "30 SEATER CAMP - SITE-AUH + 30 SEATER + ADDITIONAL TRIP", party = "SPEED BADSHAH", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", vehicle = "O / 56229", ref = "PV-10/2026-07", chequeNo = "001855", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_13", userId = userId, date = "2026-10-09", account = "Bank", type = "out", amount = 3150.00, vat = 150.0, desc = "DOUBLE CABIN PICK-UP", party = "BAB AL SKOON TRANSPORT", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", ref = "PV-10/2026-06", chequeNo = "001853", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_14", userId = userId, date = "2026-10-07", account = "Bank", type = "out", amount = 20000.00, vat = 0.0, desc = "Salary", category = "Salaries", ref = "CHQ-01844", chequeNo = "01844", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_15", userId = userId, date = "2026-10-07", account = "Bank", type = "out", amount = 9073.00, vat = 0.0, desc = "Sabir 14 str1 August + Salik", category = "Rent", subcategory = "14-Seater Sharjah - Sabir Khan", ref = "CHQ-01844", chequeNo = "01844", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_16", userId = userId, date = "2026-10-07", account = "Bank", type = "out", amount = 10500.00, vat = 0.0, desc = "60 SEATER AC BUS AUH", party = "AZAD", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", vehicle = "O / 56229", ref = "PV-10/2026-05", chequeNo = "001852", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_17", userId = userId, date = "2026-10-07", account = "Bank", type = "out", amount = 20012.00, vat = 0.0, desc = "60 seater JABEL ALI TO PALM JUMEIRAH + SALIK", party = "NASIR ULLAH", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", vehicle = "C / 79067", ref = "PV-10/2026-03", chequeNo = "001847", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_18", userId = userId, date = "2026-10-06", account = "Bank", type = "in", amount = 195997.12, vat = 0.0, desc = "Alico", category = "Income", subcategory = "ALICO Project Income", ref = "TX-20261006-958", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_19", userId = userId, date = "2026-10-05", account = "Bank", type = "out", amount = 30000.00, vat = 0.0, desc = "Salary cheque", category = "Salaries", ref = "TX-20261022-741", chequeNo = "01842", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_20", userId = userId, date = "2026-10-04", account = "Bank", type = "out", amount = 15550.00, vat = 0.0, desc = "14str SHJ-RAK + 30 SEATER -SHJ-DXB", party = "BASHEER", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", vehicle = "M / 30419", ref = "PV-10/2026-02", chequeNo = "001845", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_21", userId = userId, date = "2026-10-04", account = "Bank", type = "out", amount = 7386.00, vat = 0.0, desc = "14str shj - dxb + SALIK", party = "Sabir khan", category = "Vehicle hire (subcontract)", subcategory = "Bus hire", vehicle = "27804 (SHARJAH)", ref = "PV-10/2026-01", chequeNo = "001846", clearedOn = "2026-10-22", enteredBy = "Office setup"),
        TransactionRecord(id = "tx_22", userId = userId, date = "2026-10-01", account = "Bank", type = "out", amount = 6000.00, vat = 0.0, desc = "Office Rent", category = "AVA", subcategory = "AVA Office Rent", ref = "CHQ-1711", chequeNo = "1711", clearedOn = "2026-10-22", enteredBy = "Main Office"),
        TransactionRecord(id = "tx_23", userId = userId, date = "2026-09-28", account = "Bank", type = "out", amount = 10700.00, vat = 0.0, desc = "OUTWARD T/T THEYYAMBATTIL MOHAMED EBRAHIM", party = "THEYYAMBATTIL MOHAMED EBRAHIM", category = "AVA", subcategory = "AVA VAT", ref = "BNK-AV0015", clearedOn = "2026-09-28", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_24", userId = userId, date = "2026-09-28", account = "Bank", type = "out", amount = 3.15, vat = 0.15, desc = "Bank charge – remittance charge", party = "Bank", category = "Bank charges", subcategory = "Remittance charge", ref = "BNK-AV0014", clearedOn = "2026-09-28", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_25", userId = userId, date = "2026-09-26", account = "Bank", type = "out", amount = 12000.00, vat = 0.0, desc = "Cash withdrawal chq 001840", party = "MUBARAK THEYYAMBATTIL", category = "Expenses", subcategory = "General Fleet Maintenance", ref = "CHQ-001840", chequeNo = "001840", clearedOn = "2026-09-26", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_26", userId = userId, date = "2026-09-23", account = "Bank", type = "out", amount = 2500.00, vat = 0.0, desc = "Cheque 001838", category = "Cheque payments", subcategory = "To be assigned", ref = "CHQ-001838", chequeNo = "001838", clearedOn = "2026-09-23", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_27", userId = userId, date = "2026-09-22", account = "Bank", type = "out", amount = 17338.00, vat = 0.0, desc = "House cheque 001807", category = "Cheque payments", subcategory = "To be assigned", ref = "CHQ-001807", chequeNo = "001807", clearedOn = "2026-09-22", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_28", userId = userId, date = "2026-09-21", account = "Bank", type = "out", amount = 10000.00, vat = 0.0, desc = "Cash withdrawal chq 001839", party = "MUBARAK THEYYAMBATTIL", category = "Expenses", subcategory = "General Fleet Maintenance", ref = "CHQ-001839", chequeNo = "001839", clearedOn = "2026-09-21", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_29", userId = userId, date = "2026-09-15", account = "Bank", type = "out", amount = 15000.00, vat = 0.0, desc = "Cash withdrawal chq 001837", party = "MUBARAK THEYYAMBATTIL", category = "Expenses", subcategory = "General Fleet Maintenance", ref = "CHQ-001837", chequeNo = "001837", clearedOn = "2026-09-15", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_30", userId = userId, date = "2026-09-07", account = "Bank", type = "in", amount = 231543.14, vat = 0.0, desc = "INWARD T/T ALUMINIUM & LIGHT IND. CO.", party = "ALUMINIUM & LIGHT IND. CO.", category = "Income", subcategory = "ALICO Project Income", ref = "BNK-AV0013", clearedOn = "2026-09-07", enteredBy = "Office setup (statement)"),
        TransactionRecord(id = "tx_31", userId = userId, date = "2026-10-22", account = "Bank", type = "out", amount = 15000.00, vat = 0.0, desc = "Petty cash transfer", category = "Transfer", ref = "TR-DW0003", chequeNo = "001848", clearedOn = "2026-10-22", enteredBy = "Android DW"),
        TransactionRecord(id = "tx_32", userId = userId, date = "2026-10-22", account = "Petty", type = "in", amount = 15000.00, vat = 0.0, desc = "Petty cash transfer received", category = "Transfer", ref = "TR-DW0003", enteredBy = "Android DW"),
        TransactionRecord(id = "tx_33", userId = userId, date = "2026-10-22", account = "Petty", type = "out", amount = 2000.00, vat = 0.0, desc = "Advance", category = "Rent", ref = "OUT-DW0006", enteredBy = "Android DW"),
        TransactionRecord(id = "tx_34", userId = userId, date = "2026-10-26", account = "Petty", type = "out", amount = 500.00, vat = 0.0, desc = "Salik Recharge", category = "AVA", subcategory = "AVA Salik", ref = "OUT-DW0007", enteredBy = "Android DW"),
        TransactionRecord(id = "tx_35", userId = userId, date = "2026-10-26", account = "Petty", type = "out", amount = 1270.00, vat = 0.0, desc = "Hammer repair parts", category = "Expenses", subcategory = "Hammer Expense", ref = "OUT-DW0008", enteredBy = "Android DW"),
        TransactionRecord(id = "tx_36", userId = userId, date = "2026-10-26", account = "Petty", type = "out", amount = 2130.00, vat = 0.0, desc = "Camry vehicle repair", category = "Expenses", subcategory = "Camry Expense", ref = "OUT-DW0009", enteredBy = "Android DW")
    )

    fun amountToWords(amount: Double): String {
        val wholePart = amount.toLong()
        val fils = Math.round((amount - wholePart) * 100).toInt()

        val units = arrayOf(
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
        )
        val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

        fun numToText(n: Long): String {
            return when {
                n == 0L -> ""
                n < 20 -> units[n.toInt()]
                n < 100 -> tens[(n / 10).toInt()] + (if (n % 10 != 0L) " " + units[(n % 10).toInt()] else "")
                n < 1000 -> units[(n / 100).toInt()] + " Hundred" + (if (n % 100 != 0L) " " + numToText(n % 100) else "")
                n < 1000000 -> numToText(n / 1000) + " Thousand" + (if (n % 1000 != 0L) " " + numToText(n % 1000) else "")
                n < 1000000000 -> numToText(n / 1000000) + " Million" + (if (n % 1000000 != 0L) " " + numToText(n % 1000000) else "")
                else -> numToText(n / 1000000000) + " Billion" + (if (n % 1000000000 != 0L) " " + numToText(n % 1000000000) else "")
            }
        }

        val dirhamsText = if (wholePart == 0L) "Zero Dirhams" else numToText(wholePart) + " Dirhams"
        val filsText = if (fils > 0) " and " + numToText(fils.toLong()) + " Fils" else ""
        return "AED $dirhamsText$filsText Only"
    }
}
