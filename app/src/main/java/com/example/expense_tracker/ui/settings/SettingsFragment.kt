package com.example.expense_tracker.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expense_tracker.AuthActivity
import com.example.expense_tracker.ExpenseTrackerApp
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentSettingsBinding
import com.example.expense_tracker.utils.SharedPrefsHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        val app = requireActivity().application as ExpenseTrackerApp
        SettingsViewModelFactory(
            app.userRepository,
            SharedPrefsHelper(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvUserName.text = name
        }
        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.tvUserEmail.text = email
        }
        viewModel.biometricEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchBiometric.isChecked = enabled
        }
        viewModel.twoFactorEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchTwoFactor.isChecked = enabled
        }
        viewModel.spendingAlertsEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchSpendingAlerts.isChecked = enabled
        }
        viewModel.weeklyReportEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchWeeklyReport.isChecked = enabled
        }
    }

    private fun setupListeners() {
        // Navigate to Manage Account
        binding.rowManageAccounts.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_manageAccount)
        }

        // Navigate to Budget Limits
        binding.rowBudgetLimits.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_budgetLimits)
        }

        // UI-only toggles
        binding.switchBiometric.setOnCheckedChangeListener { _, checked ->
            viewModel.setBiometric(checked)
        }
        binding.switchTwoFactor.setOnCheckedChangeListener { _, checked ->
            viewModel.setTwoFactor(checked)
        }
        binding.switchSpendingAlerts.setOnCheckedChangeListener { _, checked ->
            viewModel.setSpendingAlerts(checked)
        }
        binding.switchWeeklyReport.setOnCheckedChangeListener { _, checked ->
            viewModel.setWeeklyReport(checked)
        }

        // Stub rows
        binding.rowHelpCenter.setOnClickListener { /* TODO */ }
        binding.rowPrivacyPolicy.setOnClickListener { /* TODO */ }

        // Logout: clear and navigate to login
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}