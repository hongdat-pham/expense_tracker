package com.example.expense_tracker
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.utils.MockSeeder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expense_tracker.utils.SharedPrefsHelper

/**
 * Entry point of the app.
 * Checks if user is already logged in → go to MainActivity (main nav).
 * Otherwise → go to AuthActivity (login/register nav).
 *
 * Set this as the launcher activity in AndroidManifest.xml.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            MockSeeder.seedAnalyticsData(
//                AppDatabase.getInstance(this@SplashActivity)
//            )
//        }
        val prefs = SharedPrefsHelper(this)

        val destination = if (prefs.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }

        startActivity(destination)
        finish()
    }
}