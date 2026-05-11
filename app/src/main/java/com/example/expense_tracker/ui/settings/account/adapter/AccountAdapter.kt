package com.example.expense_tracker.ui.settings.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.databinding.ItemAccountBinding
import com.example.expense_tracker.utils.CurrencyFormatter

data class AccountUiModel(
    val name: String,
    val type: String,
    val lastFourDigits: String,
    val balance: Double
)

class AccountAdapter :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private val items = mutableListOf<AccountUiModel>()

    inner class AccountViewHolder(
        private val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AccountUiModel) {

            binding.tvAccountName.text = item.name

            binding.tvAccountType.text =
                "${item.type} • **** ${item.lastFourDigits}"

            binding.tvAccountBalance.text =
                CurrencyFormatter.formatAmount(item.balance)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {

        val binding = ItemAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int
    ) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(newItems: List<AccountUiModel>) {

        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}