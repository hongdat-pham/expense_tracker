package com.example.expense_tracker.ui.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class ActivityViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        @Suppress("UNCHECKED_CAST")
        return ActivityViewModel(
            transactionRepository = TransactionRepository(
                transactionDao = db.transactionDao(),
                accountDao = db.accountDao(),
                budgetDao = db.budgetDao()
            ),
            sharedPrefsHelper = SharedPrefsHelper(context)
        ) as T
    }
}