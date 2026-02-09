package com.example.transactiontracker.ui.screens.transaction

import com.example.transactiontracker.data.local.TransactionEntity

fun List<TransactionEntity>.toTransactionUiState(): TransactionUiState {
    val uiList = map {
        TransactionUi(
            it.merchant,
            it.amount,
            it.formatDate(it.date)
        )
    }
    return TransactionUiState(
        uiList,
        false
    )
}
