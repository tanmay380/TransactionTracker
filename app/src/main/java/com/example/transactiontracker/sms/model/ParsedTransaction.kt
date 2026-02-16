package com.example.transactiontracker.sms.model

data class ParsedTransaction(
    val merchant: String,
    val amount: Double?,
    val cardNo: String,
    val date: Long,
    val type: TransactionType,
    val bankName : String,
    val sms: String,
    val cashback : Int = 0,
    val cashBackCategory: CashBackCategory = CashBackCategory.NA,
){

    fun toLong(): String{
        return "$merchant $amount $cardNo $date"
    }
}

enum class CashBackCategory{
    PHONE_PE,
    UPI,
    ONLINE,
    NA
}

enum class TransactionType{
    DEBIT,
    CREDIT,
    REFUND;

    fun applySign(amount: Double) : Double{
        return when(this){
            TransactionType.REFUND -> -amount
            else -> amount
        }
    }
}