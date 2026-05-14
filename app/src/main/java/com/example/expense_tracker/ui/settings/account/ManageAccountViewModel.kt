package com.example.expense_tracker.ui.settings.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.AccountType
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.launch

class ManageAccountViewModel(
    private val accountRepository: AccountRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val userId: Long = sharedPrefsHelper.getUserId()

    val accounts: LiveData<List<AccountEntity>> =
        accountRepository.getAccountsByUser(userId).asLiveData()

    private val _saveResult = MutableLiveData<SaveResult>()
    val saveResult: LiveData<SaveResult> = _saveResult

    fun saveAccount(name: String, lastFour: String, type: AccountType) {
        val trimmedName = name.trim()
        val trimmedFour = lastFour.trim()

        when {
            trimmedName.isEmpty() ->
                _saveResult.value = SaveResult.Error("Please enter an account name")

            trimmedFour.length != 4 || !trimmedFour.all { it.isDigit() } ->
                _saveResult.value = SaveResult.Error("Last 4 digits must be exactly 4 numbers")

            else -> viewModelScope.launch {
                accountRepository.insertAccount(
                    AccountEntity(
                        userId = userId,
                        name = trimmedName,
                        lastFourDigits = trimmedFour,
                        type = type,
                        balance = 0.0,
                        isActive = true
                    )
                )
                _saveResult.postValue(SaveResult.Success)
            }
        }
    }

    fun updateAccount(account: AccountEntity, newName: String, newLastFour: String, newType: AccountType) {
        val trimmedName = newName.trim()
        val trimmedFour = newLastFour.trim()

        when {
            trimmedName.isEmpty() ->
                _saveResult.value = SaveResult.Error("Please enter an account name")

            trimmedFour.length != 4 || !trimmedFour.all { it.isDigit() } ->
                _saveResult.value = SaveResult.Error("Last 4 digits must be exactly 4 numbers")

            else -> viewModelScope.launch {
                val updatedAccount = account.copy(
                    name = trimmedName,
                    lastFourDigits = trimmedFour,
                    type = newType
                )
                accountRepository.updateAccount(updatedAccount)
                _saveResult.postValue(SaveResult.Success)
            }
        }
    }

    fun deleteAccount(account: AccountEntity) {
        viewModelScope.launch {
            accountRepository.deactivateAccount(account.id)
            _saveResult.postValue(SaveResult.Success)
        }
    }

    sealed class SaveResult {
        object Success : SaveResult()
        data class Error(val message: String) : SaveResult()
    }
}