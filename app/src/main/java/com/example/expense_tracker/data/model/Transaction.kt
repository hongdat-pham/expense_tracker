package com.example.expense_tracker.data.model

data class Transaction(
    val id: Int,
    val title: String,
    val categoryName: String,
    val amount: Double,
    val time: String,
    val date: String, // VD: "Nov 24"
    val type: String, // "Expense" hoặc "Income"
    val iconRes: Int  // ID của drawable icon
)