package com.example.transactiontracker.ui.screens.cardhistory

import com.example.transactiontracker.data.local.TransactionEntity
import java.util.Calendar
import java.util.Locale

fun List<TransactionEntity>.toCardHistoryMapper(): CardHistoryUiState {

    val sorted = sortedByDescending {
        it.date
    }

    val grouped = sorted.groupBy {
        val cal = Calendar.getInstance().apply {
            timeInMillis = it.date
        }
        val month = cal.getDisplayName(Calendar.MONTH,
            Calendar.LONG,  Locale.getDefault())
        val year = cal.get(Calendar.YEAR)
        "$month $year"
    }.mapValues {
        it.value.map {it ->
            CardTransactionUi(
                merchant = it.merchant,
                amount = it.amount,
                date = it.date
            )

        }
    }



    return CardHistoryUiState(
        grouped,
        isLoading = false
    )

}