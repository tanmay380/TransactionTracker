package com.example.transactiontracker.sms.model

data class ParsedTransaction(
    val merchant: String,
    val amount: Double,
    val cardNo: String,
    val date: Long,
    val bankName: String
){
    fun toLong(): String{
        return "$merchant $amount $cardNo $date $bankName"
    }
}