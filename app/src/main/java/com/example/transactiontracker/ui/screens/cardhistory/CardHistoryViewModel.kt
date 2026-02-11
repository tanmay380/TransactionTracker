package com.example.transactiontracker.ui.screens.cardhistory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transactiontracker.data.local.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardHistoryViewModel @Inject constructor(
    private val repository: TransactionRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val cardId = savedStateHandle.get<String>("cardNumber") ?: ""

    private val _uiState = MutableStateFlow(CardHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeCardHistory()
    }

    private fun observeCardHistory() {
        repository.getCardTransaction(cardId)
            .map {
                if (it.isEmpty())
                    CardHistoryUiState(isLoading = false)
                else
                    it.toCardHistoryMapper()
            }
            .onStart { emit(CardHistoryUiState(isLoading = true)) }
            .stateIn (
                 viewModelScope,
                 SharingStarted.WhileSubscribed(5000),
                 CardHistoryUiState(isLoading = true)
            )
            .also{
                viewModelScope.launch {
                    it.collect{
                        _uiState.value = it
                    }
                }
            }
    }



}