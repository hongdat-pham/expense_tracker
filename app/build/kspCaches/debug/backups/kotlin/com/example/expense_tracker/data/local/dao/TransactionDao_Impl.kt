package com.example.expense_tracker.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.expense_tracker.`data`.local.Converters
import com.example.expense_tracker.`data`.local.entity.TransactionEntity
import com.example.expense_tracker.`data`.local.entity.TransactionType
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TransactionDao_Impl(
  __db: RoomDatabase,
) : TransactionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTransactionEntity: EntityInsertAdapter<TransactionEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfTransactionEntity = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `transactions` (`id`,`userId`,`accountId`,`categoryId`,`amount`,`type`,`date`,`description`,`receiptPath`,`isRecurring`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindLong(3, entity.accountId)
        statement.bindText(4, entity.categoryId)
        statement.bindDouble(5, entity.amount)
        val _tmp: String = __converters.fromTransactionType(entity.type)
        statement.bindText(6, _tmp)
        statement.bindLong(7, entity.date)
        statement.bindText(8, entity.description)
        val _tmpReceiptPath: String? = entity.receiptPath
        if (_tmpReceiptPath == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpReceiptPath)
        }
        val _tmp_1: Int = if (entity.isRecurring) 1 else 0
        statement.bindLong(10, _tmp_1.toLong())
        statement.bindLong(11, entity.createdAt)
      }
    }
  }

  public override suspend fun insertTransaction(transaction: TransactionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfTransactionEntity.insertAndReturnId(_connection,
        transaction)
    _result
  }

  public override fun getAllTransactionsByUser(userId: Long): Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions WHERE userId = ? ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecentTransactions(userId: Long, limit: Int):
      Flow<List<TransactionEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM transactions
        |        WHERE userId = ?
        |        ORDER BY date DESC
        |        LIMIT ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTransactionsByUserAndMonth(
    userId: Long,
    month: Int,
    year: Int,
  ): Flow<List<TransactionEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM transactions
        |        WHERE userId = ?
        |          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', ?)
        |          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', ?)
        |        ORDER BY date DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
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
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun searchTransactions(userId: Long, query: String):
      Flow<List<TransactionEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM transactions
        |        WHERE userId = ?
        |          AND description LIKE '%' || ? || '%'
        |        ORDER BY date DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTransactionsByType(userId: Long, type: TransactionType):
      Flow<List<TransactionEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM transactions
        |        WHERE userId = ? AND type = ?
        |        ORDER BY date DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        val _tmp: String = __converters.fromTransactionType(type)
        _stmt.bindText(_argIndex, _tmp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp_1)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecurringTransactions(userId: Long): Flow<List<TransactionEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM transactions
        |        WHERE userId = ? AND isRecurring = 1
        |        ORDER BY date DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions WHERE accountId = ? ORDER BY date DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, accountId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTransactionsByUserAndMonthOnce(
    userId: Long,
    month: Int,
    year: Int,
  ): List<TransactionEntity> {
    val _sql: String = """
        |
        |        SELECT * FROM transactions
        |        WHERE userId = ?
        |          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', ?)
        |          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', ?)
        |        ORDER BY date DESC
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
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
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalSpentByCategory(
    userId: Long,
    categoryId: String,
    month: Int,
    year: Int,
  ): Double {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        |        WHERE userId = ?
        |          AND categoryId = ?
        |          AND type = 'EXPENSE'
        |          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', ?)
        |          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', ?)
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
        val _result: Double
        if (_stmt.step()) {
          val _tmp: Double
          _tmp = _stmt.getDouble(0)
          _result = _tmp
        } else {
          _result = 0.0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalExpenseForMonth(
    userId: Long,
    month: Int,
    year: Int,
  ): Double {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        |        WHERE userId = ?
        |          AND type = 'EXPENSE'
        |          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', ?)
        |          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', ?)
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, year.toLong())
        val _result: Double
        if (_stmt.step()) {
          val _tmp: Double
          _tmp = _stmt.getDouble(0)
          _result = _tmp
        } else {
          _result = 0.0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalIncomeForMonth(
    userId: Long,
    month: Int,
    year: Int,
  ): Double {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(amount), 0.0) FROM transactions
        |        WHERE userId = ?
        |          AND type = 'INCOME'
        |          AND strftime('%m', date / 1000, 'unixepoch') = printf('%02d', ?)
        |          AND strftime('%Y', date / 1000, 'unixepoch') = printf('%04d', ?)
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, year.toLong())
        val _result: Double
        if (_stmt.step()) {
          val _tmp: Double
          _tmp = _stmt.getDouble(0)
          _result = _tmp
        } else {
          _result = 0.0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTransactionById(transactionId: Long): TransactionEntity? {
    val _sql: String = "SELECT * FROM transactions WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, transactionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfReceiptPath: Int = getColumnIndexOrThrow(_stmt, "receiptPath")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: TransactionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpCategoryId: String
          _tmpCategoryId = _stmt.getText(_columnIndexOfCategoryId)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpType: TransactionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toTransactionType(_tmp)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpReceiptPath: String?
          if (_stmt.isNull(_columnIndexOfReceiptPath)) {
            _tmpReceiptPath = null
          } else {
            _tmpReceiptPath = _stmt.getText(_columnIndexOfReceiptPath)
          }
          val _tmpIsRecurring: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result =
              TransactionEntity(_tmpId,_tmpUserId,_tmpAccountId,_tmpCategoryId,_tmpAmount,_tmpType,_tmpDate,_tmpDescription,_tmpReceiptPath,_tmpIsRecurring,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
