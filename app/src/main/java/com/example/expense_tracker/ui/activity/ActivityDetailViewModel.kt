package com.example.expense_tracker.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.local.entity.TransactionEntity
import com.example.expense_tracker.data.repository.AccountRepository
import com.example.expense_tracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityDetailViewModel(
    private val transactionId: Long,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _transaction = MutableStateFlow<TransactionEntity?>(null)
    val transaction: StateFlow<TransactionEntity?> = _transaction.asStateFlow()

    private val _accountName = MutableStateFlow<String?>(null)
    val accountName: StateFlow<String?> = _accountName.asStateFlow()

    init {
        loadTransaction()
    }

    private fun loadTransaction() {
        viewModelScope.launch {
            val tx = transactionRepository.getById(transactionId)
            _transaction.value = tx

            // Lấy tên account
            tx?.let {
                if (it.accountId == -1L) {
                    _accountName.value = "Cash"
                } else {
                    val account = accountRepository.getAccountById(it.accountId)
                    _accountName.value = "${account.name} ••••${account.lastFourDigits}"
                }
            }
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            _transaction.value?.let {
                transactionRepository.deleteTransaction(it)
            }
        }
    }
}