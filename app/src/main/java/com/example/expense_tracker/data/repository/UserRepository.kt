package com.example.expense_tracker.data.repository

import com.example.expense_tracker.data.local.dao.UserDao
import com.example.expense_tracker.data.local.entity.UserEntity
import com.example.expense_tracker.utils.SharedPrefsHelper

class UserRepository(
    private val userDao: UserDao,
    private val sharedPrefsHelper: SharedPrefsHelper
) {

    suspend fun getCurrentUser(): UserEntity? {
        val userId = sharedPrefsHelper.getUserId()
        if (userId == -1L) return null
        return userDao.getUserById(userId)
    }

    // THÊM method này
    suspend fun getUserById(userId: Long): UserEntity? {
        return userDao.getUserById(userId)
    }

    fun logout() {
        sharedPrefsHelper.clearUser()
    }
}