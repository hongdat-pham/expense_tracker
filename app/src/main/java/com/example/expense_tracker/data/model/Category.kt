package com.example.expense_tracker.data.model

enum class CategoryType { EXPENSE, INCOME }

data class Category(
    val id: String,
    val displayName: String,
    val icon: String,           // Material Symbols name
    val type: CategoryType,
    val colorHex: String        // background color for icon
)