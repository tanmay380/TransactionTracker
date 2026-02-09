package com.example.transactiontracker.sms.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.transactiontracker.R
import com.example.transactiontracker.sms.model.ParsedTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class TransactionNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun show(txn: ParsedTransaction) {
        val channelId = "transactions"

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(
            NotificationChannel(
                channelId,
                "Transactions",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Transaction detected")
            .setContentText("₹${txn.amount} spent")
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
