package com.example.expense_tracker.data.repository

import com.example.expense_tracker.data.local.dao.UserDao
import com.example.expense_tracker.data.local.entity.UserEntity
import com.example.expense_tracker.utils.SharedPrefsHelper

class UserRepository(
    private val userDao: UserDao,
    private val sharedPrefsHelper: SharedPrefsHelper
) {

    /**
     * Returns the currently logged-in user, or null if no user is logged in.
     * Used by SettingsFragment to display name and email in the hero card.
     */
    suspend fun getCurrentUser(): UserEntity? {
        val userId = sharedPrefsHelper.getUserId()
        if (userId == -1L) return null
        return userDao.getUserById(userId)
    }

    /**
     * Clears the current session. Called on Log Out.
     */
    fun logout() {
        sharedPrefsHelper.clearUser()
    }
}