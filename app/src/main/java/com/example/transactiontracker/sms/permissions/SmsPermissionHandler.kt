package com.example.transactiontracker.sms.permissions

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlin.math.log

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsPermissionHandler(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(
        android.Manifest.permission.READ_SMS
    )
    LaunchedEffect(permissionState.status) {
        Log.d("tanmay", "SmsPermissionHandler: " + permissionState.status.isGranted)
        if (permissionState.status.isGranted) {
            onPermissionGranted()
        }
    }

    if (!permissionState.status.isGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Allow SMS Access")
            }
        }
    }
}