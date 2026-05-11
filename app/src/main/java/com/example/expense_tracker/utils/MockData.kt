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

        val calendar = Calendar.getInstance()

        // Generate 6 months of data
        for (monthOffset in 0..5) {

            val baseCalendar = Calendar.getInstance().apply {
                add(Calendar.MONTH, -monthOffset)
            }

            // =========================
            // INCOME
            // =========================

            transactions.add(
                TransactionEntity(
                    userId = userId,
                    accountId = accountId,
                    categoryId = "salary",
                    amount = 15000000.0,
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
                    amount = 2500000.0,
                    type = TransactionType.INCOME,
                    date = baseCalendar.apply {
                        add(Calendar.DAY_OF_MONTH, -2)
                    }.timeInMillis,
                    description = "Freelance Project"
                )
            )

            // =========================
            // EXPENSES
            // =========================

            val expenseData = listOf(

                Triple("food_drink", 120000.0, "Lunch"),
                Triple("food_drink", 85000.0, "Coffee"),
                Triple("groceries", 450000.0, "Supermarket"),
                Triple("transport", 70000.0, "Grab Bike"),
                Triple("shopping", 900000.0, "New Clothes"),
                Triple("housing", 3500000.0, "Apartment Rent"),
                Triple("entertainment", 250000.0, "Cinema"),
                Triple("health", 300000.0, "Medicine"),
                Triple("education", 500000.0, "Online Course"),
                Triple("utilities", 400000.0, "Electric Bill"),
                Triple("travel", 1500000.0, "Weekend Trip"),
                Triple("personal_care", 220000.0, "Haircut"),
                Triple("pet", 180000.0, "Pet Food"),
                Triple("other_expense", 100000.0, "Misc")
            )

            expenseData.forEachIndexed { index, item ->

                val txCalendar = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -monthOffset)
                    add(Calendar.DAY_OF_MONTH, -index)
                }

                transactions.add(
                    TransactionEntity(
                        userId = userId,
                        accountId = accountId,
                        categoryId = item.first,
                        amount = item.second + (0..200000).random(),
                        type = TransactionType.EXPENSE,
                        date = txCalendar.timeInMillis,
                        description = item.third
                    )
                )
            }
        }

        return transactions
    }
}