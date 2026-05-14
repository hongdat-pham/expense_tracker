package com.example.expense_tracker.ui.settings.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.ExpenseTrackerApp
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.data.model.Category
import com.example.expense_tracker.databinding.FragmentBudgetLimitsBinding
import com.example.expense_tracker.ui.settings.budget.adapter.BudgetAdapter
import com.example.expense_tracker.utils.CurrencyFormatter
import com.example.expense_tracker.utils.SharedPrefsHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class BudgetLimitsFragment : Fragment() {

    private var _binding: FragmentBudgetLimitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var budgetAdapter: BudgetAdapter

    private val viewModel: BudgetLimitsViewModel by viewModels {
        val app = requireActivity().application as ExpenseTrackerApp
        BudgetLimitsViewModelFactory(
            app.budgetRepository,
            SharedPrefsHelper(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetLimitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecyclerView() {
        budgetAdapter = BudgetAdapter(
            onSetLimit = { category -> showLimitDialog(category, existingBudget = null) },
            onEditLimit = { budget, category -> showLimitDialog(category, budget) },
            onDeleteLimit = { category -> showDeleteConfirmDialog(category) }
        )
        binding.rvBudgets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBudgets.adapter = budgetAdapter
    }

    private fun observeViewModel() {
        viewModel.budgetItems.observe(viewLifecycleOwner) { items ->
            budgetAdapter.submitList(items)
        }

        viewModel.totalBudget.observe(viewLifecycleOwner) { total ->
            binding.tvTotalBudget.text = CurrencyFormatter.formatAmount(total)
        }

        viewModel.totalSpent.observe(viewLifecycleOwner) { spent ->
            binding.tvTotalSpent.text = CurrencyFormatter.formatAmount(spent)
            val remaining = (viewModel.totalBudget.value ?: 0.0) - spent
            binding.tvTotalRemaining.text = CurrencyFormatter.formatAmount(remaining.coerceAtLeast(0.0))
        }

        viewModel.totalPercent.observe(viewLifecycleOwner) { percent ->
            binding.tvTotalPercent.text = "$percent%"
            binding.progressTotal.progress = percent
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            if (result == true) {
                // Refresh sẽ tự động qua Flow
            }
        }
    }

    // Popup Set/Edit Limit đẹp hơn
    private fun showLimitDialog(category: Category, existingBudget: BudgetEntity?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_budget_limit, null)

        val inputLayout = dialogView.findViewById<TextInputLayout>(R.id.tilLimitAmount)
        val input = dialogView.findViewById<EditText>(R.id.etLimitAmount)

        input.hint = "Enter limit amount"
        existingBudget?.let {
            input.setText(it.limitAmount.toBigDecimal().toPlainString())
        }

        val title = if (existingBudget == null)
            "Set Limit"
        else
            "Edit Limit"

        val message = if (existingBudget == null)
            "Set a monthly spending limit for ${category.displayName}"
        else
            "Update the monthly spending limit for ${category.displayName}"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val raw = input.text.toString().toDoubleOrNull()
                if (raw != null && raw > 0) {
                    viewModel.saveLimitForCategory(category, raw)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Popup Delete Limit đẹp hơn
    private fun showDeleteConfirmDialog(category: Category) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Limit")
            .setMessage("Are you sure you want to delete the spending limit for \"${category.displayName}\"? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteLimitForCategory(category)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}