package com.example.expense_tracker.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.data.repository.TransactionRepository

class NewTransactionViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTransactionViewModel::class.java)) {
            return NewTransactionViewModel(transactionRepository, accountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}