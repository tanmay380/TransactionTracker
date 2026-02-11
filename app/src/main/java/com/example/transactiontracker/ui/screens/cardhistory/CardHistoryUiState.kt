package com.example.transactiontracker.ui.screens.cardhistory

data class CardHistoryUiState (
    val groupedTransaction: Map<String, List<CardTransactionUi>> = emptyMap(),
    val isLoading: Boolean = true
)

data class CardTransactionUi(
    val merchant: String,
    val amount: Int,
    val date: Long
)