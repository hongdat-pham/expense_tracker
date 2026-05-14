package com.example.expense_tracker.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentActivityBinding
import com.example.expense_tracker.ui.activity.adapter.TransactionAdapter
import kotlinx.coroutines.launch
import java.util.Calendar

class ActivityFragment : Fragment() {

    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by viewModels {
        ActivityViewModelFactory(requireContext())
    }

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupFilterChips()
        setupDateFilter()
        setupClearDateButton()
        setupFab()
        observeUiState()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(
            onItemClick = { transaction ->
                val bundle = Bundle().apply { putLong("transactionId", transaction.id) }
                findNavController().navigate(R.id.activityDetailFragment, bundle)
            }
        )

        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupSearch() {
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }
    }

    // SỬA: Xóa chipRecurring khỏi danh sách filter
    private fun setupFilterChips() {
        val chips = mapOf(
            binding.chipAll to TransactionFilter.ALL,
            binding.chipExpenses to TransactionFilter.EXPENSES,
            binding.chipIncome to TransactionFilter.INCOME
            // ĐÃ XÓA binding.chipRecurring
        )

        chips.forEach { (chip, filter) ->
            chip.setOnClickListener {
                chips.keys.forEach { it.isChecked = false }
                chip.isChecked = true
                viewModel.setFilter(filter)
            }
        }
    }

    private fun setupDateFilter() {
        binding.btnDateFilter.setOnClickListener {
            showDateRangePicker()
        }
    }

    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, startYear, startMonth, startDay ->
                val startCal = Calendar.getInstance().apply {
                    set(startYear, startMonth, startDay, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                DatePickerDialog(
                    requireContext(),
                    { _, endYear, endMonth, endDay ->
                        val endCal = Calendar.getInstance().apply {
                            set(endYear, endMonth, endDay, 23, 59, 59)
                            set(Calendar.MILLISECOND, 999)
                        }
                        viewModel.setDateRange(startCal.timeInMillis, endCal.timeInMillis)
                        updateDateFilterLabel(startCal, endCal)
                        binding.btnClearDate.visibility = View.VISIBLE
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateFilterLabel(start: Calendar, end: Calendar) {
        val startStr = "${start.get(Calendar.MONTH) + 1}/${start.get(Calendar.DAY_OF_MONTH)}"
        val endStr = "${end.get(Calendar.MONTH) + 1}/${end.get(Calendar.DAY_OF_MONTH)}"
        binding.tvDateFilter.text = "$startStr – $endStr"
    }

    private fun setupClearDateButton() {
        binding.btnClearDate.setOnClickListener {
            viewModel.clearDateRange()
            binding.tvDateFilter.text = "All Dates"
            binding.btnClearDate.visibility = View.GONE
        }
    }

    private fun setupFab() {
        binding.fabAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_activity_to_newTransaction)
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

    // SỬA: Cập nhật UI chỉ với 3 chip
    private fun updateUi(state: ActivityUiState) {
        if (state.isEmpty) {
            binding.rvTransactions.visibility = View.GONE
            binding.emptyState.visibility = View.VISIBLE
        } else {
            binding.rvTransactions.visibility = View.VISIBLE
            binding.emptyState.visibility = View.GONE
            transactionAdapter.submitGroupedList(state.groupedTransactions)
        }

        val chipToCheck = when (state.activeFilter) {
            TransactionFilter.ALL -> binding.chipAll
            TransactionFilter.EXPENSES -> binding.chipExpenses
            TransactionFilter.INCOME -> binding.chipIncome
        }
        listOf(binding.chipAll, binding.chipExpenses, binding.chipIncome)
            .forEach { it.isChecked = false }
        chipToCheck.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}