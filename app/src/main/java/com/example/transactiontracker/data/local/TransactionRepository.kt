package com.example.transactiontracker.data.local

import com.example.transactiontracker.sms.model.ParsedTransaction
import com.example.transactiontracker.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {

    fun getAllTransaction(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransaction()
    }

    suspend fun insertTransaction(parsed: ParsedTransaction) {
        transactionDao.insertTransaction(
            TransactionEntity(
                merchant = parsed.merchant,
                amount = parsed.amount.toInt(),
                cardNo = parsed.cardNo,
                date = parsed.date,
                bankName = parsed.bankName
            )
        )
    }

    fun getCurrentMonthTransaction() : Flow<List<TransactionEntity>>{
        val start = DateUtils.startOfCurrentMonth()
        val end = DateUtils.endOfCurrentMonth()
        return transactionDao.getTransactionBetween(start, end)

    }
    suspend fun insertTransactionIfNew(parsed: ParsedTransaction): Boolean {
        return try {
            transactionDao.insertTransaction(
                TransactionEntity(
                    amount = parsed.amount.toInt(),
                    merchant = parsed.merchant,
                    cardNo = parsed.cardNo,
                    date = parsed.date,
                    bankName = parsed.bankName
                )
            )
            true
        } catch (e: Exception) {
            false // duplicate ignored
        }
    }


}