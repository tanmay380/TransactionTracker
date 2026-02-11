package com.example.transactiontracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("Select * from `transactions` ORDER BY date DESC")
    fun getAllTransaction(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Query("Select * from transactions where date between :start and :end")
    fun getTransactionBetween(
        start: Long,
        end: Long
    ): Flow<List<TransactionEntity>>

    @Query("Select * from transactions where cardNo = :cardNo ORDER BY date DESC")
    fun getCardTransaction(cardNo: String): Flow<List<TransactionEntity>>

    @Query("Delete From transactions where id = :id")
    suspend fun deleteEntryForThisCard(id: Int)

}

