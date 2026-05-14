package com.example.expense_tracker.ui.transaction

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.AppDatabase
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.data.repository.TransactionRepository
import com.example.expense_tracker.databinding.FragmentNewTransactionBinding
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.DateUtils.toDisplayDate
import com.example.expense_tracker.utils.SharedPrefsHelper
import com.google.android.material.chip.Chip
import java.util.Calendar

class NewTransactionFragment : Fragment() {

    private var _binding: FragmentNewTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewTransactionViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        NewTransactionViewModelFactory(
            TransactionRepository(db.transactionDao(), db.accountDao(), db.budgetDao()),
            AccountRepository(db.accountDao())
        )
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setReceiptPath(it.toString())
            binding.ivReceiptPreview.setImageURI(it)
            binding.ivReceiptPreview.isVisible = true
            binding.tvAddReceipt.text = "Change Receipt"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = SharedPrefsHelper(requireContext())
        val userId = prefs.getUserId()

        setupToolbar()
        setupAmountField()
        setupTypeToggle()
        setupCategoryGrid()
        setupDatePicker()
        setupReceiptPicker()
        setupSaveButton(userId)
        observeViewModel(userId)

        viewModel.loadAccounts(userId)
    }

    private fun setupToolbar() {
        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAmountField() {
        binding.etAmount.text = null

        binding.etAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (binding.etAmount.text.isNullOrEmpty()) {
                    binding.etAmount.text = null
                }
            } else {
                if (binding.etAmount.text.isNullOrEmpty()) {
                    binding.etAmount.setText("0")
                }
            }
        }
    }

    private fun setupTypeToggle() {
        binding.btnExpense.setOnClickListener {
            viewModel.selectType(TransactionType.EXPENSE)
            updateTypeToggleUI(TransactionType.EXPENSE)
        }
        binding.btnIncome.setOnClickListener {
            viewModel.selectType(TransactionType.INCOME)
            updateTypeToggleUI(TransactionType.INCOME)
        }
        updateTypeToggleUI(TransactionType.EXPENSE)
    }

    private fun updateTypeToggleUI(type: TransactionType) {
        val isExpense = type == TransactionType.EXPENSE
        binding.btnExpense.isSelected = isExpense
        binding.btnIncome.isSelected = !isExpense

        binding.btnExpense.setBackgroundColor(
            if (isExpense) ContextCompat.getColor(requireContext(), R.color.primary)
            else ContextCompat.getColor(requireContext(), R.color.surface_container_high)
        )
        binding.btnExpense.setTextColor(
            if (isExpense) ContextCompat.getColor(requireContext(), R.color.on_primary)
            else ContextCompat.getColor(requireContext(), R.color.on_surface_variant)
        )
        binding.btnIncome.setBackgroundColor(
            if (!isExpense) ContextCompat.getColor(requireContext(), R.color.primary)
            else ContextCompat.getColor(requireContext(), R.color.surface_container_high)
        )
        binding.btnIncome.setTextColor(
            if (!isExpense) ContextCompat.getColor(requireContext(), R.color.on_primary)
            else ContextCompat.getColor(requireContext(), R.color.on_surface_variant)
        )
        refreshCategoryGrid(type)
    }

    private fun setupCategoryGrid() {
        binding.tvViewAll.setOnClickListener {
            val isExpanded = binding.cgCategoryExtra.isVisible
            binding.cgCategoryExtra.isVisible = !isExpanded
            binding.tvViewAll.text = if (isExpanded) "View All" else "Show Less"
        }
        refreshCategoryGrid(TransactionType.EXPENSE)
    }

    private fun refreshCategoryGrid(type: TransactionType) {
        val categories = Constants.getCategoriesByType(type)

        binding.cgCategory.removeAllViews()
        binding.cgCategoryExtra.removeAllViews()
        binding.cgCategoryExtra.isVisible = false
        binding.tvViewAll.text = "View All"

        categories.forEachIndexed { index, category ->
            val chip = Chip(requireContext()).apply {
                text = category.displayName
                chipIcon = ContextCompat.getDrawable(
                    requireContext(),
                    Constants.getIconResource(category.icon)
                )
                isChipIconVisible = true
                isCheckable = false
                isClickable = true
                tag = category.id

                setChipBackgroundColorResource(R.color.surface_container_high)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))

                setOnClickListener {
                    resetCategorySelection()
                    setChipBackgroundColorResource(R.color.primary)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary))
                    viewModel.selectCategory(category.id)
                }
            }
            if (index < 8) binding.cgCategory.addView(chip)
            else binding.cgCategoryExtra.addView(chip)
        }
    }

    private fun resetCategorySelection() {
        listOf(binding.cgCategory, binding.cgCategoryExtra).forEach { group ->
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as? Chip ?: continue
                chip.setChipBackgroundColorResource(R.color.surface_container_high)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                chip.isChecked = false
            }
        }
    }

    private fun setupDatePicker() {
        binding.tvTransactionDate.text = System.currentTimeMillis().toDisplayDate()

        binding.rowDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val picked = Calendar.getInstance().apply {
                        set(year, month, day, 0, 0, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    viewModel.selectDate(picked)
                    binding.tvTransactionDate.text = picked.toDisplayDate()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupReceiptPicker() {
        binding.layoutReceipt.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun setupSaveButton(userId: Long) {
        binding.btnSaveTransaction.setOnClickListener {
            val amountText = binding.etAmount.text.toString()
            val description = binding.etDescription.text.toString()

            if (description.isBlank()) {
                Toast.makeText(requireContext(), "Please enter a description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveTransaction(
                userId = userId,
                amountText = amountText,
                description = description
            )
        }
    }

    private fun observeViewModel(userId: Long) {
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            binding.cgPaymentMethod.removeAllViews()
            if (accounts.isEmpty()) {
                Toast.makeText(requireContext(), "Please add an account in Settings first", Toast.LENGTH_LONG).show()
                return@observe
            }

            accounts.forEach { account ->
                val chip = Chip(requireContext()).apply {
                    if (account.type == AccountType.CASH) {
                        text = "Cash"
                        chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_wallet)
                    } else {
                        text = "${account.name} ••••${account.lastFourDigits}"
                        chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_credit_card)
                    }
                    isChipIconVisible = true
                    isCheckable = false
                    isClickable = true
                    tag = account.id

                    setChipBackgroundColorResource(R.color.surface_container_high)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))

                    setOnClickListener {
                        resetPaymentMethodSelection()
                        setChipBackgroundColorResource(R.color.primary)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary))
                        viewModel.selectAccount(account.id)
                    }
                }
                binding.cgPaymentMethod.addView(chip)
            }
        }

        viewModel.selectedCategoryId.observe(viewLifecycleOwner) { selectedId ->
            listOf(binding.cgCategory, binding.cgCategoryExtra).forEach { group ->
                for (i in 0 until group.childCount) {
                    val chip = group.getChildAt(i) as? Chip ?: continue
                    if (chip.tag == selectedId) {
                        chip.setChipBackgroundColorResource(R.color.primary)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary))
                    } else {
                        chip.setChipBackgroundColorResource(R.color.surface_container_high)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                    }
                }
            }
        }

        viewModel.selectedAccountId.observe(viewLifecycleOwner) { selectedId ->
            for (i in 0 until binding.cgPaymentMethod.childCount) {
                val chip = binding.cgPaymentMethod.getChildAt(i) as? Chip ?: continue
                if (chip.tag == selectedId) {
                    chip.setChipBackgroundColorResource(R.color.primary)
                    chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary))
                } else {
                    chip.setChipBackgroundColorResource(R.color.surface_container_high)
                    chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            if (result) {
                findNavController().navigateUp()
            }
            viewModel.clearSaveResult()
        }
    }

    private fun resetPaymentMethodSelection() {
        for (i in 0 until binding.cgPaymentMethod.childCount) {
            val chip = binding.cgPaymentMethod.getChildAt(i) as? Chip ?: continue
            chip.setChipBackgroundColorResource(R.color.surface_container_high)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
            chip.isChecked = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}