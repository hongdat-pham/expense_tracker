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

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactionsByUser(userId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
        ORDER BY date DESC
        LIMIT :limit
    """)
    fun getRecentTransactions(userId: Long, limit: Int = 5): Flow<List<TransactionEntity>>

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

    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId
          AND description LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchTransactions(userId: Long, query: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND type = :type
        ORDER BY date DESC
    """)
    fun getTransactionsByType(userId: Long, type: TransactionType): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE userId = :userId AND isRecurring = 1
        ORDER BY date DESC
    """)
    fun getRecurringTransactions(userId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC")
    fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>>

    // ── Suspend (one-shot) queries ────────────────────────────────────────────

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

    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId
          AND type = 'EXPENSE'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
    """)
    suspend fun getTotalExpenseForMonth(userId: Long, month: Int, year: Int): Double

    @Query("""
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        WHERE userId = :userId
          AND type = 'INCOME'
          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', :month)
          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', :year)
    """)
    suspend fun getTotalIncomeForMonth(userId: Long, month: Int, year: Int): Double

    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    suspend fun getTransactionsByUser(userId: Long): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)
}