package com.example.transactiontracker.ui.screens.transaction

data class TransactionUiState (
    val transaction : List<TransactionUi> = emptyList(),
    val isLoading : Boolean = true
)

data class TransactionUi(
    val merchant : String = "",
    val amount : Int = 0,
    val date : Long = 0
)