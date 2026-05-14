package com.example.expense_tracker.ui.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.data.repository.TransactionRepository

class ActivityDetailViewModelFactory(
    private val context: Context,
    private val transactionId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        @Suppress("UNCHECKED_CAST")
        return ActivityDetailViewModel(
            transactionId = transactionId,
            transactionRepository = TransactionRepository(
                transactionDao = db.transactionDao(),
                accountDao = db.accountDao(),
                budgetDao = db.budgetDao()
            ),
            accountRepository = AccountRepository(db.accountDao())
        ) as T
    }
}