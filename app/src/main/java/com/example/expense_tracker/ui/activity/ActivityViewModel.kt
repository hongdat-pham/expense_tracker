package com.example.expense_tracker.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.utils.SharedPrefsHelper
import com.example.expense_tracker.utils.DateUtils
import com.example.expense_tracker.utils.DateUtils.toGroupHeader
import com.example.expense_tracker.utils.DateUtils.toShortDate
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Calendar

enum class TransactionFilter { ALL, EXPENSES, INCOME}

data class ActivityUiState(
    val groupedTransactions: List<TransactionListItem> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val activeFilter: TransactionFilter = TransactionFilter.ALL
)

sealed class TransactionListItem {
    data class Header(val label: String, val date: String) : TransactionListItem()
    data class Item(val transaction: TransactionEntity) : TransactionListItem()
}

@OptIn(FlowPreview::class)
class ActivityViewModel(
    private val transactionRepository: TransactionRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _activeFilter = MutableStateFlow(TransactionFilter.ALL)
    private val _startDate = MutableStateFlow<Long?>(null)
    private val _endDate = MutableStateFlow<Long?>(null)

    val activeFilter: StateFlow<TransactionFilter> = _activeFilter.asStateFlow()
    val startDate: StateFlow<Long?> = _startDate.asStateFlow()
    val endDate: StateFlow<Long?> = _endDate.asStateFlow()

    private val userId: Long get() = sharedPrefsHelper.getUserId()

    // THÊM: Lấy timestamp 30 ngày trước
    private val thirtyDaysAgo: Long by lazy {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -30)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            combine(
                _searchQuery.debounce(300L),
                _activeFilter,
                _startDate,
                _endDate
            ) { query, filter, start, end ->
                Quad(query, filter, start, end)
            }.flatMapLatest { (query, filter, start, end) ->
                when {
                    query.isNotBlank() ->
                        transactionRepository.searchTransactions(userId, query)
                    filter == TransactionFilter.EXPENSES ->
                        transactionRepository.getTransactionsByType(userId, TransactionType.EXPENSE)
                    filter == TransactionFilter.INCOME ->
                        transactionRepository.getTransactionsByType(userId, TransactionType.INCOME)
                    else ->
                        transactionRepository.getAllTransactionsByUser(userId)
                }
            }.collect { transactions ->
                // Lọc chỉ lấy 30 ngày gần nhất
                val filteredByDate = applyDefaultDateFilter(transactions)
                val filteredByRange = applyCustomDateFilter(filteredByDate)
                val grouped = groupByDate(filteredByRange)

                _uiState.value = ActivityUiState(
                    groupedTransactions = grouped,
                    isLoading = false,
                    isEmpty = grouped.isEmpty(),
                    activeFilter = _activeFilter.value
                )
            }
        }
    }

    // Hàm mới: Lọc transactions trong 30 ngày gần nhất (nếu không có custom filter)
    private fun applyDefaultDateFilter(transactions: List<TransactionEntity>): List<TransactionEntity> {
        // Nếu đang có custom date filter thì không áp dụng default
        if (_startDate.value != null || _endDate.value != null) {
            return transactions
        }
        return transactions.filter { it.date >= thirtyDaysAgo }
    }

    private fun applyCustomDateFilter(transactions: List<TransactionEntity>): List<TransactionEntity> {
        val start = _startDate.value ?: return transactions
        val end = _endDate.value ?: return transactions
        return transactions.filter { it.date in start..end }
    }

    private fun groupByDate(transactions: List<TransactionEntity>): List<TransactionListItem> {
        val result = mutableListOf<TransactionListItem>()
        val sorted = transactions.sortedByDescending { it.date }

        var lastGroup = ""
        for (tx in sorted) {
            val group = tx.date.toGroupHeader()
            val displayDate = tx.date.toShortDate()

            if (group != lastGroup) {
                result.add(TransactionListItem.Header(label = group, date = displayDate))
                lastGroup = group
            }
            result.add(TransactionListItem.Item(tx))
        }
        return result
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: TransactionFilter) {
        _activeFilter.value = filter
        _searchQuery.value = ""
    }

    fun setDateRange(startMs: Long, endMs: Long) {
        _startDate.value = startMs
        _endDate.value = endMs
    }

    fun clearDateRange() {
        _startDate.value = null
        _endDate.value = null
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)