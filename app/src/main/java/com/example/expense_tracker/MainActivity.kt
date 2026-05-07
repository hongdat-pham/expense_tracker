package com.example.expense_tracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.expense_tracker.databinding.ActivityMainBinding
import com.example.expense_tracker.utils.SharedPrefsHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        // Kiểm tra đã đăng nhập chưa
        // Nếu chưa → load nav_auth, ẩn bottom nav
        // Nếu rồi → load nav_main, hiện bottom nav
        val prefs = SharedPrefsHelper.getInstance(this)
        if (prefs.isLoggedIn()) {
            navController.setGraph(R.navigation.nav_main)
            binding.bottomNavigationView.visibility = android.view.View.VISIBLE
        } else {
            navController.setGraph(R.navigation.nav_auth)
            binding.bottomNavigationView.visibility = android.view.View.GONE
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        // Ẩn bottom nav khi ở NewTransaction và ActivityDetail
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newTransactionFragment,
                R.id.activityDetailFragment -> {
                    binding.bottomNavigationView.visibility = android.view.View.GONE
                }
                R.id.loginFragment,
                R.id.registerFragment -> {
                    binding.bottomNavigationView.visibility = android.view.View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = android.view.View.VISIBLE
                }
            }
        }
    }
}