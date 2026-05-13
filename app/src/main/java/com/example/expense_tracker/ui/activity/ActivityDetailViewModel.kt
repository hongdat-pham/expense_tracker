package com.example.expense_tracker.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.expense_tracker.utils.DateUtils  // THÊM IMPORT NÀY

class ActivityDetailViewModel(
    private val transactionId: Long,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transaction = MutableStateFlow<TransactionEntity?>(null)
    val transaction: StateFlow<TransactionEntity?> = _transaction.asStateFlow()

    // accountName sẽ tích hợp sau khi Phúc push AccountRepository
    private val _accountName = MutableStateFlow<String?>(null)
    val accountName: StateFlow<String?> = _accountName.asStateFlow()

    init {
        loadTransaction()
    }

    private fun loadTransaction() {
        viewModelScope.launch {
            _transaction.value = transactionRepository.getById(transactionId)
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            _transaction.value?.let { transactionRepository.deleteTransaction(it) }
        }
    }
}