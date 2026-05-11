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

    // ── Flow queries (live, for UI screens) ───────────────────────────────────

    /** All transactions for a user — Activity screen */
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactionsByUser(userId: Long): Flow<List<TransactionEntity>>

    /** Recent N transactions — Overview screen */
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
        ORDER BY date DESC
        LIMIT :limit
    """)
    fun getRecentTransactions(userId: Long, limit: Int = 5): Flow<List<TransactionEntity>>

    /**
     * Transactions for a specific month — Overview live card.
     * [month] is 1-based (1 = January). [year] is a 4-digit Int.
     * Using printf('%02d', :month) pads single digits: 1 → "01".
     */
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
        ORDER BY date DESC
    """)
    fun getTransactionsByUserAndMonth(
        userId: Long,
        month: Int,
        year: Int
    ): Flow<List<TransactionEntity>>

    /** Search by description — Activity search bar */
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
          AND description LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchTransactions(userId: Long, query: String): Flow<List<TransactionEntity>>

    /** Filter by type (EXPENSE / INCOME) — Activity filter chips */
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND type = :type
        ORDER BY date DESC
    """)
    fun getTransactionsByType(userId: Long, type: TransactionType): Flow<List<TransactionEntity>>

    /** Recurring only — Activity "Recurring" chip */
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND isRecurring = 1
        ORDER BY date DESC
    """)
    fun getRecurringTransactions(userId: Long): Flow<List<TransactionEntity>>

    /** All transactions for an account */
    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC")
    fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>>

    // ── Suspend (one-shot) queries — for Analytics & Budget logic ─────────────

    /**
     * One-shot list for a month — used by AnalyticsViewModel.
     * Returns a plain List (not Flow) so Analytics can do its own computation
     * without keeping a live collector open for each of the 6 historical months.
     */
    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
        ORDER BY date DESC
    """)
    suspend fun getTransactionsByUserAndMonthOnce(
        userId: Long,
        month: Int,
        year: Int
    ): List<TransactionEntity>

    /** Total EXPENSE for a category in a month — Budget logic & Analytics */
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId
          AND categoryId = :categoryId
          AND type = 'EXPENSE'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
    """)
    suspend fun getTotalSpentByCategory(
        userId: Long,
        categoryId: String,
        month: Int,
        year: Int
    ): Double

    /** Total EXPENSE for a month — Analytics BarChart */
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId
          AND type = 'EXPENSE'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
    """)
    suspend fun getTotalExpenseForMonth(userId: Long, month: Int, year: Int): Double

    /** Total INCOME for a month — Analytics savings calculation */
    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId
          AND type = 'INCOME'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
    """)
    suspend fun getTotalIncomeForMonth(userId: Long, month: Int, year: Int): Double

    /** Single transaction by ID — ActivityDetail */
    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?
}