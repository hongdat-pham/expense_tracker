package com.example.expense_tracker.ui.overview

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.BudgetEntity
import com.example.expense_tracker.databinding.ItemBudgetAlertBinding
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.CurrencyFormatter

class AlertBudgetAdapter :
    ListAdapter<BudgetEntity, AlertBudgetAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBudgetAlertBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemBudgetAlertBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: BudgetEntity) {
            val category = Constants.getCategoryById(budget.categoryId)

            // Tên category
            binding.tvCategoryName.text = category?.displayName ?: budget.categoryId

            // Số tiền đã tiêu / hạn mức
            val spent   = budget.spent
            val limit   = budget.limitAmount
            val percent = if (limit > 0) ((spent / limit) * 100).toInt().coerceIn(0, 100) else 0

            binding.tvSpentInfo.text =
                "Spent ${CurrencyFormatter.formatAmount(spent)} of ${CurrencyFormatter.formatAmount(limit)}"
            binding.tvPercent.text   = "$percent%"
            binding.progressBudget.progress = percent

            // Màu đỏ khi >= 80%, primary khi > 50%
            val colorRes = if (percent >= 80) R.color.error else R.color.primary
            val color    = binding.root.context.getColor(colorRes)
            binding.tvPercent.setTextColor(color)
            binding.progressBudget.setIndicatorColor(color)

            // Icon Material Symbols — set text vào tvIcon
            binding.tvIcon.text = category?.icon ?: "savings"

            // Màu nền từ Category.colorHex
            val bgColor = runCatching {
                Color.parseColor(category?.colorHex ?: "#F5F5F5")
            }.getOrDefault(Color.parseColor("#F5F5F5"))
            binding.iconContainer.backgroundTintList = ColorStateList.valueOf(bgColor)

            // Màu icon
            binding.tvIcon.setTextColor(
                binding.root.context.getColor(R.color.on_surface_variant)
            )
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<BudgetEntity>() {
        override fun areItemsTheSame(oldItem: BudgetEntity, newItem: BudgetEntity) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BudgetEntity, newItem: BudgetEntity) =
            oldItem == newItem
    }
}