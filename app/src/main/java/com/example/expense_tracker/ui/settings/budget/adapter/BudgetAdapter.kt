package com.example.expense_tracker.ui.settings.budget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.databinding.ItemBudgetBinding
import com.example.expense_tracker.utils.CurrencyFormatter

data class BudgetUiModel(
    val category: String,
    val spent: Double,
    val limit: Double
)

class BudgetAdapter :
    RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    private val items = mutableListOf<BudgetUiModel>()

    inner class BudgetViewHolder(
        private val binding: ItemBudgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BudgetUiModel) {

            binding.tvCategoryName.text = item.category

            binding.tvBudgetAmount.text =
                "${CurrencyFormatter.formatAmount(item.spent)} / ${
                    CurrencyFormatter.formatAmount(item.limit)
                }"

            val percent =
                ((item.spent / item.limit) * 100).toInt()

            binding.progressBudget.progress = percent
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BudgetViewHolder {

        val binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BudgetViewHolder,
        position: Int
    ) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(newItems: List<BudgetUiModel>) {

        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}