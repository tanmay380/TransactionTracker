package com.example.transactiontracker.ui.screens.homescreen

data class HomeUiState(
    val totalMonthlyExpense: Int = 0,
    val cardWiseTotal: List<CardWiseTotal> = emptyList(),
    val isLoading: Boolean = true
)

data class CardWiseTotal (
    val cardNumber : String = "",
    val totalExpense : Double = 0.0,
    val bankName: String =""
)
