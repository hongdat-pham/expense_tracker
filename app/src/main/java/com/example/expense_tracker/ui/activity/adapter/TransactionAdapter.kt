package com.example.expense_tracker.ui.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.R
import com.example.expense_tracker.data.model.Transaction

class TransactionAdapter(private val list: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val category: TextView = view.findViewById(R.id.tvCategoryTag)
        val amount: TextView = view.findViewById(R.id.tvAmount)
        val time: TextView = view.findViewById(R.id.tvTime)
        val iconContainer: com.google.android.material.card.MaterialCardView = view.findViewById(R.id.cardIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.category.text = item.categoryName
        holder.time.text = item.time

        if (item.type == "Expense") {
            holder.amount.text = "-$${String.format("%.2f", item.amount)}"
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark))
        } else {
            holder.amount.text = "+$${String.format("%.2f", item.amount)}"
            holder.amount.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark))
        }
    }

    override fun getItemCount() = list.size

    // Mock Data Helper
    companion object {
        fun getMockData(): List<Transaction> {
            return listOf(
                Transaction(1, "Whole Foods Market", "GROCERIES", 84.20, "10:45 AM", "Nov 24", "Expense", R.drawable.ic_shopping_cart),
                Transaction(2, "Freelance Project", "SALARY", 1250.00, "09:15 AM", "Nov 24", "Income", R.drawable.ic_payments),
                Transaction(3, "Blue Bottle Coffee", "DINING", 6.50, "03:22 PM", "Nov 23", "Expense", R.drawable.ic_restaurant)
            )
        }
    }
}