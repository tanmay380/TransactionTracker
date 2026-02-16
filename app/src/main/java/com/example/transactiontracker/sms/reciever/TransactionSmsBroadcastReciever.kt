package com.example.transactiontracker.sms.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.transactiontracker.sms.worker.SmsWorker
import java.util.concurrent.TimeUnit

class TransactionSmsBroadcastReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Log.d("TransactionSmsBroadcastReceiver", "Incorrect intent action: ${intent.action}")
            return
        }
        Log.d("TransactionSmsBroadcastReceiver", "onReceive: SMS received!")
        val workRequest = OneTimeWorkRequestBuilder<SmsWorker>()
            .setInitialDelay(2, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context.applicationContext).enqueue(workRequest)
        Log.d("TransactionSmsBroadcastReceiver", "Work request enqueued for SmsWorker.")
    }
}