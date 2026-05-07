package com.example.expense_tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expense_tracker.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("""
        SELECT * FROM budgets 
        WHERE userId = :userId AND month = :month AND year = :year
        ORDER BY categoryId ASC
    """)
    fun getBudgetsByUser(userId: Long, month: Int, year: Int): Flow<List<BudgetEntity>>

    @Query("""
        SELECT * FROM budgets 
        WHERE userId = :userId AND categoryId = :categoryId 
          AND month = :month AND year = :year
        LIMIT 1
    """)
    suspend fun getBudgetByCategory(
        userId: Long,
        categoryId: String,
        month: Int,
        year: Int
    ): BudgetEntity?

    @Query("UPDATE budgets SET spent = :spent WHERE id = :budgetId")
    suspend fun updateSpent(budgetId: Long, spent: Double)

    // Used by Overview: budgets where spent > 50% of limitAmount
    @Query("""
        SELECT * FROM budgets
        WHERE userId = :userId AND month = :month AND year = :year
          AND spent > limitAmount * 0.5
    """)
    fun getAlertBudgets(userId: Long, month: Int, year: Int): Flow<List<BudgetEntity>>
}