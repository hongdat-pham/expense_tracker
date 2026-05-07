package com.example.expense_tracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expense_tracker.databinding.ActivityAuthBinding

/**
 * Hosts the auth navigation graph (Login ↔ Register).
 * After successful login, starts MainActivity and finishes itself.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}