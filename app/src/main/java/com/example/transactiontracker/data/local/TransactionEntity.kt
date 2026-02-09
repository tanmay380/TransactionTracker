package com.example.transactiontracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val merchant: String,
    val amount: Int,
    val cardNo: String,
    val date: Long,
    val bankName: String
) {
    fun formatDate(date: Long): Long {
        val sdf = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        return sdf.format(date).toLong()
    }
}
