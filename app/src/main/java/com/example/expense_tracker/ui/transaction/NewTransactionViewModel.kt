package com.example.expense_tracker.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.AccountEntity
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.local.entity.TransactionType
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class NewTransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _accounts = MutableLiveData<List<AccountEntity>>(emptyList())
    val accounts: LiveData<List<AccountEntity>> = _accounts

    private val _selectedType = MutableLiveData(TransactionType.EXPENSE)
    val selectedType: LiveData<TransactionType> = _selectedType

    private val _selectedCategoryId = MutableLiveData<String?>(null)
    val selectedCategoryId: LiveData<String?> = _selectedCategoryId

    private val _selectedAccountId = MutableLiveData<Long?>(null)
    val selectedAccountId: LiveData<Long?> = _selectedAccountId

    private val _selectedDateMs = MutableLiveData(System.currentTimeMillis())
    val selectedDateMs: LiveData<Long> = _selectedDateMs

    private val _receiptPath = MutableLiveData<String?>(null)
    val receiptPath: LiveData<String?> = _receiptPath

    private val _isRecurring = MutableLiveData(false)
    val isRecurring: LiveData<Boolean> = _isRecurring

    private val _saveResult = MutableLiveData<Boolean?>(null)
    val saveResult: LiveData<Boolean?> = _saveResult

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadAccounts(userId: Long) {
        viewModelScope.launch {
            accountRepository.getAccountsByUser(userId).collect { list ->
                _accounts.value = list
                if (_selectedAccountId.value == null && list.isNotEmpty()) {
                    _selectedAccountId.value = list[0].id
                }
            }
        }
    }

    fun selectType(type: TransactionType) {
        _selectedType.value = type
        _selectedCategoryId.value = null
    }

    fun selectCategory(categoryId: String) {
        _selectedCategoryId.value = categoryId
    }

    fun selectAccount(accountId: Long) {
        _selectedAccountId.value = accountId
    }

    fun selectDate(timestampMs: Long) {
        _selectedDateMs.value = timestampMs
    }

    fun setReceiptPath(path: String?) {
        _receiptPath.value = path
    }

    fun setRecurring(recurring: Boolean) {
        _isRecurring.value = recurring
    }

    fun saveTransaction(
        userId: Long,
        amountText: String,
        description: String
    ) {
        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            _errorMessage.value = "Please enter a valid amount"
            return
        }
        val categoryId = _selectedCategoryId.value
        if (categoryId == null) {
            _errorMessage.value = "Please select a category"
            return
        }
        val accountId = _selectedAccountId.value
        if (accountId == null) {
            _errorMessage.value = "Please add an account first in Settings"
            return
        }

        viewModelScope.launch {
            try {
                val entity = TransactionEntity(
                    userId = userId,
                    accountId = accountId,
                    categoryId = categoryId,
                    amount = amount,
                    type = _selectedType.value ?: TransactionType.EXPENSE,
                    date = _selectedDateMs.value ?: System.currentTimeMillis(),
                    description = description,
                    receiptPath = _receiptPath.value,
                    isRecurring = _isRecurring.value ?: false
                )
                transactionRepository.saveTransaction(entity)
                _saveResult.value = true
            } catch (e: Exception) {
                _saveResult.value = false
                _errorMessage.value = "Failed to save transaction: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }
}