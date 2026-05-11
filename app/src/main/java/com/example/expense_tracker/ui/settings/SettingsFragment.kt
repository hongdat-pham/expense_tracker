package com.example.expense_tracker.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingsBinding.bind(view)

        setupUserInfo()
        setupClickEvents()
    }

    private fun setupUserInfo() {
        binding.tvUserName.text = "Phuc Dinh"
        binding.tvUserEmail.text = "phuc@gmail.com"
    }

    private fun setupClickEvents() {

        binding.btnManageAccounts.setOnClickListener {
            // TODO navigate ManageAccountFragment
        }

        binding.btnBudgetLimits.setOnClickListener {
            // TODO navigate BudgetLimitsFragment
        }

        binding.btnLogout.setOnClickListener {
            // TODO logout
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}