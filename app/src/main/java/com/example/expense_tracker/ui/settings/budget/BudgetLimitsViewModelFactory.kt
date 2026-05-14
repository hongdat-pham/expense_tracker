package com.example.expense_tracker.ui.settings.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.repository.BudgetRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class BudgetLimitsViewModelFactory(
    private val budgetRepository: BudgetRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetLimitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetLimitsViewModel(budgetRepository, sharedPrefsHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}