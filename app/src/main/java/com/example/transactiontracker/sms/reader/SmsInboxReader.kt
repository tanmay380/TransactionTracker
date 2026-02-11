package com.example.transactiontracker.sms.reader

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.example.transactiontracker.AppPreferences
import com.example.transactiontracker.data.local.TransactionRepository
import com.example.transactiontracker.sms.model.ParsedTransaction
import com.example.transactiontracker.sms.parser.HdfcParser
import com.example.transactiontracker.sms.parser.ICICIParser
import com.example.transactiontracker.sms.parser.SBIParser
import com.example.transactiontracker.sms.parser.SmsParser
import com.example.transactiontracker.sms.util.SmsSyncStore
import com.example.transactiontracker.utils.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.Locale.getDefault
import javax.inject.Inject

class SmsInboxReader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val smsParser: SmsParser,
    private val repository: TransactionRepository,
    private val smsSyncStore: SmsSyncStore
) {

    val parsers = listOf(
        HdfcParser(),
        SBIParser(),
        ICICIParser()
    )

    private fun getThreeMonthsAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        return calendar.timeInMillis
    }


    suspend fun readAndStore() {
        if (AppPreferences.isFirstSyncDone(context)) return

        val uri = "content://sms/inbox".toUri()
        val cursor = context
            .contentResolver
            ./*query(uri, arrayOf("address", "body", "date"),
                "date >= ? ",
                arrayOf(getThreeMonthsAgo().toString()),
                "date desc")*/
            query(
                uri, arrayOf("address", "body", "date"),
                null,
                null,
                "date desc"
            )
            ?: return
        cursor.use {
            while (it.moveToNext()) {
                val address = it.getString(it.getColumnIndexOrThrow("address"))
                val smsBody = it.getString(it.getColumnIndexOrThrow("body"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))

                if (!smsBody.lowercase().contains("card")) {
                    continue
                }

                val parser = parsers.firstOrNull { parser ->
                    parser.canHandle(address, smsBody.lowercase())
                }

                val transaction = parser?.parse(smsBody.lowercase(), date)

                if (transaction != null) {
                    repository.insertTransaction(transaction)
                    smsSyncStore.saveLastProcessedTime(System.currentTimeMillis())
                }
            }
        }
    }

    private fun isFromValidAddress(address: String): Boolean {
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

        Log.d("tanmay", "readLatestAndStore: ")

        val inserted = mutableListOf<ParsedTransaction>()
        var newestTimestamp = lastProcessesTime

        cursor.use {
            while (it.moveToNext()) {
                val body = it.getString(it.getColumnIndexOrThrow("body"))
                val date = it.getLong(it.getColumnIndexOrThrow("date"))
                val address = it.getString(it.getColumnIndexOrThrow("address"))


                val parser = parsers.firstOrNull { parser ->
                    parser.canHandle(address, body.lowercase())
                }

                val transaction = parser?.parse(body.lowercase(), date)
                Log.d("tanmay", "readLatestAndStore: " + transaction + "  " +parser)

                if (repository.insertTransactionIfNew(transaction!!)) {
                    inserted.add(transaction)
                }

                if (date > newestTimestamp) {
                    newestTimestamp = date
                }
            }
        }
        if (newestTimestamp > lastProcessesTime) {
            smsSyncStore.saveLastProcessedTime(newestTimestamp)
        }

        return inserted
    }
}