package com.example.expense_tracker.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Wrapper around SharedPreferences for storing the logged-in user's session data.
 * Always access through the singleton — never instantiate directly.
 */
class SharedPrefsHelper private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    // ── User session ──────────────────────────────────────────

    fun saveUserId(id: Long) = prefs.edit().putLong(PrefKeys.USER_ID, id).apply()
    fun getUserId(): Long = prefs.getLong(PrefKeys.USER_ID, -1L)
    fun isLoggedIn(): Boolean = getUserId() != -1L

    fun saveUserEmail(email: String) = prefs.edit().putString(PrefKeys.USER_EMAIL, email).apply()
    fun getUserEmail(): String = prefs.getString(PrefKeys.USER_EMAIL, "") ?: ""

    fun saveUserFullName(name: String) = prefs.edit().putString(PrefKeys.USER_FULL_NAME, name).apply()
    fun getUserFullName(): String = prefs.getString(PrefKeys.USER_FULL_NAME, "") ?: ""

    /** Save all user session data in a single commit — call this after login */
    fun saveSession(userId: Long, email: String, fullName: String) {
        prefs.edit()
            .putLong(PrefKeys.USER_ID, userId)
            .putString(PrefKeys.USER_EMAIL, email)
            .putString(PrefKeys.USER_FULL_NAME, fullName)
            .apply()
    }

    /** Clear all session data — call this on logout */
    fun clearSession() {
        prefs.edit()
            .remove(PrefKeys.USER_ID)
            .remove(PrefKeys.USER_EMAIL)
            .remove(PrefKeys.USER_FULL_NAME)
            .apply()
    }

    // ── Settings toggles (UI-only, no real logic behind them) ─

    fun setBiometricEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(PrefKeys.BIOMETRIC_ENABLED, enabled).apply()
    fun isBiometricEnabled(): Boolean = prefs.getBoolean(PrefKeys.BIOMETRIC_ENABLED, false)

    fun setTwoFaEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(PrefKeys.TWO_FA_ENABLED, enabled).apply()
    fun isTwoFaEnabled(): Boolean = prefs.getBoolean(PrefKeys.TWO_FA_ENABLED, false)

    fun setSpendingAlertsEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(PrefKeys.SPENDING_ALERTS, enabled).apply()
    fun isSpendingAlertsEnabled(): Boolean = prefs.getBoolean(PrefKeys.SPENDING_ALERTS, true)

    fun setWeeklyReportsEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(PrefKeys.WEEKLY_REPORTS, enabled).apply()
    fun isWeeklyReportsEnabled(): Boolean = prefs.getBoolean(PrefKeys.WEEKLY_REPORTS, true)

    companion object {
        private const val PREF_FILE = "ventura_prefs"

        @Volatile
        private var INSTANCE: SharedPrefsHelper? = null

        fun getInstance(context: Context): SharedPrefsHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefsHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}