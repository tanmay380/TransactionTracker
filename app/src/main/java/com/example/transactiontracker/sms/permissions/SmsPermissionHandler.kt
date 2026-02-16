package com.example.transactiontracker.sms.permissions

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
fun SmsPermissionHandler(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(
        android.Manifest.permission.READ_SMS
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Button(onClick = onPermissionGranted) {
            Text("Allow SMS Access")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberSmsPermissionState(): Pair<Boolean, () -> Unit> {
    val permissionState = rememberPermissionState(
        android.Manifest.permission.READ_SMS
    )
    val hasPermission = permissionState.status.isGranted

    val requestPermission = {
        permissionState.launchPermissionRequest()
    }

    return Pair(hasPermission, requestPermission)

}