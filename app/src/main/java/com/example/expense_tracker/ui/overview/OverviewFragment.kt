package com.example.expense_tracker.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentOverviewBinding
import com.example.expense_tracker.ui.activity.adapter.TransactionAdapter
import com.example.expense_tracker.utils.CurrencyFormatter
import kotlinx.coroutines.launch

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by viewModels {
        OverviewViewModelFactory(requireContext())
    }

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var alertBudgetAdapter: AlertBudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupClickListeners()
        observeUiState()
    }

    private fun setupRecyclerViews() {
        transactionAdapter = TransactionAdapter(
            onItemClick = { transaction ->
                val bundle = Bundle().apply { putLong("transactionId", transaction.id) }
                findNavController().navigate(R.id.activityDetailFragment, bundle)
            }
        )
        binding.rvRecentTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
            isNestedScrollingEnabled = false
        }

        alertBudgetAdapter = AlertBudgetAdapter()
        binding.rvAlertBudgets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alertBudgetAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupClickListeners() {
        binding.tvViewAll.setOnClickListener {
            val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigationView
            )
            bottomNav?.selectedItemId = R.id.navigation_activity
        }

        binding.fabAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_overview_to_newTransaction)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: OverviewUiState) {
        // Update total balance
        binding.tvTotalBalance.text = CurrencyFormatter.formatAmount(state.totalBalance)

        // Update balance change percentage
        val changeText = if (state.balanceChangePercent >= 0) {
            "+${String.format("%.1f", state.balanceChangePercent)}%"
        } else {
            "${String.format("%.1f", state.balanceChangePercent)}%"
        }
        binding.tvBalanceChange.text = changeText

        val changeColor = if (state.balanceChangePercent >= 0) R.color.tertiary else R.color.error
        binding.tvBalanceChange.setTextColor(requireContext().getColor(changeColor))
        binding.tvBalanceChange.setBackgroundResource(
            if (state.balanceChangePercent >= 0) R.drawable.badge_income_bg else R.drawable.badge_neutral_bg
        )

        // Update monthly spending
        binding.tvMonthlySpent.text = CurrencyFormatter.formatAmount(state.monthlySpent)

        // Update budget remaining
        binding.tvBudgetRemaining.text = CurrencyFormatter.formatAmount(state.budgetRemaining)
        binding.tvBudgetTotal.text = " / ${CurrencyFormatter.formatCompact(state.budgetTotal)}"

        // Update budget progress
        val budgetPercent = if (state.budgetTotal > 0) {
            ((state.monthlySpent / state.budgetTotal) * 100).toInt().coerceIn(0, 100)
        } else 0
        binding.progressBudget.progress = budgetPercent
        binding.tvBudgetPercent.text = "You've reached ${budgetPercent}% of your monthly limit."

        // Update alert budgets section
        if (state.alertBudgets.isNotEmpty()) {
            binding.sectionAlertBudgets.visibility = View.VISIBLE
            alertBudgetAdapter.submitList(state.alertBudgets)
        } else {
            binding.sectionAlertBudgets.visibility = View.GONE
        }

        // Update recent transactions section
        if (state.recentTransactions.isNotEmpty()) {
            binding.rvRecentTransactions.visibility = View.VISIBLE
            binding.emptyRecentState.visibility = View.GONE
            transactionAdapter.submitList(state.recentTransactions)
        } else {
            binding.rvRecentTransactions.visibility = View.GONE
            binding.emptyRecentState.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}