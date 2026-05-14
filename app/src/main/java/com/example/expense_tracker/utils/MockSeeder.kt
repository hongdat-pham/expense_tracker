package com.example.expense_tracker.utils

import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.Calendar

object MockSeeder {

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun seedAnalyticsData(
        database: AppDatabase
    ) {

        withContext(Dispatchers.IO) {

            val userDao = database.userDao()
            val accountDao = database.accountDao()
            val transactionDao = database.transactionDao()
            val budgetDao = database.budgetDao()

            // =========================
            // TẠO USER DEMO NẾU CHƯA TỒN TẠI
            // =========================

            var demoUser = userDao.getUserByEmail("demo@gmail.com")

            if (demoUser == null) {
                val hashedPassword = sha256("123456")
                val userId = userDao.insertUser(
                    UserEntity(
                        fullName = "Demo User",
                        email = "demo@gmail.com",
                        passwordHash = hashedPassword
                    )
                )
                demoUser = userDao.getUserById(userId)
            }

            if (demoUser == null) return@withContext

            // =========================
            // TẠO ACCOUNTS CHO USER DEMO
            // =========================

            val existingAccounts = accountDao.getAccountsByUser(demoUser.id).first()

            if (existingAccounts.isEmpty()) {
                // Cash account (mặc định)
                accountDao.insertAccount(
                    AccountEntity(
                        userId = demoUser.id,
                        name = "Cash",
                        lastFourDigits = "CASH",
                        type = AccountType.CASH,
                        balance = 500.0,
                        isActive = true
                    )
                )

                // Bank accounts
                accountDao.insertAccount(
                    AccountEntity(
                        userId = demoUser.id,
                        name = "Vietcombank",
                        lastFourDigits = "1234",
                        type = AccountType.SAVING,
                        balance = 3000.0,
                        isActive = true
                    )
                )
                accountDao.insertAccount(
                    AccountEntity(
                        userId = demoUser.id,
                        name = "Techcombank",
                        lastFourDigits = "5678",
                        type = AccountType.CHECKING,
                        balance = 2000.0,
                        isActive = true
                    )
                )
                accountDao.insertAccount(
                    AccountEntity(
                        userId = demoUser.id,
                        name = "Credit Card",
                        lastFourDigits = "9012",
                        type = AccountType.CREDIT_CARD,
                        balance = 0.0,
                        isActive = true
                    )
                )
            }

            // =========================
            // TẠO BUDGETS CHO USER DEMO
            // =========================

            val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            val existingBudgets = budgetDao.getBudgetsByUser(demoUser.id, currentMonth, currentYear).first()

            if (existingBudgets.isEmpty()) {
                val sampleBudgets = listOf(
                    BudgetEntity(
                        userId = demoUser.id,
                        categoryId = "food_drink",
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = 300.0,
                        spent = 0.0
                    ),
                    BudgetEntity(
                        userId = demoUser.id,
                        categoryId = "transport",
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = 150.0,
                        spent = 0.0
                    ),
                    BudgetEntity(
                        userId = demoUser.id,
                        categoryId = "shopping",
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = 200.0,
                        spent = 0.0
                    ),
                    BudgetEntity(
                        userId = demoUser.id,
                        categoryId = "housing",
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = 1200.0,
                        spent = 0.0
                    ),
                    BudgetEntity(
                        userId = demoUser.id,
                        categoryId = "entertainment",
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = 100.0,
                        spent = 0.0
                    ),
                    BudgetEntity(
                        userId = demoUser.id,
                        categoryId = "utilities",
                        month = currentMonth,
                        year = currentYear,
                        limitAmount = 80.0,
                        spent = 0.0
                    )
                )
                sampleBudgets.forEach { budget ->
                    budgetDao.insertOrReplaceBudget(budget)
                }
            }

            // =========================
            // TẠO TRANSACTIONS CHO USER DEMO
            // =========================

            val existingTransactions = transactionDao.getTransactionsByUserAndMonthOnce(
                userId = demoUser.id,
                month = currentMonth,
                year = currentYear
            )

            if (existingTransactions.isEmpty()) {
                // Lấy account Cash
                val cashAccount = accountDao.getAccountsByUser(demoUser.id).first()
                    .find { it.name == "Cash" }
                val cashAccountId = cashAccount?.id ?: 0

                val mockTransactions = MockData.transactions(
                    userId = demoUser.id,
                    accountId = cashAccountId
                )
                transactionDao.insertAll(mockTransactions)

                // Cập nhật spent cho budgets
                val budgets = budgetDao.getBudgetsByUser(demoUser.id, currentMonth, currentYear).first()
                val transactions = transactionDao.getTransactionsByUserAndMonthOnce(
                    userId = demoUser.id,
                    month = currentMonth,
                    year = currentYear
                )

                budgets.forEach { budget ->
                    val spentForCategory = transactions
                        .filter { it.categoryId == budget.categoryId && it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    if (spentForCategory > 0) {
                        budgetDao.updateSpent(budget.id, spentForCategory)
                    }
                }
            }
        }
    }
}