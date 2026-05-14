package com.example.expense_tracker.data.repository

import com.example.expense_tracker.data.local.dao.BudgetDao
import com.example.expense_tracker.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val budgetDao: BudgetDao
) {
    fun getBudgetsByUser(userId: Long, month: Int, year: Int): Flow<List<BudgetEntity>> =
        budgetDao.getBudgetsByUser(userId, month, year)

    suspend fun getBudgetByCategory(
        userId: Long,
        categoryId: String,
        month: Int,
        year: Int
    ): BudgetEntity? = budgetDao.getBudgetByCategory(userId, categoryId, month, year)

    suspend fun setBudgetLimit(
        userId: Long,
        categoryId: String,
        limit: Double,
        month: Int,
        year: Int
    ) {
        val existing = budgetDao.getBudgetByCategory(userId, categoryId, month, year)
        if (existing != null) {
            budgetDao.updateBudget(
                existing.copy(limitAmount = limit)
            )
        } else {
            budgetDao.insertOrReplaceBudget(
                BudgetEntity(
                    userId = userId,
                    categoryId = categoryId,
                    limitAmount = limit,
                    spent = 0.0,
                    month = month,
                    year = year
                )
            )
        }
    }

    suspend fun updateSpent(budgetId: Long, spent: Double) =
        budgetDao.updateSpent(budgetId, spent)
}