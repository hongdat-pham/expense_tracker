package com.example.expense_tracker

import android.app.Application
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.data.repository.BudgetRepository
import com.example.expense_tracker.data.repository.UserRepository
import com.example.expense_tracker.utils.SharedPrefsHelper

class ExpenseTrackerApp : Application() {

    lateinit var accountRepository: AccountRepository
    lateinit var budgetRepository: BudgetRepository
    lateinit var userRepository: UserRepository
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getInstance(this)
        sharedPrefsHelper = SharedPrefsHelper(this)

        accountRepository = AccountRepository(database.accountDao())
        budgetRepository = BudgetRepository(database.budgetDao())
        userRepository = UserRepository(database.userDao(), sharedPrefsHelper)
    }
}