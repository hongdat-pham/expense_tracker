package com.example.expense_tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    // All transactions for a user in a given month — used by Overview & Analytics
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = :year
        ORDER BY date DESC
    """)
    fun getTransactionsByUserAndMonth(
        userId: Long,
        month: Int,
        year: String
    ): Flow<List<TransactionEntity>>

    // All transactions for a user — used by Activity screen
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactionsByUser(userId: Long): Flow<List<TransactionEntity>>

    // Recent N transactions — used by Overview
    @Query("""
        SELECT * FROM transactions WHERE userId = :userId 
        ORDER BY date DESC LIMIT :limit
    """)
    fun getRecentTransactions(userId: Long, limit: Int = 5): Flow<List<TransactionEntity>>

    // Search by description — used by Activity search bar
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND description LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchTransactions(userId: Long, query: String): Flow<List<TransactionEntity>>

    // Filter by type (EXPENSE / INCOME) — used by Activity filter chips
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND type = :type
        ORDER BY date DESC
    """)
    fun getTransactionsByType(userId: Long, type: TransactionType): Flow<List<TransactionEntity>>

    // Recurring only — used by Activity "Recurring" chip
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND isRecurring = 1
        ORDER BY date DESC
    """)
    fun getRecurringTransactions(userId: Long): Flow<List<TransactionEntity>>

    // Total spent by category in a month — used by Budget logic & Analytics
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId AND categoryId = :categoryId AND type = 'EXPENSE'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = :year
    """)
    suspend fun getTotalSpentByCategory(
        userId: Long,
        categoryId: String,
        month: Int,
        year: String
    ): Double

    // Monthly totals for EXPENSE — used by Analytics BarChart
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId AND type = 'EXPENSE'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = :year
    """)
    suspend fun getTotalExpenseForMonth(userId: Long, month: Int, year: String): Double

    // Monthly totals for INCOME — used by Analytics LineChart
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId AND type = 'INCOME'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = :year
    """)
    suspend fun getTotalIncomeForMonth(userId: Long, month: Int, year: String): Double

    // Single transaction by ID — used by ActivityDetail
    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?

    // All transactions for an account — used internally
    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC")
    fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>>
}