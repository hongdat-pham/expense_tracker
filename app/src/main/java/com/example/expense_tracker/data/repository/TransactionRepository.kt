package com.example.expense_tracker.data.repository

import com.example.expense_tracker.data.local.dao.AccountDao
import com.example.expense_tracker.data.local.dao.BudgetDao
import com.example.expense_tracker.data.local.dao.TransactionDao
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.utils.DateUtils
import kotlinx.coroutines.flow.Flow

/**
 * TransactionRepository — written by Thành (Người 2).
 * Analytics functions at the bottom added by Quỳnh (Người 4).
 */
class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao
) {

    // ── Core save logic (Thành) ───────────────────────────────────────────────

    /**
     * Saves a transaction and keeps Account balance + Budget spent in sync.
     * All three steps run sequentially; if any throws, the caller's coroutine
     * scope will cancel and Room's WAL ensures no partial writes are visible.
     */
    suspend fun saveTransaction(entity: TransactionEntity) {
        // 1 — insert
        transactionDao.insertTransaction(entity)

        // 2 — update account balance
        val account = accountDao.getById(entity.accountId)
        val newBalance = when (entity.type) {
            TransactionType.EXPENSE -> account.balance - entity.amount
            TransactionType.INCOME  -> account.balance + entity.amount
        }
        accountDao.updateBalance(entity.accountId, newBalance)

        // 3 — update budget spent (EXPENSE only)
        if (entity.type == TransactionType.EXPENSE) {
            val month  = DateUtils.getCurrentMonth()
            val year   = DateUtils.getCurrentYear()
            val budget = budgetDao.getBudgetByCategory(entity.userId, entity.categoryId, month, year)
            budget?.let { budgetDao.updateSpent(it.id, it.spent + entity.amount) }
        }
    }

    // ── Flow queries for UI screens (Thành / Tiến Đạt) ───────────────────────

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

    // ── One-shot suspend queries for Analytics (Quỳnh) ───────────────────────

    /**
     * Returns all transactions for a user in a given month as a plain List.
     * Used by AnalyticsViewModel to compute category breakdowns without keeping
     * a live Flow open for each of the 6 historical months.
     */
    suspend fun getTransactionsByUserAndMonthOnce(
        userId: Long, month: Int, year: Int
    ): List<TransactionEntity> =
        transactionDao.getTransactionsByUserAndMonthOnce(userId, month, year)

    /** Total EXPENSE for a month — AnalyticsViewModel BarChart + trend */
    suspend fun getTotalExpenseForMonth(userId: Long, month: Int, year: Int): Double =
        transactionDao.getTotalExpenseForMonth(userId, month, year)

    /** Total INCOME for a month — AnalyticsViewModel savings calculation */
    suspend fun getTotalIncomeForMonth(userId: Long, month: Int, year: Int): Double =
        transactionDao.getTotalIncomeForMonth(userId, month, year)

    /** Total EXPENSE for a specific category in a month — BudgetRepository also uses this */
    suspend fun getTotalSpentByCategory(
        userId: Long, categoryId: String, month: Int, year: Int
    ): Double = transactionDao.getTotalSpentByCategory(userId, categoryId, month, year)
}