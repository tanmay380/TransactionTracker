package com.example.transactiontracker.ui.screens.cardhistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.transactiontracker.sms.model.TransactionType
import com.example.transactiontracker.ui.screens.components.EmptyView
import com.example.transactiontracker.ui.screens.components.LoadingView
import com.example.transactiontracker.utils.DateUtils

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CardHistoryScreen(
    cardLast4: String,
    viewModel: CardHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember {
        mutableStateOf(false)
    }
    var selectedSms by remember {
        mutableStateOf("")
    }

    when {
        state.isLoading -> LoadingView()
        state.groupedTransaction.isEmpty() -> EmptyView("No transactions")
        else -> {
            Scaffold() { i ->
                LazyColumn {
                    state.groupedTransaction.forEach { (month, txns) ->

                        item {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val total = txns.sumOf {
                                    if (it.type == TransactionType.DEBIT)
                                        it.amount
                                    else
                                        -it.amount
                                }
                                Text(
                                    text = month,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                                Text(
                                    text = total.toString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        items(
                            txns,
                        ) { transaction ->
                            /*Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp, 8.dp)
                                    .combinedClickable(
                                        onClick = {
                                            viewModel.deleteEntryForThisCard(txn)
                                        },
                                        onLongClick = {
                                            showDialog = true
                                            selectedSms = txn.sms
                                        }
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(DateUtils.formatDate(txn.date))
                                    Text(
                                        txn.merchant, textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text("₹${txn.amount}",
                                        maxLines = 1,
                                        softWrap = false)
                                }
                            }*/
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .combinedClickable(
                                        onClick = {
                                            viewModel.deleteEntryForThisCard(transaction)
                                        },
                                        onLongClick = {
                                            showDialog = true
                                            selectedSms = transaction.sms
                                        }
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = transaction.merchant,
                                            style = MaterialTheme.typography.bodyLarge,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = DateUtils.formatDate(transaction.date),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }

                                    Text(
                                        text = if (transaction.type == TransactionType.DEBIT){
                                            "₹${transaction.amount}"
                                        }
                                        else {
                                            "+₹${transaction.amount}"
                                             },
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (transaction.type == TransactionType.CREDIT){
                                            Color(0xFF2E7D32)
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                }
                            }

                        }
                    }
                }
                if (showDialog) {
                    BasicAlertDialog(
                        onDismissRequest = { showDialog = false },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = AlertDialogDefaults.TonalElevation
                        ) {
                            Text(
                                selectedSms,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}