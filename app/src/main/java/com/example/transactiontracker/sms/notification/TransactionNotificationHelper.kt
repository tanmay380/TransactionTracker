package com.example.transactiontracker.sms.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.transactiontracker.MainActivity
import com.example.transactiontracker.R
import com.example.transactiontracker.sms.model.ParsedTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Runnable


class TransactionNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }


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

        val pendingIntent : PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("Transaction detected")
            .setContentText("₹${txn.amount} spent on ${txn.merchant}")
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
