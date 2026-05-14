package com.example.expense_tracker.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ── User session ──────────────────────────────────────────────────────────

    fun saveUserId(userId: Long) = prefs.edit().putLong(KEY_USER_ID, userId).apply()
    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    fun saveUserEmail(email: String) = prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    fun saveUserFullName(fullName: String) = prefs.edit().putString(KEY_USER_FULL_NAME, fullName).apply()
    fun getUserFullName(): String? = prefs.getString(KEY_USER_FULL_NAME, null)

    fun isLoggedIn(): Boolean = prefs.getLong(KEY_USER_ID, -1L) != -1L

    fun clearUser() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_FULL_NAME)
            .apply()
    }

    // ── Settings toggles ─────────────────────────────────────────────────────

    fun getBiometricEnabled(): Boolean = prefs.getBoolean(KEY_BIOMETRIC, false)
    fun setBiometricEnabled(v: Boolean) = prefs.edit().putBoolean(KEY_BIOMETRIC, v).apply()

    fun getTwoFactorEnabled(): Boolean = prefs.getBoolean(KEY_TWO_FACTOR, false)
    fun setTwoFactorEnabled(v: Boolean) = prefs.edit().putBoolean(KEY_TWO_FACTOR, v).apply()

    fun getSpendingAlertsEnabled(): Boolean = prefs.getBoolean(KEY_ALERTS, true)
    fun setSpendingAlertsEnabled(v: Boolean) = prefs.edit().putBoolean(KEY_ALERTS, v).apply()

    fun getWeeklyReportEnabled(): Boolean = prefs.getBoolean(KEY_WEEKLY, true)
    fun setWeeklyReportEnabled(v: Boolean) = prefs.edit().putBoolean(KEY_WEEKLY, v).apply()

    companion object {
        private const val PREFS_NAME = "ventura_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_FULL_NAME = "user_full_name"
        private const val KEY_BIOMETRIC = "pref_biometric_enabled"
        private const val KEY_TWO_FACTOR = "pref_two_factor_enabled"
        private const val KEY_ALERTS = "pref_spending_alerts_enabled"
        private const val KEY_WEEKLY = "pref_weekly_report_enabled"
    }
}