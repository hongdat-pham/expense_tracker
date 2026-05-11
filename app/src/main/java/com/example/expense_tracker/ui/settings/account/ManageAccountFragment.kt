package com.example.expense_tracker.ui.settings.account

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentManageAccountBinding
import com.example.expense_tracker.ui.settings.account.adapter.AccountAdapter
import com.example.expense_tracker.ui.settings.account.adapter.AccountUiModel

class ManageAccountFragment :
    Fragment(R.layout.fragment_manage_account) {

    private var _binding: FragmentManageAccountBinding? = null
    private val binding get() = _binding!!

    private val accountAdapter = AccountAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentManageAccountBinding.bind(view)

        setupRecyclerView()
        setupSpinner()
        setupMockData()
        setupSaveButton()
    }

    private fun setupRecyclerView() {
        binding.rvAccounts.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvAccounts.adapter = accountAdapter
    }

    private fun setupSpinner() {

        val types = listOf(
            "Saving",
            "Checking",
            "Credit Card",
            "Digital Wallet"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            types
        )

        binding.spinnerAccountType.adapter = adapter
    }

    private fun setupMockData() {

        accountAdapter.submitData(
            listOf(
                AccountUiModel(
                    "Vietcombank",
                    "Checking",
                    "4291",
                    1250.0
                ),
                AccountUiModel(
                    "MoMo Wallet",
                    "Digital Wallet",
                    "8888",
                    230.5
                )
            )
        )
    }

    private fun setupSaveButton() {

        binding.btnSaveAccount.setOnClickListener {

            val name =
                binding.etAccountName.text.toString().trim()

            val lastFour =
                binding.etLastFourDigits.text.toString().trim()

            if (name.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Enter account name",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (lastFour.length != 4) {

                Toast.makeText(
                    requireContext(),
                    "Need 4 digits",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            Toast.makeText(
                requireContext(),
                "Account Saved",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}