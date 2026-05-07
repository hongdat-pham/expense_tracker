package com.example.expense_tracker.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.ExpenseTrackerApp
import com.example.expense_tracker.data.local.entity.UserEntity
import kotlinx.coroutines.launch
import java.security.MessageDigest

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val db = (application as ExpenseTrackerApp).database
    private val prefs = (application as ExpenseTrackerApp).prefsHelper

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> = _authResult

    // Email sau khi đăng ký — dùng để tự điền vào LoginFragment
    private val _registeredEmail = MutableLiveData<String>()
    val registeredEmail: LiveData<String> = _registeredEmail

    fun register(fullName: String, email: String, password: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _authResult.value = AuthResult.Error("Please fill in all fields")
            return
        }
        if (password.length < 6) {
            _authResult.value = AuthResult.Error("Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            try {
                val existing = db.userDao().getUserByEmail(email)
                if (existing != null) {
                    _authResult.value = AuthResult.Error("Email already registered")
                    return@launch
                }

                val user = UserEntity(
                    fullName = fullName,
                    email = email,
                    passwordHash = hashPassword(password)
                )
                db.userDao().insertUser(user)
                _registeredEmail.value = email
                _authResult.value = AuthResult.Success

            } catch (e: Exception) {
                _authResult.value = AuthResult.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authResult.value = AuthResult.Error("Please fill in all fields")
            return
        }

        viewModelScope.launch {
            try {
                val user = db.userDao().getUserByEmail(email)
                if (user == null || user.passwordHash != hashPassword(password)) {
                    _authResult.value = AuthResult.Error("Invalid email or password")
                    return@launch
                }

                prefs.saveSession(user.id, user.email, user.fullName)
                _authResult.value = AuthResult.Success

            } catch (e: Exception) {
                _authResult.value = AuthResult.Error("Login failed: ${e.message}")
            }
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}