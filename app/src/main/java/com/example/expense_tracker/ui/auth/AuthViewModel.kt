package com.example.expense_tracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.dao.AccountDao
import com.example.expense_tracker.data.local.dao.UserDao
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.data.local.entity.UserEntity
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AuthViewModel(
    private val userDao: UserDao,
    private val accountDao: AccountDao,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val loginState: StateFlow<AuthResult> = _loginState

    private val _registerState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val registerState: StateFlow<AuthResult> = _registerState

    private val _registeredCredentials = MutableSharedFlow<Credentials>()
    val registeredCredentials: SharedFlow<Credentials> = _registeredCredentials

    fun register(fullName: String, email: String, password: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = AuthResult.Error("All fields are required")
            return
        }
        viewModelScope.launch {
            _registerState.value = AuthResult.Loading
            val existing = userDao.getUserByEmail(email)
            if (existing != null) {
                _registerState.value = AuthResult.Error("An account with this email already exists")
                return@launch
            }
            val hash = sha256(password)
            val newUser = UserEntity(fullName = fullName, email = email, passwordHash = hash)
            val userId = userDao.insertUser(newUser)

            // Tạo Cash account mặc định cho user mới
            val cashAccount = AccountEntity(
                userId = userId,
                name = "Cash",
                lastFourDigits = "CASH",
                type = AccountType.CASH,
                balance = 500.0,
                isActive = true
            )
            accountDao.insertAccount(cashAccount)

            _registeredCredentials.emit(Credentials(email, password))
            _registerState.value = AuthResult.Success
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = AuthResult.Error("Email and password are required")
            return
        }
        viewModelScope.launch {
            _loginState.value = AuthResult.Loading
            val user = userDao.getUserByEmail(email)
            if (user == null || user.passwordHash != sha256(password)) {
                _loginState.value = AuthResult.Error("Invalid email or password")
                return@launch
            }
            sharedPrefsHelper.saveUserId(user.id)
            sharedPrefsHelper.saveUserEmail(user.email)
            sharedPrefsHelper.saveUserFullName(user.fullName)
            _loginState.value = AuthResult.Success
        }
    }

    fun resetLoginState() {
        _loginState.value = AuthResult.Idle
    }

    fun resetRegisterState() {
        _registerState.value = AuthResult.Idle
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    sealed class AuthResult {
        object Idle : AuthResult()
        object Loading : AuthResult()
        object Success : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    data class Credentials(val email: String, val password: String)
}