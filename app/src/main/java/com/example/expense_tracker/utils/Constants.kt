package com.example.expense_tracker.utils

import com.example.expense_tracker.R
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.model.Category
import com.example.expense_tracker.data.model.CategoryType

object Constants {

    fun getCategoryById(categoryId: String): Category? {
        return categoriesMap[categoryId]
    }

    fun getCategoriesByType(type: TransactionType): List<Category> {
        val targetType = when (type) {
            TransactionType.EXPENSE -> CategoryType.EXPENSE
            TransactionType.INCOME -> CategoryType.INCOME
        }
        return categoriesMap.values.filter { it.type == targetType }
    }

    fun getAllCategories(): List<Category> {
        return categoriesMap.values.toList()
    }

    fun getIconResource(iconName: String): Int {
        return when (iconName) {
            // Food & Dining
            "fastfood" -> R.drawable.ic_fastfood
            "dining" -> R.drawable.ic_dining
            "restaurant" -> R.drawable.ic_restaurant
            "grocery" -> R.drawable.ic_grocery

            // Transport
            "electric_car" -> R.drawable.ic_electric_car
            "car" -> R.drawable.ic_car
            "directions_car" -> R.drawable.ic_directions_car

            // Travel
            "flight" -> R.drawable.ic_flight
            "beach" -> R.drawable.ic_beach_access
            "beach_access" -> R.drawable.ic_beach_access

            // Entertainment
            "movie" -> R.drawable.ic_movie
            "sports_esports" -> R.drawable.ic_sports_esports

            // Health
            "health" -> R.drawable.ic_health
            "fitness" -> R.drawable.ic_fitness

            // Education
            "school" -> R.drawable.ic_school
            "library" -> R.drawable.ic_library_books
            "library_books" -> R.drawable.ic_library_books

            // Home/Housing
            "home" -> R.drawable.ic_home
            "other_houses" -> R.drawable.ic_other_houses

            // Finance
            "payments" -> R.drawable.ic_payments
            "savings" -> R.drawable.ic_savings
            "credit_card" -> R.drawable.ic_credit_card
            "wallet" -> R.drawable.ic_wallet
            "security" -> R.drawable.ic_security

            // Shopping
            "shopping_bag" -> R.drawable.ic_shopping_bag
            "local_mall" -> R.drawable.ic_local_mall

            // Utilities
            "bolt" -> R.drawable.ic_bolt

            // Personal Care
            "spa" -> R.drawable.ic_spa

            // Pets
            "pets" -> R.drawable.ic_pets

            // Technology
            "laptop" -> R.drawable.ic_laptop
            "smartphone" -> R.drawable.ic_smartphone

            // Gift
            "gift" -> R.drawable.ic_gift

            // Subscription
            "repeat" -> R.drawable.ic_repeat

            // Analytics
            "insights" -> R.drawable.ic_insights
            "trending_up" -> R.drawable.ic_trending_up

            // Settings
            "notifications" -> R.drawable.ic_notifications
            "fingerprint" -> R.drawable.ic_fingerprint

            // Default
            "receipt_long" -> R.drawable.ic_receipt
            else -> R.drawable.ic_receipt
        }
    }

    private val categoriesMap = mapOf(
        // =========================
        // INCOME categories
        // =========================
        "salary" to Category(
            id = "salary",
            displayName = "Salary",
            icon = "payments",
            type = CategoryType.INCOME,
            colorHex = "#4CAF50"
        ),
        "freelance" to Category(
            id = "freelance",
            displayName = "Freelance",
            icon = "laptop",
            type = CategoryType.INCOME,
            colorHex = "#2196F3"
        ),

        // =========================
        // EXPENSE categories - Food & Dining
        // =========================
        "food_drink" to Category(
            id = "food_drink",
            displayName = "Food & Drink",
            icon = "fastfood",
            type = CategoryType.EXPENSE,
            colorHex = "#FF9800"
        ),
        "restaurant" to Category(
            id = "restaurant",
            displayName = "Restaurant",
            icon = "dining",
            type = CategoryType.EXPENSE,
            colorHex = "#FF5722"
        ),
        "groceries" to Category(
            id = "groceries",
            displayName = "Groceries",
            icon = "grocery",
            type = CategoryType.EXPENSE,
            colorHex = "#8BC34A"
        ),

        // =========================
        // Transport
        // =========================
        "transport" to Category(
            id = "transport",
            displayName = "Transport",
            icon = "electric_car",
            type = CategoryType.EXPENSE,
            colorHex = "#9C27B0"
        ),
        "car" to Category(
            id = "car",
            displayName = "Car",
            icon = "car",
            type = CategoryType.EXPENSE,
            colorHex = "#673AB7"
        ),

        // =========================
        // Shopping
        // =========================
        "shopping" to Category(
            id = "shopping",
            displayName = "Shopping",
            icon = "shopping_bag",
            type = CategoryType.EXPENSE,
            colorHex = "#E91E63"
        ),

        // =========================
        // Housing
        // =========================
        "housing" to Category(
            id = "housing",
            displayName = "Housing",
            icon = "other_houses",
            type = CategoryType.EXPENSE,
            colorHex = "#795548"
        ),
        "home" to Category(
            id = "home",
            displayName = "Home",
            icon = "home",
            type = CategoryType.EXPENSE,
            colorHex = "#8D6E63"
        ),

        // =========================
        // Entertainment
        // =========================
        "entertainment" to Category(
            id = "entertainment",
            displayName = "Entertainment",
            icon = "sports_esports",
            type = CategoryType.EXPENSE,
            colorHex = "#FF4081"
        ),
        "movie" to Category(
            id = "movie",
            displayName = "Movie",
            icon = "movie",
            type = CategoryType.EXPENSE,
            colorHex = "#3F51B5"
        ),

        // =========================
        // Health
        // =========================
        "health" to Category(
            id = "health",
            displayName = "Health",
            icon = "health",
            type = CategoryType.EXPENSE,
            colorHex = "#4CAF50"
        ),

        // =========================
        // Education
        // =========================
        "education" to Category(
            id = "education",
            displayName = "Education",
            icon = "library",
            type = CategoryType.EXPENSE,
            colorHex = "#00BCD4"
        ),
        "school" to Category(
            id = "school",
            displayName = "School",
            icon = "school",
            type = CategoryType.EXPENSE,
            colorHex = "#009688"
        ),

        // =========================
        // Utilities
        // =========================
        "utilities" to Category(
            id = "utilities",
            displayName = "Utilities",
            icon = "bolt",
            type = CategoryType.EXPENSE,
            colorHex = "#FFC107"
        ),

        // =========================
        // Travel
        // =========================
        "travel" to Category(
            id = "travel",
            displayName = "Travel",
            icon = "beach",
            type = CategoryType.EXPENSE,
            colorHex = "#2196F3"
        ),
        "flight" to Category(
            id = "flight",
            displayName = "Flight",
            icon = "flight",
            type = CategoryType.EXPENSE,
            colorHex = "#448AFF"
        ),

        // =========================
        // Personal Care
        // =========================
        "personal_care" to Category(
            id = "personal_care",
            displayName = "Personal Care",
            icon = "spa",
            type = CategoryType.EXPENSE,
            colorHex = "#FF9800"
        ),

        // =========================
        // Pets
        // =========================
        "pet" to Category(
            id = "pet",
            displayName = "Pet",
            icon = "pets",
            type = CategoryType.EXPENSE,
            colorHex = "#8D6E63"
        ),

        // =========================
        // Others
        // =========================
        "other_expense" to Category(
            id = "other_expense",
            displayName = "Other",
            icon = "receipt_long",
            type = CategoryType.EXPENSE,
            colorHex = "#9E9E9E"
        ),
        "gift" to Category(
            id = "gift",
            displayName = "Gift",
            icon = "gift",
            type = CategoryType.EXPENSE,
            colorHex = "#FF5722"
        ),

        // =========================
        // Technology
        // =========================
        "electronics" to Category(
            id = "electronics",
            displayName = "Electronics",
            icon = "laptop",
            type = CategoryType.EXPENSE,
            colorHex = "#607D8B"
        ),

        // =========================
        // Financial
        // =========================
        "bank_fee" to Category(
            id = "bank_fee",
            displayName = "Bank Fee",
            icon = "credit_card",
            type = CategoryType.EXPENSE,
            colorHex = "#EF5350"
        ),
        "insurance" to Category(
            id = "insurance",
            displayName = "Insurance",
            icon = "security",
            type = CategoryType.EXPENSE,
            colorHex = "#66BB6A"
        ),
        "savings" to Category(
            id = "savings",
            displayName = "Savings",
            icon = "savings",
            type = CategoryType.EXPENSE,
            colorHex = "#42A5F5"
        ),

        // =========================
        // Subscription
        // =========================
        "subscription" to Category(
            id = "subscription",
            displayName = "Subscription",
            icon = "repeat",
            type = CategoryType.EXPENSE,
            colorHex = "#AB47BC"
        )
    )
}