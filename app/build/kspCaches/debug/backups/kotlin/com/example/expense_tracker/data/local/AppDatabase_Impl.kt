package com.example.expense_tracker.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.expense_tracker.`data`.local.dao.AccountDao
import com.example.expense_tracker.`data`.local.dao.AccountDao_Impl
import com.example.expense_tracker.`data`.local.dao.BudgetDao
import com.example.expense_tracker.`data`.local.dao.BudgetDao_Impl
import com.example.expense_tracker.`data`.local.dao.TransactionDao
import com.example.expense_tracker.`data`.local.dao.TransactionDao_Impl
import com.example.expense_tracker.`data`.local.dao.UserDao
import com.example.expense_tracker.`data`.local.dao.UserDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  private val _accountDao: Lazy<AccountDao> = lazy {
    AccountDao_Impl(this)
  }

  private val _transactionDao: Lazy<TransactionDao> = lazy {
    TransactionDao_Impl(this)
  }

  private val _budgetDao: Lazy<BudgetDao> = lazy {
    BudgetDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "2e2f58d0ef160a99f642c879c75e510a", "0d7354b6912054147ae04c4142a14dd0") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fullName` TEXT NOT NULL, `email` TEXT NOT NULL, `passwordHash` TEXT NOT NULL)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_email` ON `users` (`email`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `lastFourDigits` TEXT NOT NULL, `type` TEXT NOT NULL, `balance` REAL NOT NULL, `isActive` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_accounts_userId` ON `accounts` (`userId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `accountId` INTEGER NOT NULL, `categoryId` TEXT NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `date` INTEGER NOT NULL, `description` TEXT NOT NULL, `receiptPath` TEXT, `isRecurring` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_userId` ON `transactions` (`userId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_accountId` ON `transactions` (`accountId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `categoryId` TEXT NOT NULL, `limitAmount` REAL NOT NULL, `spent` REAL NOT NULL, `month` INTEGER NOT NULL, `year` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_userId` ON `budgets` (`userId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2e2f58d0ef160a99f642c879c75e510a')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `accounts`")
        connection.execSQL("DROP TABLE IF EXISTS `transactions`")
        connection.execSQL("DROP TABLE IF EXISTS `budgets`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("fullName", TableInfo.Column("fullName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("passwordHash", TableInfo.Column("passwordHash", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesUsers.add(TableInfo.Index("index_users_email", true, listOf("email"),
            listOf("ASC")))
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers,
            _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.example.expense_tracker.data.local.entity.UserEntity).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsAccounts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAccounts.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("lastFourDigits", TableInfo.Column("lastFourDigits", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("balance", TableInfo.Column("balance", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAccounts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysAccounts.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION",
            listOf("userId"), listOf("id")))
        val _indicesAccounts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesAccounts.add(TableInfo.Index("index_accounts_userId", false, listOf("userId"),
            listOf("ASC")))
        val _infoAccounts: TableInfo = TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts,
            _indicesAccounts)
        val _existingAccounts: TableInfo = read(connection, "accounts")
        if (!_infoAccounts.equals(_existingAccounts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |accounts(com.example.expense_tracker.data.local.entity.AccountEntity).
              | Expected:
              |""".trimMargin() + _infoAccounts + """
              |
              | Found:
              |""".trimMargin() + _existingAccounts)
        }
        val _columnsTransactions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransactions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("accountId", TableInfo.Column("accountId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("categoryId", TableInfo.Column("categoryId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("amount", TableInfo.Column("amount", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("date", TableInfo.Column("date", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("description", TableInfo.Column("description", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("receiptPath", TableInfo.Column("receiptPath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("isRecurring", TableInfo.Column("isRecurring", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransactions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTransactions.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION",
            listOf("userId"), listOf("id")))
        _foreignKeysTransactions.add(TableInfo.ForeignKey("accounts", "CASCADE", "NO ACTION",
            listOf("accountId"), listOf("id")))
        val _indicesTransactions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTransactions.add(TableInfo.Index("index_transactions_userId", false,
            listOf("userId"), listOf("ASC")))
        _indicesTransactions.add(TableInfo.Index("index_transactions_accountId", false,
            listOf("accountId"), listOf("ASC")))
        val _infoTransactions: TableInfo = TableInfo("transactions", _columnsTransactions,
            _foreignKeysTransactions, _indicesTransactions)
        val _existingTransactions: TableInfo = read(connection, "transactions")
        if (!_infoTransactions.equals(_existingTransactions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transactions(com.example.expense_tracker.data.local.entity.TransactionEntity).
              | Expected:
              |""".trimMargin() + _infoTransactions + """
              |
              | Found:
              |""".trimMargin() + _existingTransactions)
        }
        val _columnsBudgets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBudgets.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("categoryId", TableInfo.Column("categoryId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("limitAmount", TableInfo.Column("limitAmount", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("spent", TableInfo.Column("spent", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("month", TableInfo.Column("month", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgets.put("year", TableInfo.Column("year", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBudgets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysBudgets.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION",
            listOf("userId"), listOf("id")))
        val _indicesBudgets: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBudgets.add(TableInfo.Index("index_budgets_userId", false, listOf("userId"),
            listOf("ASC")))
        val _infoBudgets: TableInfo = TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets,
            _indicesBudgets)
        val _existingBudgets: TableInfo = read(connection, "budgets")
        if (!_infoBudgets.equals(_existingBudgets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |budgets(com.example.expense_tracker.data.local.entity.BudgetEntity).
              | Expected:
              |""".trimMargin() + _infoBudgets + """
              |
              | Found:
              |""".trimMargin() + _existingBudgets)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "accounts",
        "transactions", "budgets")
  }

  public override fun clearAllTables() {
    super.performClear(true, "users", "accounts", "transactions", "budgets")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AccountDao::class, AccountDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TransactionDao::class, TransactionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(BudgetDao::class, BudgetDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun userDao(): UserDao = _userDao.value

  public override fun accountDao(): AccountDao = _accountDao.value

  public override fun transactionDao(): TransactionDao = _transactionDao.value

  public override fun budgetDao(): BudgetDao = _budgetDao.value
}
