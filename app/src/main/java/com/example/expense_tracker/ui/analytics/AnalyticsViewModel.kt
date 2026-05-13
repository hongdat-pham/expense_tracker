package com.example.expense_tracker.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.model.Category
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.CurrencyFormatter
import com.example.expense_tracker.utils.DateUtils
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

data class CategorySpending(
    val category: Category,
    val amount: Double,
    val percent: Float
)

data class MonthlyTotal(
    val label: String,
    val month: Int,
    val year: Int,
    val expense: Double,
    val income: Double
)

data class IntensityRow(
    val category: Category,
    val amount: Double,
    val percent: Float
)

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val monthLabel: String = "",
    val totalSpent: Double = 0.0,
    val trendPercent: Double = 0.0,
    val categorySpending: List<CategorySpending> = emptyList(),
    val highestCategory: CategorySpending? = null,
    val estimatedSavings: Double = 0.0,
    val monthlyTotals: List<MonthlyTotal> = emptyList(),
    val avgMonthlyExpense: Double = 0.0,
    val intensityRows: List<IntensityRow> = emptyList()
)

class AnalyticsViewModel(
    private val transactionRepository: TransactionRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    private val _calendar = MutableStateFlow(Calendar.getInstance())

    init {
        viewModelScope.launch {
            _calendar.collectLatest { cal ->
                loadAnalytics(
                    month = cal.get(Calendar.MONTH) + 1,
                    year = cal.get(Calendar.YEAR)
                )
            }
        }
    }

    fun previousMonth() {
        _calendar.value = (_calendar.value.clone() as Calendar).apply {
            add(Calendar.MONTH, -1)
        }
    }

    fun nextMonth() {
        _calendar.value = (_calendar.value.clone() as Calendar).apply {
            add(Calendar.MONTH, 1)
        }
    }

    private suspend fun loadAnalytics(month: Int, year: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val userId = sharedPrefsHelper.getUserId()
        if (userId == -1L) {
            _uiState.value = AnalyticsUiState(isLoading = false)
            return
        }

        val transactions = transactionRepository.getTransactionsByUserAndMonthOnce(userId, month, year)
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        val incomes = transactions.filter { it.type == TransactionType.INCOME }

        val totalExpense = expenses.sumOf { it.amount }
        val totalIncome = incomes.sumOf { it.amount }

        val prevCal = (Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }).also { it.add(Calendar.MONTH, -1) }

        val prevExpense = transactionRepository.getTotalExpenseForMonth(
            userId,
            month = prevCal.get(Calendar.MONTH) + 1,
            year = prevCal.get(Calendar.YEAR)
        )
        val trendPercent = if (prevExpense > 0) ((totalExpense - prevExpense) / prevExpense) * 100.0 else 0.0

        val categorySpending = buildCategorySpending(expenses, totalExpense)
        val intensityRows = categorySpending.map { cs -> IntensityRow(cs.category, cs.amount, cs.percent) }
        val highestCategory = categorySpending.maxByOrNull { it.amount }

        val monthlyTotals = buildMonthlyTotals(userId)
        val avgExpense = monthlyTotals.map { it.expense }.average().takeIf { it.isFinite() } ?: 0.0

        _uiState.value = AnalyticsUiState(
            isLoading = false,
            monthLabel = DateUtils.toMonthYearLabel(month, year),
            totalSpent = totalExpense,
            trendPercent = trendPercent,
            categorySpending = categorySpending,
            highestCategory = highestCategory,
            estimatedSavings = totalIncome - totalExpense,
            monthlyTotals = monthlyTotals,
            avgMonthlyExpense = avgExpense,
            intensityRows = intensityRows
        )
    }

    private fun buildCategorySpending(
        expenses: List<TransactionEntity>,
        totalExpense: Double
    ): List<CategorySpending> {
        if (totalExpense == 0.0) return emptyList()

        return expenses
            .groupBy { it.categoryId }
            .mapNotNull { (categoryId, txList) ->
                val category = Constants.getCategoryById(categoryId) ?: return@mapNotNull null
                val amount = txList.sumOf { it.amount }
                val percent = ((amount / totalExpense) * 100).toFloat()
                CategorySpending(category, amount, percent)
            }
            .sortedByDescending { it.amount }
    }

    private suspend fun buildMonthlyTotals(userId: Long): List<MonthlyTotal> {
        return DateUtils.lastNMonths(6)
            .reversed()
            .map { (m, y) ->
                val expense = transactionRepository.getTotalExpenseForMonth(userId, m, y)
                val income = transactionRepository.getTotalIncomeForMonth(userId, m, y)
                MonthlyTotal(
                    label = DateUtils.toShortMonthLabel(m),
                    month = m,
                    year = y,
                    expense = expense,
                    income = income
                )
            }
    }
}