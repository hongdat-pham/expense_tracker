package com.example.expense_tracker.ui.settings.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.data.model.Category
import com.example.expense_tracker.databinding.ItemBudgetBinding
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.CurrencyFormatter

data class BudgetItem(
    val category: Category,
    val budget: BudgetEntity?
)

class BudgetAdapter(
    private val onSetLimit: (Category) -> Unit,
    private val onEditLimit: (BudgetEntity, Category) -> Unit,
    private val onDeleteLimit: (Category) -> Unit
) : ListAdapter<BudgetItem, BudgetAdapter.BudgetViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BudgetViewHolder(binding, onSetLimit, onEditLimit, onDeleteLimit)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BudgetViewHolder(
        private val binding: ItemBudgetBinding,
        private val onSetLimit: (Category) -> Unit,
        private val onEditLimit: (BudgetEntity, Category) -> Unit,
        private val onDeleteLimit: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BudgetItem) {
            val category = item.category
            val budget = item.budget

            binding.tvCategoryName.text = category.displayName
            binding.ivCategoryIcon.setImageResource(Constants.getIconResource(category.icon))

            if (budget != null && budget.limitAmount > 0) {
                // Has budget - show progress
                val spent = budget.spent
                val limit = budget.limitAmount
                val percent = if (limit > 0) ((spent / limit) * 100).toInt().coerceIn(0, 100) else 0

                binding.groupProgress.visibility = android.view.View.VISIBLE
                binding.btnSetLimit.visibility = android.view.View.GONE
                binding.btnEdit.visibility = android.view.View.VISIBLE
                binding.btnDelete.visibility = android.view.View.VISIBLE

                binding.tvSpentVsLimit.text = "Spent ${CurrencyFormatter.formatAmount(spent)} of ${CurrencyFormatter.formatAmount(limit)}"
                binding.tvPercent.text = "$percent%"
                binding.progressBar.progress = percent

                val colorRes = if (percent >= 80) R.color.error else R.color.primary
                binding.tvPercent.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
                binding.progressBar.progressTintList = ContextCompat.getColorStateList(binding.root.context, colorRes)

                binding.btnEdit.setOnClickListener {
                    onEditLimit(budget, category)
                }
                binding.btnDelete.setOnClickListener {
                    onDeleteLimit(category)
                }
            } else {
                // No budget - hide progress, show Set button
                binding.groupProgress.visibility = android.view.View.GONE
                binding.btnSetLimit.visibility = android.view.View.VISIBLE
                binding.btnEdit.visibility = android.view.View.GONE
                binding.btnDelete.visibility = android.view.View.GONE

                binding.btnSetLimit.text = "Set Limit"
                binding.btnSetLimit.setOnClickListener {
                    onSetLimit(category)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BudgetItem>() {
            override fun areItemsTheSame(old: BudgetItem, new: BudgetItem): Boolean {
                return old.category.id == new.category.id
            }
            override fun areContentsTheSame(old: BudgetItem, new: BudgetItem): Boolean {
                return old.budget?.id == new.budget?.id &&
                        old.budget?.limitAmount == new.budget?.limitAmount
            }
        }
    }
}