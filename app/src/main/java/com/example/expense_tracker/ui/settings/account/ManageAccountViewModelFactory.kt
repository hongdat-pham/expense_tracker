package com.example.expense_tracker.ui.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class ManageAccountViewModelFactory(
    private val accountRepository: AccountRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManageAccountViewModel(accountRepository, sharedPrefsHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}