package com.example.transactiontracker

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.transactiontracker.data.local.AppDatabase
import com.example.transactiontracker.ui.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: AppDatabase

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var onPermissionResult: ((Boolean, Boolean, Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.BLACK   // 👈 set background

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 👈 dark icons
        }

        permissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permission ->
                val smsPermission = permission[Manifest.permission.READ_SMS] ?: false
                val notificationPermission =
                    permission[Manifest.permission.POST_NOTIFICATIONS] ?: false
                val receiveSms = permission[Manifest.permission.RECEIVE_SMS] ?: false

                onPermissionResult?.invoke(smsPermission, notificationPermission, receiveSms)


            }

        setContent {
            MaterialTheme {
                AppNavGraph(
                    requestPermission = { it ->
                        onPermissionResult = it
                        val permissionList = mutableListOf(
                            Manifest.permission.READ_SMS,
                            Manifest.permission.POST_NOTIFICATIONS,
                            Manifest.permission.RECEIVE_SMS
                        )
                        permissionLauncher.launch(permissionList.toTypedArray())
                    }
                )
            }
        }
    }
}



