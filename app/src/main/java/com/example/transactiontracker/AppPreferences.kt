package com.example.transactiontracker

import android.content.Context
import androidx.core.content.edit

object AppPreferences {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_FIRST_SYNC_DONE = "first_sync_done"

    fun isFirstSyncDone(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_SYNC_DONE, false)
    }

    fun setFirstSyncDone(context: Context, value: Boolean = true) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_FIRST_SYNC_DONE, value) }
    }
}