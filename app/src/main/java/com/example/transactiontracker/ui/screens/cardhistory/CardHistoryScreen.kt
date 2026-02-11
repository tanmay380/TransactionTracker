package com.example.transactiontracker.ui.screens.cardhistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.transactiontracker.sms.model.TransactionType
import com.example.transactiontracker.ui.screens.components.EmptyView
import com.example.transactiontracker.ui.screens.components.LoadingView
import com.example.transactiontracker.utils.DateUtils

@Composable
fun CardHistoryScreen(
    cardLast4: String,
    viewModel: CardHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        state.isLoading -> LoadingView()
        state.groupedTransaction.isEmpty() -> EmptyView("No transactions")
        else -> {
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
                    ) { txn ->
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp, 8.dp)
                                .clickable {
                                    viewModel.deleteEntryForThisCard(txn)
                                }
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
                                Text("₹${txn.amount}")
                            }
                        }
                    }
                }
            }
        }
    }
}
