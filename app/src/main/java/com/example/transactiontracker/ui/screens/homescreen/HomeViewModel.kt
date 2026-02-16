package com.example.transactiontracker.ui.screens.homescreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transactiontracker.AppPreferences
import com.example.transactiontracker.data.local.TransactionRepository
import com.example.transactiontracker.sms.reader.SmsInboxReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val transactionRepository: TransactionRepository,
    private val smsInboxReader: SmsInboxReader
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val _isSyncing = MutableStateFlow(false)

//    private val smsInboxReader : SmsInboxReader

    init {
        observeHomeData()
    }

    private fun observeHomeData() {
        viewModelScope.launch {

            /*var firstEmission = true
            transactionRepository.getCurrentMonthTransaction()
                *//*.map {
                    Log.d("tanmay", "observeHomeData: " + _uiState.value.isLoading)
//                    Log.d("tanmay", "observeHomeData: " + it.size)
                    if (it.isEmpty()) {
                        Log.d("tanmay", "observeHomeData: is empty" )
                        HomeUiState(isLoading = false)
                    } else
                        it.toHomeUiState()
                }*//*
                .onStart {
                    _uiState.value = (HomeUiState(isLoading = true))
                }
                *//*.catch {
                    emit(HomeUiState(isLoading = false))
                }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeUiState(isLoading = true)
            )
            .also {
                viewModelScope.launch {
                    it.collect { _uiState.value = it }
                }
            }*//*
                .collect {
                        list ->

                    if (firstEmission) {
                        firstEmission = false

                        if (list.isEmpty()) {
                            // Still loading — don't show empty yet
                            return@collect
                        }
                    }

                    _uiState.value =
                        if (list.isEmpty()) {
                            HomeUiState(isLoading = false)
                        } else {
                            list.toHomeUiState()
                        }
                }*/
            combine(
                transactionRepository.getCurrentMonthTransaction(),
                _isSyncing
            ) { list, syncing ->
                when {
                    syncing -> HomeUiState(isLoading = true)
                    list.isEmpty() -> HomeUiState(isLoading = false)
                    else -> list.toHomeUiState()
                }
            }.collect {
                _uiState.value = it
            }
        }

    }

    fun onSmsPermissionGranted() {
        viewModelScope.launch {
            _isSyncing.value = true
            withContext(Dispatchers.IO) {
                smsInboxReader.readAndStore()
            }
            AppPreferences.setFirstSyncDone(application)
            _isSyncing.value = false
        }

    }


}
