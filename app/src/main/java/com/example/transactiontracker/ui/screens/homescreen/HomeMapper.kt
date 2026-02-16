package com.example.transactiontracker.ui.screens.homescreen

import com.example.transactiontracker.data.local.TransactionEntity
import com.example.transactiontracker.sms.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun List<TransactionEntity>.toHomeUiState() : HomeUiState{
    val total = sumOf {
        if (it.type != TransactionType.CREDIT) it.amount
        else 0
    }

    val cardWise = groupBy { it.cardNo }
        .map {(card, transactions) ->
            CardWiseTotal(
                card,
                transactions.sumOf {
                    if (it.type != TransactionType.CREDIT) it.amount
                    else 0
                }.toDouble(),
                transactions.first().bankName
            )
        }
    return HomeUiState(
        total,
        cardWise,
        false
    )
}