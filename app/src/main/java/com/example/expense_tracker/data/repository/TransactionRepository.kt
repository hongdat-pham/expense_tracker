package com.example.expense_tracker.data.repository

import com.example.expense_tracker.data.local.dao.AccountDao
import com.example.expense_tracker.data.local.dao.BudgetDao
import com.example.expense_tracker.data.local.dao.TransactionDao
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.utils.DateUtils
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao
) {

    suspend fun saveTransaction(entity: TransactionEntity) {
        transactionDao.insertTransaction(entity)
        val account = accountDao.getById(entity.accountId)
        val newBalance = when (entity.type) {
            TransactionType.EXPENSE -> account.balance - entity.amount
            TransactionType.INCOME  -> account.balance + entity.amount
        }
        accountDao.updateBalance(entity.accountId, newBalance)

        if (entity.type == TransactionType.EXPENSE) {
            val month  = DateUtils.getCurrentMonth()
            val year   = DateUtils.getCurrentYear()
            val budget = budgetDao.getBudgetByCategory(entity.userId, entity.categoryId, month, year)
            budget?.let { budgetDao.updateSpent(it.id, it.spent + entity.amount) }
        }
    }

    // Flow queries (realtime)
    fun getAllTransactionsByUser(userId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getAllTransactionsByUser(userId)

    fun getRecentTransactions(userId: Long, limit: Int = 5): Flow<List<TransactionEntity>> =
        transactionDao.getRecentTransactions(userId, limit)

    fun getTransactionsByUserAndMonth(
        userId: Long, month: Int, year: Int
    ): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByUserAndMonth(userId, month, year)

    fun searchTransactions(userId: Long, query: String): Flow<List<TransactionEntity>> =
        transactionDao.searchTransactions(userId, query)

    fun getTransactionsByType(userId: Long, type: TransactionType): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByType(userId, type)

    fun getRecurringTransactions(userId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getRecurringTransactions(userId)

    suspend fun getTransactionById(id: Long): TransactionEntity? =
        transactionDao.getTransactionById(id)

    // One-shot suspend queries
    suspend fun getTransactionsByUserAndMonthOnce(
        userId: Long, month: Int, year: Int
    ): List<TransactionEntity> =
        transactionDao.getTransactionsByUserAndMonthOnce(userId, month, year)

    suspend fun getTotalExpenseForMonth(userId: Long, month: Int, year: Int): Double =
        transactionDao.getTotalExpenseForMonth(userId, month, year)

    suspend fun getTotalIncomeForMonth(userId: Long, month: Int, year: Int): Double =
        transactionDao.getTotalIncomeForMonth(userId, month, year)

    suspend fun getTotalSpentByCategory(
        userId: Long, categoryId: String, month: Int, year: Int
    ): Double = transactionDao.getTotalSpentByCategory(userId, categoryId, month, year)

    // Additional methods
    suspend fun getById(id: Long): TransactionEntity? =
        transactionDao.getTransactionById(id)

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteById(transaction.id)
    }

    suspend fun getAllTransactions(): List<TransactionEntity> =
        transactionDao.getAllTransactions()

    suspend fun getTransactionsByUser(userId: Long): List<TransactionEntity> =
        transactionDao.getTransactionsByUser(userId)

    // THÊM: overload cho OverviewViewModel dùng Flow
    fun getTransactionsByUserFlow(userId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getAllTransactionsByUser(userId)
}