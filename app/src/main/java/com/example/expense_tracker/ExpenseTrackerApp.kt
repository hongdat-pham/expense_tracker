package com.example.expense_tracker

import android.app.Application
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.utils.SharedPrefsHelper

/**
 * Application class — registered in AndroidManifest.xml via android:name=".ExpenseTrackerApp"
 *
 * Initializes singletons at startup so they're ready before any Activity/Fragment opens.
 */
class ExpenseTrackerApp : Application() {

    // Lazily initialized so the first call to db triggers Room setup on a background thread
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    val prefsHelper: SharedPrefsHelper by lazy {
        SharedPrefsHelper(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Warm up the DB instance on app start (avoids first-call delay in UI)
        database
    }
}