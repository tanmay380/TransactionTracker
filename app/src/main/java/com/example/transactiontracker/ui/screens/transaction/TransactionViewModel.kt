package com.example.transactiontracker.ui.screens.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transactiontracker.data.local.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeTransaction()
    }

    private fun observeTransaction() {
        transactionRepository.getAllTransaction()
            .map { transaction ->
                if (transaction.isEmpty())
                    TransactionUiState(isLoading = false)
                else
                    transaction.toTransactionUiState()
            }
            .onStart { emit(TransactionUiState(isLoading =  false)) }
            .catch {
                TransactionUiState(isLoading = false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TransactionUiState(isLoading = true)
            )
            .also {
                viewModelScope.launch {
                    it.collect { _uiState.value = it }
                }
            }

    }
}