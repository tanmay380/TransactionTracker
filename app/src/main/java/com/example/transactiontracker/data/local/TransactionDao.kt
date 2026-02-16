package com.example.transactiontracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.transactiontracker.sms.model.ParsedTransaction
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


    @Query("delete from transactions")
    suspend fun deleteAll()

    @Update
    suspend fun update(toParsedTransaction: ParsedTransaction)

    @Query("select * from transactions where id = :id")
    fun getTransactionDetailsById(id: Int): TransactionEntity

    /*@Query("SELECT * FROM transactions " +
            "WHERE strftime('%Y-%m', date / 1000, 'unixepoch') = :month " +
            "ORDER BY     CASE    " +
            "     WHEN type = 'DEBIT' " +
            "THEN 0      " +
            "  ELSE 1   " +
            " END, " +
            "   date DESC;")
    suspend fun getTransactionsForCurrentMonth(): List<TransactionEntity>*/

}

