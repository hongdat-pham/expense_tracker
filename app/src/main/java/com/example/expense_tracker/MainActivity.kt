package com.example.expense_tracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.expense_tracker.databinding.ActivityMainBinding
import com.example.expense_tracker.utils.SharedPrefsHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom nav wired to main nav graph
        binding.bottomNavigationView.setupWithNavController(navController)

        // Hide bottom nav on transaction/detail screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newTransactionFragment, R.id.activityDetailFragment -> {
                    binding.bottomNavigationView.visibility = android.view.View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = android.view.View.VISIBLE
                }
            }
        }
    }
}