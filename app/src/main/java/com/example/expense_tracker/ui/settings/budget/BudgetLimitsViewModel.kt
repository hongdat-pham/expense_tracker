package com.example.expense_tracker.ui.settings.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.data.repository.BudgetRepository
import com.example.expense_tracker.data.model.Category
import com.example.expense_tracker.ui.settings.budget.adapter.BudgetItem
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.DateUtils
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BudgetLimitsViewModel(
    private val budgetRepository: BudgetRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val userId: Long = sharedPrefsHelper.getUserId()
    private val currentMonth = DateUtils.getCurrentMonth()
    private val currentYear = DateUtils.getCurrentYear()

    private val _budgetItems = MutableLiveData<List<BudgetItem>>()
    val budgetItems: LiveData<List<BudgetItem>> = _budgetItems

    private val _totalBudget = MutableLiveData<Double>()
    val totalBudget: LiveData<Double> = _totalBudget

    private val _totalSpent = MutableLiveData<Double>()
    val totalSpent: LiveData<Double> = _totalSpent

    private val _totalPercent = MutableLiveData<Int>()
    val totalPercent: LiveData<Int> = _totalPercent

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> = _saveResult

    init {
        loadBudgets()
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            budgetRepository.getBudgetsByUser(userId, currentMonth, currentYear)
                .collectLatest { budgetList ->
                    val budgetMap: Map<String, BudgetEntity> = budgetList.associateBy { it.categoryId }
                    val allCategories = Constants.getAllCategories()
                    val items = allCategories.map { category ->
                        BudgetItem(
                            category = category,
                            budget = budgetMap[category.id]
                        )
                    }
                    _budgetItems.postValue(items)

                    // Tính tổng budget
                    val totalLimit = budgetList.sumOf { it.limitAmount }
                    val totalSpentAmount = budgetList.sumOf { it.spent }
                    val totalPercentValue = if (totalLimit > 0) {
                        ((totalSpentAmount / totalLimit) * 100).toInt().coerceIn(0, 100)
                    } else 0

                    _totalBudget.postValue(totalLimit)
                    _totalSpent.postValue(totalSpentAmount)
                    _totalPercent.postValue(totalPercentValue)
                }
        }
    }

    fun saveLimitForCategory(category: Category, limit: Double) {
        if (limit <= 0) return
        viewModelScope.launch {
            budgetRepository.setBudgetLimit(
                userId = userId,
                categoryId = category.id,
                limit = limit,
                month = currentMonth,
                year = currentYear
            )
            _saveResult.postValue(true)
        }
    }

    fun deleteLimitForCategory(category: Category) {
        viewModelScope.launch {
            budgetRepository.setBudgetLimit(
                userId = userId,
                categoryId = category.id,
                limit = 0.0,
                month = currentMonth,
                year = currentYear
            )
            _saveResult.postValue(true)
        }
    }
}