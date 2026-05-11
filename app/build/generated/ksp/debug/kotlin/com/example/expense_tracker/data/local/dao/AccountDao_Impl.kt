package com.example.expense_tracker.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.expense_tracker.`data`.local.Converters
import com.example.expense_tracker.`data`.local.entity.AccountEntity
import com.example.expense_tracker.`data`.local.entity.AccountType
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
public class AccountDao_Impl(
  __db: RoomDatabase,
) : AccountDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAccountEntity: EntityInsertAdapter<AccountEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfAccountEntity = object : EntityInsertAdapter<AccountEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `accounts` (`id`,`userId`,`name`,`lastFourDigits`,`type`,`balance`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AccountEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.lastFourDigits)
        val _tmp: String = __converters.fromAccountType(entity.type)
        statement.bindText(5, _tmp)
        statement.bindDouble(6, entity.balance)
        val _tmp_1: Int = if (entity.isActive) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
      }
    }
  }

  public override suspend fun insertAccount(account: AccountEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfAccountEntity.insertAndReturnId(_connection, account)
    _result
  }

  public override fun getAccountsByUser(userId: Long): Flow<List<AccountEntity>> {
    val _sql: String = "SELECT * FROM accounts WHERE userId = ? AND isActive = 1 ORDER BY id ASC"
    return createFlow(__db, false, arrayOf("accounts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLastFourDigits: Int = getColumnIndexOrThrow(_stmt, "lastFourDigits")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfBalance: Int = getColumnIndexOrThrow(_stmt, "balance")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: MutableList<AccountEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AccountEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLastFourDigits: String
          _tmpLastFourDigits = _stmt.getText(_columnIndexOfLastFourDigits)
          val _tmpType: AccountType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toAccountType(_tmp)
          val _tmpBalance: Double
          _tmpBalance = _stmt.getDouble(_columnIndexOfBalance)
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _item =
              AccountEntity(_tmpId,_tmpUserId,_tmpName,_tmpLastFourDigits,_tmpType,_tmpBalance,_tmpIsActive)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(accountId: Long): AccountEntity {
    val _sql: String = "SELECT * FROM accounts WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, accountId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLastFourDigits: Int = getColumnIndexOrThrow(_stmt, "lastFourDigits")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfBalance: Int = getColumnIndexOrThrow(_stmt, "balance")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: AccountEntity
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLastFourDigits: String
          _tmpLastFourDigits = _stmt.getText(_columnIndexOfLastFourDigits)
          val _tmpType: AccountType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toAccountType(_tmp)
          val _tmpBalance: Double
          _tmpBalance = _stmt.getDouble(_columnIndexOfBalance)
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _result =
              AccountEntity(_tmpId,_tmpUserId,_tmpName,_tmpLastFourDigits,_tmpType,_tmpBalance,_tmpIsActive)
        } else {
          error("The query result was empty, but expected a single row to return a NON-NULL object of type <com.example.expense_tracker.`data`.local.entity.AccountEntity>.")
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFirstAccount(): AccountEntity? {
    val _sql: String = "SELECT * FROM accounts LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLastFourDigits: Int = getColumnIndexOrThrow(_stmt, "lastFourDigits")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfBalance: Int = getColumnIndexOrThrow(_stmt, "balance")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: AccountEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_columnIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLastFourDigits: String
          _tmpLastFourDigits = _stmt.getText(_columnIndexOfLastFourDigits)
          val _tmpType: AccountType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toAccountType(_tmp)
          val _tmpBalance: Double
          _tmpBalance = _stmt.getDouble(_columnIndexOfBalance)
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _result =
              AccountEntity(_tmpId,_tmpUserId,_tmpName,_tmpLastFourDigits,_tmpType,_tmpBalance,_tmpIsActive)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateBalance(accountId: Long, newBalance: Double) {
    val _sql: String = "UPDATE accounts SET balance = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, newBalance)
        _argIndex = 2
        _stmt.bindLong(_argIndex, accountId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deactivateAccount(accountId: Long) {
    val _sql: String = "UPDATE accounts SET isActive = 0 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, accountId)
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
