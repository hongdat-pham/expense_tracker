package com.example.expense_tracker.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.expense_tracker.`data`.local.entity.BudgetEntity
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BudgetDao_Impl(
  __db: RoomDatabase,
) : BudgetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBudgetEntity: EntityInsertAdapter<BudgetEntity>

  private val __updateAdapterOfBudgetEntity: EntityDeleteOrUpdateAdapter<BudgetEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfBudgetEntity = object : EntityInsertAdapter<BudgetEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `budgets` (`id`,`userId`,`categoryId`,`limitAmount`,`spent`,`month`,`year`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.categoryId)
        statement.bindDouble(4, entity.limitAmount)
        statement.bindDouble(5, entity.spent)
        statement.bindLong(6, entity.month.toLong())
        statement.bindLong(7, entity.year.toLong())
      }
    }
    this.__updateAdapterOfBudgetEntity = object : EntityDeleteOrUpdateAdapter<BudgetEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `budgets` SET `id` = ?,`userId` = ?,`categoryId` = ?,`limitAmount` = ?,`spent` = ?,`month` = ?,`year` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.categoryId)
        statement.bindDouble(4, entity.limitAmount)
        statement.bindDouble(5, entity.spent)
        statement.bindLong(6, entity.month.toLong())
        statement.bindLong(7, entity.year.toLong())
        statement.bindLong(8, entity.id)
      }
    }
  }

  public override suspend fun insertOrReplaceBudget(budget: BudgetEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfBudgetEntity.insertAndReturnId(_connection, budget)
    _result
  }

  public override suspend fun updateBudget(budget: BudgetEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfBudgetEntity.handle(_connection, budget)
  }

  public override fun getBudgetsByUser(
    userId: Long,
    month: Int,
    year: Int,
  ): Flow<List<BudgetEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM budgets 
        |        WHERE userId = ? AND month = ? AND year = ?
        |        ORDER BY categoryId ASC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("budgets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, year.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfLimitAmount: Int = getColumnIndexOrThrow(_stmt, "limitAmount")
        val _columnIndexOfSpent: Int = getColumnIndexOrThrow(_stmt, "spent")
        val _columnIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _result: MutableList<BudgetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BudgetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpLimitAmount: Double
          _tmpLimitAmount = _stmt.getDouble(_columnIndexOfLimitAmount)
          val _tmpSpent: Double
          _tmpSpent = _stmt.getDouble(_columnIndexOfSpent)
          val _tmpMonth: Int
          _tmpMonth = _stmt.getLong(_columnIndexOfMonth).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          _item =
              BudgetEntity(_tmpId,_tmpUserId,_tmpCategoryId,_tmpLimitAmount,_tmpSpent,_tmpMonth,_tmpYear)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBudgetByCategory(
    userId: Long,
    categoryId: String,
    month: Int,
    year: Int,
  ): BudgetEntity? {
    val _sql: String = """
        |
        |        SELECT * FROM budgets 
        |        WHERE userId = ? AND categoryId = ? 
        |          AND month = ? AND year = ?
        |        LIMIT 1
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, categoryId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 4
        _stmt.bindLong(_argIndex, year.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfLimitAmount: Int = getColumnIndexOrThrow(_stmt, "limitAmount")
        val _columnIndexOfSpent: Int = getColumnIndexOrThrow(_stmt, "spent")
        val _columnIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _result: BudgetEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpLimitAmount: Double
          _tmpLimitAmount = _stmt.getDouble(_columnIndexOfLimitAmount)
          val _tmpSpent: Double
          _tmpSpent = _stmt.getDouble(_columnIndexOfSpent)
          val _tmpMonth: Int
          _tmpMonth = _stmt.getLong(_columnIndexOfMonth).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          _result =
              BudgetEntity(_tmpId,_tmpUserId,_tmpCategoryId,_tmpLimitAmount,_tmpSpent,_tmpMonth,_tmpYear)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAlertBudgets(
    userId: Long,
    month: Int,
    year: Int,
  ): Flow<List<BudgetEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM budgets
        |        WHERE userId = ? AND month = ? AND year = ?
        |          AND spent > limitAmount * 0.5
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("budgets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, year.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfLimitAmount: Int = getColumnIndexOrThrow(_stmt, "limitAmount")
        val _columnIndexOfSpent: Int = getColumnIndexOrThrow(_stmt, "spent")
        val _columnIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _result: MutableList<BudgetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BudgetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpLimitAmount: Double
          _tmpLimitAmount = _stmt.getDouble(_columnIndexOfLimitAmount)
          val _tmpSpent: Double
          _tmpSpent = _stmt.getDouble(_columnIndexOfSpent)
          val _tmpMonth: Int
          _tmpMonth = _stmt.getLong(_columnIndexOfMonth).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          _item =
              BudgetEntity(_tmpId,_tmpUserId,_tmpCategoryId,_tmpLimitAmount,_tmpSpent,_tmpMonth,_tmpYear)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateSpent(budgetId: Long, spent: Double) {
    val _sql: String = "UPDATE budgets SET spent = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, spent)
        _argIndex = 2
        _stmt.bindLong(_argIndex, budgetId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
