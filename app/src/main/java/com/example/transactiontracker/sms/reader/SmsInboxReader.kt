package com.example.transactiontracker.sms.reader

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.example.transactiontracker.data.local.TransactionRepository
import com.example.transactiontracker.sms.model.ParsedTransaction
import com.example.transactiontracker.sms.parser.SmsParser
import com.example.transactiontracker.sms.util.SmsSyncStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import java.util.Locale.getDefault
import javax.inject.Inject

class SmsInboxReader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val smsParser: SmsParser,
    private val repository: TransactionRepository,
    private val smsSyncStore: SmsSyncStore
) {
    suspend fun readAndStore() {
        val uri = "content://sms/inbox".toUri()
        val cursor = context
            .contentResolver
            .query(uri, arrayOf("address", "body", "date"), null, null, null) ?: return

        cursor.use {
            while (it.moveToNext()) {
                val address = it.getString(it.getColumnIndexOrThrow("address"))
                val smsBody = it.getString(it.getColumnIndexOrThrow("body"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))

                if (isFromValidAddress(address)
                ) {
                    Log.d("tanmay", "readAndStore: is from valid")
                    val parsedTransaction = smsParser.parse(smsBody, date) ?: continue
                    repository.insertTransaction(parsedTransaction)
                }

            }
        }
    }

    private fun isFromValidAddress(address: String): Boolean  {
        Log.d("tanmay", "isFromValidAddress: " + address)
        val upperCaseAddress = address.uppercase(getDefault())
        return upperCaseAddress.contains("ICICI") || upperCaseAddress.contains("HDFCBK")
                || upperCaseAddress.contains("SBICRD")

    }

    suspend fun readLatestAndStore(): List<ParsedTransaction> {
        val lastProcessesTime = smsSyncStore.getLastProcessedTime()
        val uri = "content://sms/inbox".toUri()
        val cursor = context.contentResolver
            .query(
                uri,
                arrayOf("address", "body", "date"),
                "date > ?",
                arrayOf(lastProcessesTime.toString()),
                "date asc"
            ) ?: return emptyList()

        val inserted = mutableListOf<ParsedTransaction>()
        var newestTimestamp = lastProcessesTime

        cursor.use {
            while (it.moveToNext()) {
                val body = it.getString(it.getColumnIndexOrThrow("body"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))
                val address = it.getString(it.getColumnIndexOrThrow("address"))

                if (isFromValidAddress(address)) {
                    val parsed = smsParser.parse(body, date) ?: continue

                    if (repository.insertTransactionIfNew(parsed)) {
                        inserted.add(parsed)
                    }

                    if (date > newestTimestamp) {
                        newestTimestamp = date
                    }
                }
            }
        }
        if (newestTimestamp > lastProcessesTime) {
            smsSyncStore.saveLastProcessedTime(newestTimestamp)
        }

        return inserted
    }
}