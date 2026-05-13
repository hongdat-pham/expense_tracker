package com.example.expense_tracker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.databinding.FragmentActivityDetailBinding
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.CurrencyFormatter
import com.example.expense_tracker.utils.DateUtils.toFullDateTime
import kotlinx.coroutines.launch

class ActivityDetailFragment : Fragment() {

    private var _binding: FragmentActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val transactionId by lazy {
        arguments?.getLong("transactionId") ?: 0L
    }

    private val viewModel: ActivityDetailViewModel by viewModels {
        ActivityDetailViewModelFactory(requireContext(), transactionId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeData()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnShare.setOnClickListener {
            val tx = viewModel.transaction.value ?: return@setOnClickListener
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, buildShareText(tx))
            }
            startActivity(Intent.createChooser(intent, "Share transaction"))
        }

        binding.btnMore.setOnClickListener {
            showMoreMenu()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transaction.collect { tx ->
                    tx ?: return@collect
                    bindTransaction(tx)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accountName.collect { name ->
                    binding.tvPaymentMethod.text = name ?: "—"
                }
            }
        }
    }

    private fun bindTransaction(tx: TransactionEntity) {
        val category = Constants.getCategoryById(tx.categoryId)
        val isExpense = tx.type == TransactionType.EXPENSE

        // Số tiền
        binding.tvAmount.text = CurrencyFormatter.formatWithSign(tx.amount, tx.type)
        binding.tvAmount.setTextColor(
            requireContext().getColor(if (isExpense) R.color.error else R.color.tertiary)
        )

        // Tên giao dịch
        binding.tvMerchantName.text = tx.description.ifBlank {
            category?.displayName ?: tx.categoryId
        }

        // Icon - dùng ImageView
        val iconRes = Constants.getIconResource(category?.icon ?: "receipt_long")
        binding.ivCategoryIcon.setImageResource(iconRes)

        // Màu nền icon container
        binding.iconContainer.backgroundTintList = requireContext().getColorStateList(
            if (isExpense) R.color.error_container else R.color.tertiary_fixed
        )

        // Ngày giờ
        binding.tvDateTime.text = tx.date.toFullDateTime()

        // Tên category
        binding.tvCategory.text = category?.displayName ?: tx.categoryId

        // Description
        binding.tvDescription.text = if (tx.description.isNotBlank()) {
            "\"${tx.description}\""
        } else {
            getString(R.string.no_description)
        }

        // Receipt
        if (!tx.receiptPath.isNullOrBlank()) {
            binding.cardReceipt.visibility = View.VISIBLE
            Glide.with(this)
                .load(tx.receiptPath)
                .centerCrop()
                .into(binding.ivReceipt)
        } else {
            binding.cardReceipt.visibility = View.GONE
        }

        // Recurring badge
        binding.recurringBadge.visibility = if (tx.isRecurring) View.VISIBLE else View.GONE
    }

    private fun buildShareText(tx: TransactionEntity): String {
        val category = Constants.getCategoryById(tx.categoryId)
        val type = if (tx.type == TransactionType.EXPENSE) "Expense" else "Income"
        val amount = CurrencyFormatter.formatWithSign(tx.amount, tx.type)
        val date = tx.date.toFullDateTime()
        val catName = category?.displayName ?: tx.categoryId
        return "[$type] $amount — $catName\n$date\n${tx.description}"
    }

    private fun showMoreMenu() {
        val popup = PopupMenu(requireContext(), binding.btnMore)
        popup.menuInflater.inflate(R.menu.menu_transaction_detail, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    showDeleteConfirmation()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteTransaction()
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}