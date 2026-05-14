package com.example.expense_tracker.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracker.data.repository.UserRepository
import com.example.expense_tracker.utils.SharedPrefsHelper
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _biometricEnabled = MutableLiveData<Boolean>()
    val biometricEnabled: LiveData<Boolean> = _biometricEnabled

    private val _twoFactorEnabled = MutableLiveData<Boolean>()
    val twoFactorEnabled: LiveData<Boolean> = _twoFactorEnabled

    private val _spendingAlertsEnabled = MutableLiveData<Boolean>()
    val spendingAlertsEnabled: LiveData<Boolean> = _spendingAlertsEnabled

    private val _weeklyReportEnabled = MutableLiveData<Boolean>()
    val weeklyReportEnabled: LiveData<Boolean> = _weeklyReportEnabled

    init {
        loadUserInfo()
        loadToggles()
    }

    private fun loadUserInfo() {
        _userName.value = sharedPrefsHelper.getUserFullName() ?: "User"
        _userEmail.value = sharedPrefsHelper.getUserEmail() ?: ""

        val userId = sharedPrefsHelper.getUserId()
        if (userId != -1L) {
            viewModelScope.launch {
                // SỬA: dùng getUserById
                val user = userRepository.getUserById(userId)
                user?.let {
                    _userName.postValue(it.fullName)
                    _userEmail.postValue(it.email)
                }
            }
        }
    }

    private fun loadToggles() {
        _biometricEnabled.value = sharedPrefsHelper.getBiometricEnabled()
        _twoFactorEnabled.value = sharedPrefsHelper.getTwoFactorEnabled()
        _spendingAlertsEnabled.value = sharedPrefsHelper.getSpendingAlertsEnabled()
        _weeklyReportEnabled.value = sharedPrefsHelper.getWeeklyReportEnabled()
    }

    fun setBiometric(enabled: Boolean) {
        sharedPrefsHelper.setBiometricEnabled(enabled)
        _biometricEnabled.value = enabled
    }

    fun setTwoFactor(enabled: Boolean) {
        sharedPrefsHelper.setTwoFactorEnabled(enabled)
        _twoFactorEnabled.value = enabled
    }

    fun setSpendingAlerts(enabled: Boolean) {
        sharedPrefsHelper.setSpendingAlertsEnabled(enabled)
        _spendingAlertsEnabled.value = enabled
    }

    fun setWeeklyReport(enabled: Boolean) {
        sharedPrefsHelper.setWeeklyReportEnabled(enabled)
        _weeklyReportEnabled.value = enabled
    }

    fun logout() {
        sharedPrefsHelper.clearUser()
    }
}