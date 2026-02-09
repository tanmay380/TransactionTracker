package com.example.transactiontracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CardData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cardNumber: String,
    val bankName: String
)