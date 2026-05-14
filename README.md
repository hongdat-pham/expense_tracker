# Ventura Finance 💳

> A personal expense tracking Android app built with Kotlin and modern Android architecture.


## ✨ Features

- **Authentication** — Register & Login with SHA-256 password hashing, auto-login on relaunch
- **Overview Dashboard** — Total balance, monthly spending, budget alerts, recent transactions
- **Activity Feed** — Full transaction history with search, filter by type (Expense / Income / Recurring), and date grouping
- **Transaction Detail** — Full breakdown of any transaction including receipt image
- **Add Transaction** — Create expense or income with category, date, payment method, description, and receipt photo
- **Analytics** — Spending breakdown by category (PieChart), monthly comparison (BarChart), key insights
- **Settings** — Profile display, biometric toggle, two-factor toggle, spending alerts
- **Manage Accounts** — Add and view linked bank accounts and digital wallets
- **Budget Limits** — Set monthly spending limits per category with progress tracking

---

## 🏗️ Architecture

```
MVVM + Repository Pattern
Single Activity + Jetpack Navigation
```

```
UI Layer        →  Fragments + ViewModels (LiveData)
Data Layer      →  Repositories
Local Storage   →  Room Database (4 tables)
State Storage   →  SharedPreferences (userId, session)
```

---

## 🗂️ Project Structure

```
com.example.expense_tracker
│
├── data/
│   ├── local/
│   │   ├── dao/          # AccountDao, BudgetDao, TransactionDao, UserDao
│   │   ├── entity/       # AccountEntity, BudgetEntity, TransactionEntity, UserEntity
│   │   └── AppDatabase.kt
│   ├── model/            # Account, Budget, Category, Transaction, User
│   └── repository/       # AccountRepository, BudgetRepository, TransactionRepository, UserRepository
│
├── ui/
│   ├── auth/             # LoginFragment, RegisterFragment, AuthViewModel
│   ├── overview/         # OverviewFragment, OverviewViewModel, AlertBudgetAdapter
│   ├── activity/         # ActivityFragment, ActivityDetailFragment, TransactionAdapter
│   ├── analytics/        # AnalyticsFragment, AnalyticsViewModel
│   ├── transaction/      # NewTransactionFragment, NewTransactionViewModel
│   └── settings/
│       ├── account/      # ManageAccountFragment, AccountAdapter
│       ├── budget/       # BudgetLimitsFragment, BudgetAdapter
│       └── SettingsFragment, SettingsViewModel
│
├── utils/
│   ├── Constants.kt      # DEFAULT_CATEGORIES (18 categories hardcoded)
│   ├── CurrencyFormatter.kt
│   ├── DateUtils.kt
│   ├── SharedPrefsHelper.kt
│   ├── MockData.kt
│   └── MockSeeder.kt
│
├── AuthActivity.kt
├── MainActivity.kt
├── SplashActivity.kt
└── ExpenseTrackerApp.kt
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| Architecture | MVVM + Repository |
| Navigation | Jetpack Navigation Component |
| Database | Room (with KSP) |
| UI Binding | ViewBinding |
| Async | Kotlin Coroutines + Flow |
| Charts | MPAndroidChart |
| Min SDK | API 26 (Android 8.0 Oreo) |
| Target SDK | API 35 |

---

## 🗃️ Database Schema

```
users         — id, fullName, email, passwordHash
accounts      — id, userId, name, lastFourDigits, type, balance, isActive
transactions  — id, userId, accountId, categoryId, amount, type, date, description, receiptPath, isRecurring
budgets       — id, userId, categoryId, limitAmount, spent, month, year
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17+
- Android device or emulator running API 26+

### Run the app
```bash
# Clone the repository
git clone https://github.com/<your-username>/Expense_tracker.git

# Open in Android Studio
# File → Open → select the Expense_tracker folder

# Run
# Click the Run button or press Shift+F10
```

### Build APK
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

---

## 👥 Team

| Member | Role |
|--------|------|
| Hồng Đạt | Auth + Database foundation |
| Thành | Transaction screens |
| Tiến Đạt | Overview + Activity screens |
| Quỳnh | Analytics screens |
| Phúc | Settings + Manage Accounts + Budget |

---

## 📋 Git Branching

```
main
└── develop
    ├── feature/auth
    ├── feature/transaction
    ├── feature/overview-activity
    ├── feature/analytics
    └── feature/settings
```

---

## 📄 License

This project is built for academic purposes.
