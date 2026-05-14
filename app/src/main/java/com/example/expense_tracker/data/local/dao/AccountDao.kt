package com.example.expense_tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expense_tracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Query("SELECT * FROM accounts WHERE userId = :userId AND isActive = 1 ORDER BY id ASC")
    fun getAccountsByUser(userId: Long): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :accountId LIMIT 1")
    suspend fun getById(accountId: Long): AccountEntity

    @Query("UPDATE accounts SET balance = :newBalance WHERE id = :accountId")
    suspend fun updateBalance(accountId: Long, newBalance: Double)

    @Query("UPDATE accounts SET isActive = 0 WHERE id = :accountId")
    suspend fun deactivateAccount(accountId: Long)

    @Query("SELECT * FROM accounts LIMIT 1")
    suspend fun getFirstAccount(): AccountEntity?

    // THÊM: Cập nhật account
    @Update
    suspend fun updateAccount(account: AccountEntity)
}