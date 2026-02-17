package com.example.transactiontracker.sms.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun areAllGranted(): Boolean {
        val permissions = buildList {
            add(Manifest.permission.READ_SMS)
            add(Manifest.permission.RECEIVE_SMS)
            // POST_NOTIFICATIONS only exists on Android 13+
            add(Manifest.permission.POST_NOTIFICATIONS)
        }

        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}