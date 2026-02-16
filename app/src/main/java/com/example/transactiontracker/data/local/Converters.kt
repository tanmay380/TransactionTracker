package com.example.transactiontracker.data.local

import androidx.room.TypeConverter
import com.example.transactiontracker.sms.model.CashBackCategory
import com.example.transactiontracker.sms.model.TransactionType

class Converters {

    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? {
        return value?.let { TransactionType.valueOf(it) }
    }

    @TypeConverter
    fun fromCashBackCategory(value : CashBackCategory): String{
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value : String): CashBackCategory{
        return CashBackCategory.valueOf(value)
    }
}
