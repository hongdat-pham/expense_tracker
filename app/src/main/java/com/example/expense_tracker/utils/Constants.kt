package com.example.expense_tracker.utils

import com.example.expense_tracker.data.local.entity.TransactionType

// ─────────────────────────────────────────────────────────────
// Category model (not a Room entity — hardcoded, no DB needed)
// ─────────────────────────────────────────────────────────────

data class Category(
    val id: String,           // key used in DB (TransactionEntity.categoryId)
    val displayName: String,  // shown in UI — English only
    val icon: String,         // Material Symbols icon name
    val type: TransactionType,
    val colorHex: String      // icon background color in UI
)

// ─────────────────────────────────────────────────────────────
// 18 hardcoded categories — users cannot add or remove these
// ─────────────────────────────────────────────────────────────

val DEFAULT_CATEGORIES: List<Category> = listOf(

    // ── EXPENSE (13) ──────────────────────────────────────────
    Category("food_drink",    "Food & Drink",    "restaurant",             TransactionType.EXPENSE, "#FFF3E0"),
    Category("groceries",     "Groceries",       "local_grocery_store",    TransactionType.EXPENSE, "#E8F5E9"),
    Category("transport",     "Transport",       "directions_car",         TransactionType.EXPENSE, "#E3F2FD"),
    Category("shopping",      "Shopping",        "shopping_bag",           TransactionType.EXPENSE, "#F3E5F5"),
    Category("housing",       "Housing",         "home",                   TransactionType.EXPENSE, "#E8EAF6"),
    Category("entertainment", "Entertainment",   "movie",                  TransactionType.EXPENSE, "#FCE4EC"),
    Category("health",        "Health",          "health_and_safety",      TransactionType.EXPENSE, "#E0F7FA"),
    Category("education",     "Education",       "school",                 TransactionType.EXPENSE, "#FFF8E1"),
    Category("utilities",     "Utilities",       "bolt",                   TransactionType.EXPENSE, "#EFEBE9"),
    Category("travel",        "Travel",          "flight",                 TransactionType.EXPENSE, "#E0F2F1"),
    Category("personal_care", "Personal Care",   "spa",                    TransactionType.EXPENSE, "#F8BBD0"),
    Category("pet",           "Pet",             "pets",                   TransactionType.EXPENSE, "#DCEDC8"),
    Category("other_expense", "Other",           "more_horiz",             TransactionType.EXPENSE, "#F5F5F5"),

    // ── INCOME (5) ────────────────────────────────────────────
    Category("salary",        "Salary",          "account_balance_wallet", TransactionType.INCOME,  "#E8F5E9"),
    Category("freelance",     "Freelance",       "laptop_mac",             TransactionType.INCOME,  "#E3F2FD"),
    Category("investment",    "Investment",      "trending_up",            TransactionType.INCOME,  "#FFF8E1"),
    Category("gift",          "Gift",            "card_giftcard",          TransactionType.INCOME,  "#FCE4EC"),
    Category("other_income",  "Other Income",    "payments",               TransactionType.INCOME,  "#F3E5F5"),
)

// Quick lookup helpers used across the app
object CategoryHelper {
    private val map = DEFAULT_CATEGORIES.associateBy { it.id }

    fun getById(id: String): Category? = map[id]

    fun getExpenseCategories(): List<Category> =
        DEFAULT_CATEGORIES.filter { it.type == TransactionType.EXPENSE }

    fun getIncomeCategories(): List<Category> =
        DEFAULT_CATEGORIES.filter { it.type == TransactionType.INCOME }
}

// ─────────────────────────────────────────────────────────────
// Shared preference keys — use via SharedPrefsHelper, not raw strings
// ─────────────────────────────────────────────────────────────

object PrefKeys {
    const val USER_ID       = "user_id"
    const val USER_EMAIL    = "user_email"
    const val USER_FULL_NAME = "user_full_name"
    const val BIOMETRIC_ENABLED = "biometric_enabled"
    const val TWO_FA_ENABLED    = "two_fa_enabled"
    const val SPENDING_ALERTS   = "spending_alerts"
    const val WEEKLY_REPORTS    = "weekly_reports"
}