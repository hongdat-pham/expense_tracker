package com.example.expense_tracker.ui.settings.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.databinding.ItemAccountBinding

class AccountAdapter(
    private val onEditClick: (AccountEntity) -> Unit,
    private val onDeleteClick: (AccountEntity) -> Unit
) : ListAdapter<AccountEntity, AccountAdapter.AccountViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AccountViewHolder(binding, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AccountViewHolder(
        private val binding: ItemAccountBinding,
        private val onEditClick: (AccountEntity) -> Unit,
        private val onDeleteClick: (AccountEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(account: AccountEntity) {
            binding.tvAccountName.text = account.name
            binding.tvAccountMeta.text = "${account.type.toDisplayLabel()} •••• ${account.lastFourDigits}"
            binding.ivAccountIcon.setImageResource(account.type.toIconRes())

            // Cash account không thể xóa hoặc sửa
            val isCashAccount = account.type == AccountType.CASH
            if (isCashAccount) {
                binding.btnEdit.visibility = android.view.View.GONE
                binding.btnDelete.visibility = android.view.View.GONE
            } else {
                binding.btnEdit.visibility = android.view.View.VISIBLE
                binding.btnDelete.visibility = android.view.View.VISIBLE

                binding.btnEdit.setOnClickListener {
                    onEditClick(account)
                }
                binding.btnDelete.setOnClickListener {
                    onDeleteClick(account)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<AccountEntity>() {
            override fun areItemsTheSame(old: AccountEntity, new: AccountEntity) =
                old.id == new.id

            override fun areContentsTheSame(old: AccountEntity, new: AccountEntity) =
                old == new
        }
    }
}

// Extension helpers
private fun AccountType.toDisplayLabel(): String = when (this) {
    AccountType.CASH          -> "Cash"
    AccountType.SAVING        -> "Savings"
    AccountType.CHECKING      -> "Checking"
    AccountType.CREDIT_CARD   -> "Credit Card"
    AccountType.DIGITAL_WALLET -> "Digital Wallet"
}

private fun AccountType.toIconRes(): Int = when (this) {
    AccountType.CASH          -> R.drawable.ic_wallet
    AccountType.SAVING        -> R.drawable.ic_savings
    AccountType.CHECKING      -> R.drawable.ic_wallet
    AccountType.CREDIT_CARD   -> R.drawable.ic_credit_card
    AccountType.DIGITAL_WALLET -> R.drawable.ic_payments
}