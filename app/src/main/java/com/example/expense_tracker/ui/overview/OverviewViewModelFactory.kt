package com.example.expense_tracker.ui.overview

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.repository.BudgetRepository
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class OverviewViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        @Suppress("UNCHECKED_CAST")
        return OverviewViewModel(
            transactionRepository = TransactionRepository(
                transactionDao = db.transactionDao(),
                accountDao = db.accountDao(),
                budgetDao = db.budgetDao()
            ),
            budgetRepository = BudgetRepository(db.budgetDao()),
            sharedPrefsHelper = SharedPrefsHelper(context)
        ) as T
    }
}