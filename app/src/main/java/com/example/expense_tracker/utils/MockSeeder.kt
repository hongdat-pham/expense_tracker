package com.example.expense_tracker.utils

import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

object MockSeeder {

    suspend fun seedAnalyticsData(
        database: AppDatabase
    ) {

        withContext(Dispatchers.IO) {

            val userDao = database.userDao()
            val accountDao = database.accountDao()
            val transactionDao = database.transactionDao()

            // =========================
            // USER
            // =========================

            var user = userDao.getFirstUser()

            val userId = if (user == null) {

                userDao.insertUser(
                    UserEntity(
                        fullName = "Demo User",
                        email = "demo@gmail.com",
                        passwordHash = "123456"
                    )
                )

            } else {
                user.id
            }

            // =========================
            // ACCOUNT
            // =========================

            var account = accountDao.getFirstAccount()

            val accountId = if (account == null) {

                accountDao.insertAccount(
                    AccountEntity(
                        userId = userId,
                        name = "Cash Wallet",
                        lastFourDigits = "0000",
                        type = AccountType.DIGITAL_WALLET,
                        balance = 20000000.0
                    )
                )

            } else {
                account.id
            }

            // =========================
            // TRANSACTIONS
            // =========================

            val currentMonth =
                Calendar.getInstance().get(Calendar.MONTH) + 1

            val currentYear =
                Calendar.getInstance().get(Calendar.YEAR)

            val existingTransactions =
                transactionDao.getTransactionsByUserAndMonthOnce(
                    userId = userId,
                    month = currentMonth,
                    year = currentYear
                )

            if (existingTransactions.isNotEmpty()) {
                return@withContext
            }

            transactionDao.insertAll(
                MockData.transactions(
                    userId = userId,
                    accountId = accountId
                )
            )
        }
    }
}