package com.example.transactiontracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.transactiontracker.sms.model.CashBackCategory
import com.example.transactiontracker.sms.model.ParsedTransaction
import com.example.transactiontracker.sms.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val merchant: String,
    val amount: Int,
    val cardNo: String,
    val date: Long,
    val bankName: String,
    val type: TransactionType,
    val sms: String,
    val cashBackCategory: CashBackCategory = CashBackCategory.NA,
    val cashBack: Int? = 0
) {
    fun formatDate(date: Long): Long {
        val sdf = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        return sdf.format(date).toLong()
    }
    fun removeCashback() : TransactionEntity{
        return this.copy(cashBack = 0, cashBackCategory = CashBackCategory.NA)
    }
}
