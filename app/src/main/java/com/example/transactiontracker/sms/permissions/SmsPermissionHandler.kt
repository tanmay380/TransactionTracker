package com.example.transactiontracker.sms.permissions

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsPermissionHandler(
    onPermissionGranted: Boolean,
    hasNotificationPermission: Boolean,
    requestNotificationPermission: () -> Unit,
    requestPermission1: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Button(onClick = {
            if (!onPermissionGranted)
                requestPermission1()
            else if (!hasNotificationPermission)
                requestNotificationPermission()
        }
        ) {
            Text("Allow SMS Access")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberSmsPermissionState(): Pair<Boolean, () -> Unit> {
    val permissionState = rememberPermissionState(
        Manifest.permission.READ_SMS
    )
    val hasPermission = permissionState.status.isGranted

    val requestPermission = {
        permissionState.launchPermissionRequest()
    }

    return Pair(hasPermission, requestPermission)

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberNotificationPermissionState(): Pair<Boolean, () -> Unit> {
    val permissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    val hasPermission = permissionState.status.isGranted

    val requestPermission = {
        permissionState.launchPermissionRequest()
    }

    return Pair(hasPermission, requestPermission)
}
