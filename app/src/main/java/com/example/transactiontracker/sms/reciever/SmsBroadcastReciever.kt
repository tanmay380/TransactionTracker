package com.example.transactiontracker.sms.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.transactiontracker.sms.worker.SmsWorker

class SmsBroadcastReciever: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        Log.d("tanmay", "onReceive: braodacdt reicev e")
        if (intent?.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val workRequest = OneTimeWorkRequestBuilder<SmsWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}