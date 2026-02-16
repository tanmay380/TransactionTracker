package com.example.transactiontracker.ui.screens.cardhistory

import com.example.transactiontracker.sms.model.CashBackCategory
import com.example.transactiontracker.sms.model.TransactionType

data class CardHistoryUiState (
    val groupedTransaction: Map<String, List<CardTransactionUi>> = emptyMap(),
    val isLoading: Boolean = true
)

data class CardTransactionUi(
    val id : Int,
    val merchant: String,
    val amount: Int,
    val date: Long,
    val type : TransactionType,
    val sms: String,
    val cashBackCategory : CashBackCategory,
    val cashback: Int = 0
)