package com.example.expense_tracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.repository.UserRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class SettingsViewModelFactory(
    private val userRepository: UserRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(userRepository, sharedPrefsHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}