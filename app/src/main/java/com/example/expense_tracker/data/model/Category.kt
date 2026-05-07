//// utils/Constants.kt hoặc data/model/Category.kt
//
//enum class CategoryType { EXPENSE, INCOME }
//
//data class Category(
//    val id: String,           // dùng làm khóa lưu trong DB
//    val displayName: String,  // tên hiển thị trên UI — TIẾNG ANH
//    val icon: String,         // tên icon trong Material Symbols
//    val type: CategoryType,
//    val colorHex: String      // màu nền của icon trong UI
//)
//
//val DEFAULT_CATEGORIES = listOf(
//
//    // ── CHI TIÊU (EXPENSE) — 13 category ──────────────────
//    Category("food_drink",    "Food & Drink",    "restaurant",            EXPENSE, "#FFF3E0"),
//    Category("groceries",     "Groceries",       "local_grocery_store",   EXPENSE, "#E8F5E9"),
//    Category("transport",     "Transport",       "directions_car",        EXPENSE, "#E3F2FD"),
//    Category("shopping",      "Shopping",        "shopping_bag",          EXPENSE, "#F3E5F5"),
//    Category("housing",       "Housing",         "home",                  EXPENSE, "#E8EAF6"),
//    Category("entertainment", "Entertainment",   "movie",                 EXPENSE, "#FCE4EC"),
//    Category("health",        "Health",          "health_and_safety",     EXPENSE, "#E0F7FA"),
//    Category("education",     "Education",       "school",                EXPENSE, "#FFF8E1"),
//    Category("utilities",     "Utilities",       "bolt",                  EXPENSE, "#EFEBE9"),
//    Category("travel",        "Travel",          "flight",                EXPENSE, "#E0F2F1"),
//    Category("personal_care", "Personal Care",   "spa",                   EXPENSE, "#F8BBD0"),
//    Category("pet",           "Pet",             "pets",                  EXPENSE, "#DCEDC8"),
//    Category("other_expense", "Other",           "more_horiz",            EXPENSE, "#F5F5F5"),
//
//    // ── THU NHẬP (INCOME) — 5 category ────────────────────
//    Category("salary",        "Salary",          "account_balance_wallet", INCOME, "#E8F5E9"),
//    Category("freelance",     "Freelance",       "laptop_mac",             INCOME, "#E3F2FD"),
//    Category("investment",    "Investment",      "trending_up",            INCOME, "#FFF8E1"),
//    Category("gift",          "Gift",            "card_giftcard",          INCOME, "#FCE4EC"),
//    Category("other_income",  "Other Income",    "payments",               INCOME, "#F3E5F5"),
//)