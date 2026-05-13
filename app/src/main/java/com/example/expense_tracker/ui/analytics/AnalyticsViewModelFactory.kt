package com.example.expense_tracker.ui.analytics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class AnalyticsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {
            val db = AppDatabase.getInstance(context)
            val txRepo = TransactionRepository(db.transactionDao(), db.accountDao(), db.budgetDao())
            val prefs = SharedPrefsHelper(context)
            return AnalyticsViewModel(txRepo, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}