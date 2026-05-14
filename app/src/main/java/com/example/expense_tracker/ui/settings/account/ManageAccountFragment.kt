package com.example.expense_tracker.ui.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.ExpenseTrackerApp
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.databinding.FragmentManageAccountBinding
import com.example.expense_tracker.ui.settings.account.adapter.AccountAdapter
import com.example.expense_tracker.utils.SharedPrefsHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ManageAccountFragment : Fragment() {

    private var _binding: FragmentManageAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var accountAdapter: AccountAdapter

    private val viewModel: ManageAccountViewModel by viewModels {
        val app = requireActivity().application as ExpenseTrackerApp
        ManageAccountViewModelFactory(
            app.accountRepository,
            SharedPrefsHelper(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAccountTypeDropdown()
        observeViewModel()
        setupListeners()
    }

    private fun setupRecyclerView() {
        accountAdapter = AccountAdapter(
            onEditClick = { account -> showEditDialog(account) },
            onDeleteClick = { account -> showDeleteConfirmDialog(account) }
        )
        binding.rvLinkedAccounts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLinkedAccounts.adapter = accountAdapter
    }

    private fun setupAccountTypeDropdown() {
        val types = listOf("Saving", "Checking", "Credit Card", "Digital Wallet")
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            types
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.spinnerAccountType.adapter = spinnerAdapter
    }

    private fun observeViewModel() {
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            accountAdapter.submitList(accounts)
            binding.tvEmptyAccounts.visibility =
                if (accounts.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ManageAccountViewModel.SaveResult.Success -> {
                    Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
                    clearForm()
                }
                is ManageAccountViewModel.SaveResult.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveAccount.setOnClickListener {
            val name = binding.etAccountName.text.toString()
            val lastFour = binding.etLastFourDigits.text.toString()
            val type = when (binding.spinnerAccountType.selectedItemPosition) {
                0 -> AccountType.SAVING
                1 -> AccountType.CHECKING
                2 -> AccountType.CREDIT_CARD
                3 -> AccountType.DIGITAL_WALLET
                else -> AccountType.SAVING
            }
            viewModel.saveAccount(name, lastFour, type)
        }
    }

    private fun showEditDialog(account: AccountEntity) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_account, null)

        val etName = dialogView.findViewById<EditText>(R.id.etAccountName)
        val etLastFour = dialogView.findViewById<EditText>(R.id.etLastFourDigits)

        etName.setText(account.name)
        etLastFour.setText(account.lastFourDigits)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Account")
            .setMessage("Update your account information")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newName = etName.text.toString()
                val newLastFour = etLastFour.text.toString()
                val newType = account.type
                viewModel.updateAccount(account, newName, newLastFour, newType)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmDialog(account: AccountEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete \"${account.name}\"? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteAccount(account)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun clearForm() {
        binding.etAccountName.text?.clear()
        binding.etLastFourDigits.text?.clear()
        binding.spinnerAccountType.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}