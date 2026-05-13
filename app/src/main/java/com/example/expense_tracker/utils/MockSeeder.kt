package com.example.expense_tracker.utils

import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
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

            // =========================
            // TẠO USER DEMO NẾU CHƯA TỒN TẠI
            // =========================

            var demoUser = userDao.getUserByEmail("demo@gmail.com")

            if (demoUser == null) {
                // Tạo user demo mới - PHẢI HASH PASSWORD
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

            // Tạo account cho user demo
            val accounts = accountDao.getAccountsByUser(demoUser.id).first()
            val account = if (accounts.isNotEmpty()) accounts[0] else null

            val accountId = if (account == null) {
                accountDao.insertAccount(
                    AccountEntity(
                        userId = demoUser.id,
                        name = "Main Wallet",
                        lastFourDigits = "8888",
                        type = AccountType.SAVING,
                        balance = 15000000.0,
                        isActive = true
                    )
                )
            } else {
                account.id
            }

            // Kiểm tra và seed transactions nếu chưa có
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            val existingTransactions = transactionDao.getTransactionsByUserAndMonthOnce(
                userId = demoUser.id,
                month = currentMonth,
                year = currentYear
            )

            if (existingTransactions.isEmpty()) {
                val mockTransactions = MockData.transactions(
                    userId = demoUser.id,
                    accountId = accountId
                )
                transactionDao.insertAll(mockTransactions)
            }
        }
    }
}