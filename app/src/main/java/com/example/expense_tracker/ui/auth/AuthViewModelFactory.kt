package com.example.expense_tracker.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.utils.SharedPrefsHelper

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val db = AppDatabase.getInstance(context)
            val prefs = SharedPrefsHelper(context)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(db.userDao(), db.accountDao(), prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}