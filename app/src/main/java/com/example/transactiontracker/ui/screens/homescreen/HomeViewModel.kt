package com.example.transactiontracker.ui.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transactiontracker.data.local.TransactionRepository
import com.example.transactiontracker.sms.reader.SmsInboxReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val smsInboxReader: SmsInboxReader
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

//    private val smsInboxReader : SmsInboxReader

    init {
        observeHomeData()
    }

    private fun observeHomeData() {
        transactionRepository.getCurrentMonthTransaction()
            .map {
                if (it.isEmpty()){
                    HomeUiState(isLoading = false)
                }else
                    it.toHomeUiState()
            }
            .onStart {
                emit(HomeUiState(isLoading = false))
            }
            .catch {
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
            }

    }

    fun onSmsPermissionGranted() {
        viewModelScope.launch {
            smsInboxReader.readAndStore()
        }

    }


}