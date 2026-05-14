package com.example.expense_tracker.utils

import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import java.util.Calendar

object MockData {

    fun transactions(
        userId: Long,
        accountId: Long
    ): List<TransactionEntity> {

        val transactions = mutableListOf<TransactionEntity>()

        // Generate 6 months of data
        for (monthOffset in 0..5) {

            val baseCalendar = Calendar.getInstance().apply {
                add(Calendar.MONTH, -monthOffset)
            }

            // =========================
            // INCOME - Giảm xuống (USD)
            // =========================

            transactions.add(
                TransactionEntity(
                    userId = userId,
                    accountId = accountId,
                    categoryId = "salary",
                    amount = 4500.0,  // 15,000,000 VND ~ $4,500
                    type = TransactionType.INCOME,
                    date = baseCalendar.timeInMillis,
                    description = "Monthly Salary"
                )
            )

            transactions.add(
                TransactionEntity(
                    userId = userId,
                    accountId = accountId,
                    categoryId = "freelance",
                    amount = 750.0,  // 2,500,000 VND ~ $750
                    type = TransactionType.INCOME,
                    date = baseCalendar.apply {
                        add(Calendar.DAY_OF_MONTH, -2)
                    }.timeInMillis,
                    description = "Freelance Project"
                )
            )

            // =========================
            // EXPENSES - Giảm xuống (USD)
            // =========================

            val expenseData = listOf(
                // Food & Drink
                Triple("food_drink", 15.0, "Lunch at restaurant"),
                Triple("food_drink", 5.0, "Morning coffee"),
                Triple("food_drink", 12.0, "Dinner with friends"),
                Triple("food_drink", 8.0, "Snacks and drinks"),
                Triple("food_drink", 10.0, "Weekend brunch"),
                Triple("food_drink", 6.0, "Bubble tea"),
                Triple("food_drink", 25.0, "Birthday dinner"),

                // Groceries
                Triple("groceries", 15.0, "Supermarket - Whole Foods"),
                Triple("groceries", 10.0, "Fresh vegetables"),
                Triple("groceries", 12.0, "Meat and seafood"),

                // Transport
                Triple("transport", 3.0, "Uber to office"),
                Triple("transport", 8.0, "Taxi to airport"),
                Triple("transport", 10.0, "Uber to meeting"),
                Triple("transport", 15.0, "Monthly bus pass"),
                Triple("transport", 6.0, "Taxi home late"),

                // Shopping
                Triple("shopping", 35.0, "New clothes"),
                Triple("shopping", 20.0, "Shoes"),
                Triple("shopping", 25.0, "Backpack"),

                // Housing
                Triple("housing", 1200.0, "Apartment Rent"),
                Triple("housing", 20.0, "House maintenance"),

                // Entertainment
                Triple("entertainment", 12.0, "Cinema - Avatar"),
                Triple("entertainment", 8.0, "Netflix subscription"),
                Triple("entertainment", 25.0, "Concert ticket"),
                Triple("entertainment", 15.0, "Bowling with friends"),

                // Health
                Triple("health", 15.0, "Medicine"),
                Triple("health", 25.0, "Doctor visit"),

                // Education
                Triple("education", 20.0, "Online course"),
                Triple("education", 12.0, "Books"),

                // Utilities
                Triple("utilities", 18.0, "Electricity bill"),
                Triple("utilities", 12.0, "Water bill"),
                Triple("utilities", 15.0, "Internet bill"),

                // Travel
                Triple("travel", 60.0, "Weekend trip"),

                // Personal Care
                Triple("personal_care", 12.0, "Haircut"),
                Triple("personal_care", 10.0, "Skincare products"),

                // Pet
                Triple("pet", 8.0, "Pet food"),
                Triple("pet", 15.0, "Veterinary visit"),

                // Others
                Triple("other_expense", 5.0, "Miscellaneous"),
                Triple("gift", 10.0, "Birthday gift")
            )

            expenseData.forEachIndexed { index, item ->
                val txCalendar = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -monthOffset)
                    add(Calendar.DAY_OF_MONTH, -index)
                    set(Calendar.HOUR_OF_DAY, (8..22).random())
                    set(Calendar.MINUTE, (0..59).random())
                }

                transactions.add(
                    TransactionEntity(
                        userId = userId,
                        accountId = accountId,
                        categoryId = item.first,
                        amount = item.second + (0..5).random().toDouble(),
                        type = TransactionType.EXPENSE,
                        date = txCalendar.timeInMillis,
                        description = item.third
                    )
                )
            }
        }

        return transactions.shuffled()
    }
}