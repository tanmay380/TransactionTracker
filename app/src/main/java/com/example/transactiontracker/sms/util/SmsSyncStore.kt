package com.example.transactiontracker.sms.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import androidx.core.content.edit

class SmsSyncStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs by lazy {
        context.getSharedPreferences(
            "sms_sync_prefs",
            Context.MODE_PRIVATE
        )
    }

    fun getLastProcessedTime(): Long {
        return prefs.getLong(KEY_LAST_PROCESSED_TIME, 0L)
    }

    fun saveLastProcessedTime(time: Long) {
        prefs.edit {
            putLong(KEY_LAST_PROCESSED_TIME, time)
        }
    }

    companion object {
        private const val KEY_LAST_PROCESSED_TIME = "last_processed_time"
    }
}
