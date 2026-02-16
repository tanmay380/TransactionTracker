package com.example.transactiontracker.ui.screens.homescreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.transactiontracker.sms.permissions.SmsPermissionHandler
import com.example.transactiontracker.sms.permissions.rememberSmsPermissionState
import com.example.transactiontracker.ui.screens.components.EmptyView
import com.example.transactiontracker.ui.screens.components.LoadingView
import com.google.accompanist.permissions.rememberPermissionState


@Composable
fun HomeScreen(
    onNavigateToTransaction: (Array<String>) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val (hasPermission, requestPermission) = rememberSmsPermissionState()

    /*SmsPermissionHandler {
        viewModel.onSmsPermissionGranted()
    }*/

    Log.d("tanmay", "HomeScreen: $hasPermission")

    LaunchedEffect(hasPermission) {
        if (hasPermission)
            viewModel.onSmsPermissionGranted()
    }

    if (!hasPermission){
        SmsPermissionHandler(requestPermission)
        Log.d("tanmay", "HomeScreen: inside has permission fale")
        return
    }

    when {
        state.isLoading -> LoadingView()
        state.cardWiseTotal.isEmpty() -> EmptyView("No transactions yet")
        else -> HomeContent(state, onNavigateToTransaction)
    }
}

@Composable
fun HomeContent(state: HomeUiState, onNavigateToTransaction: (Array<String>) -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            Box(
                modifier = Modifier.background(Color.Gray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Current Month Expenses",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    ) { i ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(i)
        ) {
            item {
                Text(
                    text = "${state.totalMonthlyExpense.toInt()}",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
//                    .clickable { onNavigateToTransaction() }
                )
            }

            items(state.cardWiseTotal) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp, 8.dp)
                        .clickable {
                            onNavigateToTransaction(arrayOf(it.bankName, it.cardNumber))
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = it.bankName + " -- " + it.cardNumber)
                        Text(text = "${it.totalExpense.toInt()}")
                    }
                }
            }
        }
    }
}
