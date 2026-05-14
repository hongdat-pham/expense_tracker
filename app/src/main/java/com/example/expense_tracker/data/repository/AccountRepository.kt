package com.example.expense_tracker.data.repository

import com.example.expense_tracker.data.local.dao.AccountDao
import com.example.expense_tracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val accountDao: AccountDao
) {
    fun getAccountsByUser(userId: Long): Flow<List<AccountEntity>> =
        accountDao.getAccountsByUser(userId)

    suspend fun getAccountById(accountId: Long): AccountEntity =
        accountDao.getById(accountId)

    suspend fun insertAccount(account: AccountEntity): Long =
        accountDao.insertAccount(account)

    suspend fun updateAccount(account: AccountEntity) {
        // Cần thêm method update trong AccountDao
        accountDao.updateAccount(account)
    }

    suspend fun updateBalance(accountId: Long, newBalance: Double) =
        accountDao.updateBalance(accountId, newBalance)

    suspend fun deactivateAccount(accountId: Long) =
        accountDao.deactivateAccount(accountId)
}