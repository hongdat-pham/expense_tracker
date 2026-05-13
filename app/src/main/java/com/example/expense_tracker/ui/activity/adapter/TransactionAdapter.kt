package com.example.expense_tracker.ui.activity.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.databinding.ItemDateHeaderBinding
import com.example.expense_tracker.databinding.ItemTransactionBinding
import com.example.expense_tracker.ui.activity.TransactionListItem
import com.example.expense_tracker.utils.Constants
import com.example.expense_tracker.utils.CurrencyFormatter
import com.example.expense_tracker.utils.DateUtils.toTimeString

private const val VIEW_TYPE_HEADER = 0
private const val VIEW_TYPE_ITEM = 1

class TransactionAdapter(
    private val onItemClick: (TransactionEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<TransactionListItem>()

    fun submitList(transactions: List<TransactionEntity>) {
        items.clear()
        items.addAll(transactions.map { TransactionListItem.Item(it) })
        notifyDataSetChanged()
    }

    fun submitGroupedList(grouped: List<TransactionListItem>) {
        items.clear()
        items.addAll(grouped)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is TransactionListItem.Header -> VIEW_TYPE_HEADER
        is TransactionListItem.Item -> VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ItemDateHeaderBinding.inflate(inflater, parent, false)
            )
            else -> ItemViewHolder(
                ItemTransactionBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is TransactionListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is TransactionListItem.Item -> (holder as ItemViewHolder).bind(item.transaction)
        }
    }

    override fun getItemCount() = items.size

    class HeaderViewHolder(
        private val binding: ItemDateHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: TransactionListItem.Header) {
            binding.tvGroupHeader.text = header.label
            binding.tvGroupDate.text = header.date
        }
    }

    inner class ItemViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionEntity) {
            val category = Constants.getCategoryById(transaction.categoryId)
            val isExpense = transaction.type == TransactionType.EXPENSE

            // Tên merchant/description
            binding.tvMerchantName.text = transaction.description.ifBlank {
                category?.displayName ?: transaction.categoryId
            }

            // Category tag
            binding.tvCategoryTag.text = category?.displayName ?: transaction.categoryId

            // Số tiền
            binding.tvAmount.text = CurrencyFormatter.formatWithSign(
                transaction.amount, transaction.type
            )
            binding.tvAmount.setTextColor(
                binding.root.context.getColor(
                    if (isExpense) R.color.error else R.color.tertiary
                )
            )

            // Thời gian
            binding.tvTime.text = transaction.date.toTimeString()

            // Icon - dùng ImageView (ivIcon)
            val iconRes = getIconResource(category?.icon ?: "receipt_long")
            binding.ivIcon.setImageResource(iconRes)

            // Màu nền icon container
            val bgColor = runCatching {
                Color.parseColor(category?.colorHex ?: "#E8E8E8")
            }.getOrDefault(Color.parseColor("#E8E8E8"))
            binding.iconContainer.backgroundTintList = ColorStateList.valueOf(bgColor)

            binding.root.setOnClickListener { onItemClick(transaction) }
        }

        private fun getIconResource(iconName: String): Int {
            return Constants.getIconResource(iconName)
        }
    }
}