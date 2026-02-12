package com.example.transactiontracker

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.transactiontracker.data.local.AppDatabase
import com.example.transactiontracker.ui.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.BLACK   // 👈 set background

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // 👈 dark icons
        }
        setContent {
            MaterialTheme {
                AppNavGraph()
            }
        }
    }

    /*override fun onStop() {
        super.onStop()
        Log.d("tanmay", "onDestroy: ")
        AppPreferences.setFirstSyncDone(this, false)
    }

    override fun onPause() {
        super.onPause()
        Log.d("Tanmay", "onPause: ")
        AppPreferences.setFirstSyncDone(this, false)

        lifecycleScope.launch {
            database.transactionDao().deleteAll()
        }
    }*/
}


