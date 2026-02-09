package com.example.transactiontracker.sms.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.transactiontracker.sms.notification.TransactionNotificationHelper
import com.example.transactiontracker.sms.reader.SmsInboxReader
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SmsWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted params : WorkerParameters,
    private val inboxReader: SmsInboxReader,
    private val notificationHelper: TransactionNotificationHelper
) :
CoroutineWorker(context, params){
    override suspend fun doWork(): Result {
        val newTransaction = inboxReader.readLatestAndStore()

        if (newTransaction.isNotEmpty()){
            notificationHelper.show(newTransaction.first())
        }

        return Result.success()
    }
}