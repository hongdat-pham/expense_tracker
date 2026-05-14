package com.example.expense_tracker.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.repository.BudgetRepository
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.utils.DateUtils
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class OverviewUiState(
    val totalBalance: Double = 0.0,
    val monthlySpent: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val budgetRemaining: Double = 0.0,
    val budgetTotal: Double = 0.0,
    val budgetPercent: Int = 0,
    val alertBudgets: List<BudgetEntity> = emptyList(),
    val recentTransactions: List<TransactionEntity> = emptyList(),
    val balanceChangePercent: Double = 0.0,
    val isLoading: Boolean = true
)

class OverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(OverviewUiState())
    val uiState: StateFlow<OverviewUiState> = _uiState.asStateFlow()

    private val userId: Long get() = sharedPrefsHelper.getUserId()

    init {
        loadDataRealtime()
    }

    private fun loadDataRealtime() {
        viewModelScope.launch {
            val month = DateUtils.getCurrentMonth()
            val year = DateUtils.getCurrentYear()

            // Tính tháng trước để lấy % thay đổi
            val prevMonth = if (month == 1) 12 else month - 1
            val prevYear = if (month == 1) year - 1 else year

            // Lấy transactions tháng trước
            val prevTransactions = transactionRepository.getTransactionsByUserAndMonthOnce(userId, prevMonth, prevYear)
            val prevTotalSpent = prevTransactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            // Lắng nghe transactions realtime
            transactionRepository.getTransactionsByUserAndMonth(userId, month, year)
                .collectLatest { transactions ->

                    val monthlySpent = transactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    val monthlyIncome = transactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val recentTransactions = transactions
                        .sortedByDescending { it.date }
                        .take(5)

                    // Tính % thay đổi so với tháng trước
                    val balanceChangePercent = if (prevTotalSpent > 0) {
                        ((monthlySpent - prevTotalSpent) / prevTotalSpent) * 100
                    } else {
                        0.0
                    }

                    // Budget Left = Tổng chi / Tổng thu
                    val budgetRemaining = (monthlyIncome - monthlySpent).coerceAtLeast(0.0)
                    val budgetTotal = monthlyIncome
                    val budgetPercent = if (monthlyIncome > 0) {
                        ((monthlySpent / monthlyIncome) * 100).toInt().coerceIn(0, 100)
                    } else 0

                    // Lấy budgets realtime cho alert
                    budgetRepository.getBudgetsByUser(userId, month, year)
                        .collectLatest { budgetList ->

                            val alertBudgets = budgetList.filter {
                                it.limitAmount > 0 && it.spent > it.limitAmount * 0.5
                            }

                            _uiState.value = OverviewUiState(
                                totalBalance = monthlyIncome - monthlySpent,
                                monthlySpent = monthlySpent,
                                monthlyIncome = monthlyIncome,
                                budgetRemaining = budgetRemaining,
                                budgetTotal = budgetTotal,
                                budgetPercent = budgetPercent,
                                alertBudgets = alertBudgets,
                                recentTransactions = recentTransactions,
                                balanceChangePercent = balanceChangePercent,
                                isLoading = false
                            )
                        }
                }
        }
    }

    fun refresh() {
        // Flow tự cập nhật, không cần làm gì
    }
}