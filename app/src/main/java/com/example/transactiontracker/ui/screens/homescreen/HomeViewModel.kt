package com.example.transactiontracker.ui.screens.homescreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.transactiontracker.AppPreferences
import com.example.transactiontracker.data.local.TransactionRepository
import com.example.transactiontracker.sms.permissions.PermissionChecker
import com.example.transactiontracker.sms.reader.SmsInboxReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val transactionRepository: TransactionRepository,
    private val smsInboxReader: SmsInboxReader,
    private val permissionChecker: PermissionChecker // inject this
) : ViewModel() {

    val TAG = "tanmay"

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val _isSyncing = MutableStateFlow(false)

    init {
        Log.d(TAG, "observeHomeData: ${_isSyncing.value}  ${_uiState.value.isLoading} ")

        checkPermissionsOnLaunch()
    }

    private fun checkPermissionsOnLaunch() {
        val granted = permissionChecker.areAllGranted()
        _uiState.update { it.copy(hasPermission = granted) }
        Log.d(TAG, "checkPermissionsOnLaunch: $granted")
        if (granted) {
            observeHomeData()
        }
        // if not granted, we wait — observeHomeData() called after button click
    }

    fun checkPermissionsOnResume() {
        val granted = permissionChecker.areAllGranted()
        if (granted && !_uiState.value.hasPermission) {
            _uiState.update { it.copy(hasPermission = true) }
            observeHomeData()
        }
    }

    private fun observeHomeData() {
        viewModelScope.launch {
            combine(
                transactionRepository.getCurrentMonthTransaction(),
                _isSyncing
            ) { list, syncing ->
                Log.d(TAG, "observeHomeData: $syncing  $_isSyncing  ${list.size}")
                when {
                    syncing -> _uiState.value.copy(isLoading = true)  // ← preserve hasPermission
                    list.isEmpty() -> _uiState.value.copy(
                        isLoading = false,
                        cardWiseTotal = emptyList()
                    )

                    else -> list.toHomeUiState()
                        .copy(hasPermission = true) // ← preserve hasPermission
                }
            }.collect {
                Log.d(TAG, "uiState updated → $it")
                _uiState.value = it
            }
        }
    }

    fun onSmsPermissionGranted() {
        observeHomeData()
        viewModelScope.launch { // ← set BEFORE syncing
            _uiState.update { it.copy(hasPermission = true) }
            _isSyncing.value = true
            withContext(Dispatchers.IO) {
                smsInboxReader.readAndStore()
            }
            AppPreferences.setFirstSyncDone(application)
            _isSyncing.value = false
            Log.d(TAG, "onSmsPermissionGranted: ${_isSyncing.value}")
            // no need to set hasPermission again, combine preserves it now
        }
    }

}