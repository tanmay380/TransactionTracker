package com.example.transactiontracker.ui.screens.homescreen

import com.example.transactiontracker.data.local.TransactionEntity

fun List<TransactionEntity>.toHomeUiState() : HomeUiState{
    val total = sumOf { it.amount }

    val cardWise = groupBy { it.cardNo }
        .map {(card, transactions) ->
            CardWiseTotal(
                card,
                transactions.sumOf { it.amount }.toDouble(),
                transactions.first().bankName
            )
        }
    return HomeUiState(
        total,
        cardWise,
        false
    )
}