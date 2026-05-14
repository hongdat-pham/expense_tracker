package com.example.expense_tracker.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
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
    val isLoading: Boolean = true
)

class OverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(OverviewUiState())
    val uiState: StateFlow<OverviewUiState> = _uiState.asStateFlow()

    private val userId: Long get() = sharedPrefsHelper.getUserId()

    init {
        loadDataRealtime()
    }

    // SỬA: Dùng Flow để lắng nghe thay đổi realtime
    private fun loadDataRealtime() {
        viewModelScope.launch {
            val month = DateUtils.getCurrentMonth()
            val year = DateUtils.getCurrentYear()

            // Lắng nghe transactions realtime từ database
            transactionRepository.getTransactionsByUserAndMonth(userId, month, year)
                .collectLatest { transactions ->

                    android.util.Log.d("OverviewVM", "Realtime update: ${transactions.size} transactions")

                    val monthlySpent = transactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    val monthlyIncome = transactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val recentTransactions = transactions
                        .sortedByDescending { it.date }
                        .take(5)

                    _uiState.value = OverviewUiState(
                        totalBalance = monthlyIncome - monthlySpent,
                        monthlySpent = monthlySpent,
                        monthlyIncome = monthlyIncome,
                        budgetRemaining = 0.0,
                        budgetTotal = 0.0,
                        budgetPercent = 0,
                        alertBudgets = emptyList(),
                        recentTransactions = recentTransactions,
                        isLoading = false
                    )
                }
        }
    }

    fun refresh() {
        // Không cần làm gì vì Flow đã tự cập nhật
        // Giữ lại để tương thích với code cũ nếu cần
    }
}