package com.example.expense_tracker.ui.settings.budget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentBudgetLimitsBinding
import com.example.expense_tracker.ui.settings.budget.adapter.BudgetAdapter
import com.example.expense_tracker.ui.settings.budget.adapter.BudgetUiModel
import com.example.expense_tracker.utils.CurrencyFormatter

class BudgetLimitsFragment :
    Fragment(R.layout.fragment_budget_limits) {

    private var _binding: FragmentBudgetLimitsBinding? = null
    private val binding get() = _binding!!

    private val budgetAdapter = BudgetAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBudgetLimitsBinding.bind(view)

        setupRecyclerView()
        setupBudget()
    }

    private fun setupRecyclerView() {

        binding.rvBudgets.layoutManager =
            GridLayoutManager(requireContext(), 2)

        binding.rvBudgets.adapter = budgetAdapter
    }

    private fun setupBudget() {

        val spent = 700.0
        val limit = 1000.0

        binding.tvTotalBudget.text =
            "${CurrencyFormatter.formatAmount(spent)} / ${
                CurrencyFormatter.formatAmount(limit)
            }"

        binding.progressTotalBudget.progress = 70

        budgetAdapter.submitData(
            listOf(
                BudgetUiModel(
                    "Food",
                    70.0,
                    100.0
                ),
                BudgetUiModel(
                    "Shopping",
                    120.0,
                    200.0
                ),
                BudgetUiModel(
                    "Transport",
                    40.0,
                    80.0
                ),
                BudgetUiModel(
                    "Health",
                    20.0,
                    100.0
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}