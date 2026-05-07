package com.example.expense_tracker.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserId(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    fun saveUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun saveUserFullName(fullName: String) {
        prefs.edit().putString(KEY_USER_FULL_NAME, fullName).apply()
    }

    fun getUserFullName(): String? {
        return prefs.getString(KEY_USER_FULL_NAME, null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getLong(KEY_USER_ID, -1L) != -1L
    }

    /**
     * Clears all user session data. Call this on Log Out.
     */
    fun clearUser() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_FULL_NAME)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "ventura_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_FULL_NAME = "user_full_name"
    }
}