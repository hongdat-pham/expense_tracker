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
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // Seed mock data cho tài khoản demo (chạy ngầm)
            MockSeeder.seedAnalyticsData(
                AppDatabase.getInstance(this@SplashActivity)
            )

            val prefs = SharedPrefsHelper(this@SplashActivity)

            // Kiểm tra đã đăng nhập chưa
            val destination = if (prefs.isLoggedIn()) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, AuthActivity::class.java)
            }

            startActivity(destination)
            finish()
        }
    }
}