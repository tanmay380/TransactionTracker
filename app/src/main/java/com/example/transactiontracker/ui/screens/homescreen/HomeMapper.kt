package com.example.transactiontracker.ui.screens.homescreen

import com.example.transactiontracker.data.local.TransactionEntity
import com.example.transactiontracker.sms.model.TransactionType

fun List<TransactionEntity>.toHomeUiState() : HomeUiState{
    val total = sumOf {
        if (it.type == TransactionType.DEBIT) it.amount
        else -(it.amount)
    }

    val cardWise = groupBy { it.cardNo }
        .map {(card, transactions) ->
            CardWiseTotal(
                card,
                transactions.sumOf {
                    if (it.type == TransactionType.DEBIT) it.amount
                    else -(it.amount)
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