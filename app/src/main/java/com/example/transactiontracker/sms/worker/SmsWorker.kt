package com.example.transactiontracker.sms.worker

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.transactiontracker.sms.notification.TransactionNotificationHelper
import com.example.transactiontracker.sms.reader.SmsInboxReader
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SmsWorker @AssistedInject constructor(
    @Assisted private val context : Context,
    @Assisted params : WorkerParameters,
    private val inboxReader: SmsInboxReader,
    private val notificationHelper: TransactionNotificationHelper
) :
CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
//        Log.d("tanmay", "doWork: ")

        val newTransaction = inboxReader.readLatestAndStore()
        Log.d("tanmay", "doWork: " + newTransaction)

        if (newTransaction.isNotEmpty()){
//            Toast.makeText(context, "New transaction Added", Toast.LENGTH_SHORT).show()
            notificationHelper.show(newTransaction.first())
        }

        return Result.success()
    }
}