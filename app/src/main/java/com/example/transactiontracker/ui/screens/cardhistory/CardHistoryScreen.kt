package com.example.transactiontracker.ui.screens.cardhistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.transactiontracker.R
import com.example.transactiontracker.sms.model.CashBackCategory
import com.example.transactiontracker.sms.model.TransactionType
import com.example.transactiontracker.ui.screens.components.EmptyView
import com.example.transactiontracker.ui.screens.components.LoadingView
import com.example.transactiontracker.utils.DateUtils
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CardHistoryScreen(
    cardLast4: String,
    bankName: String,
    onBackClickPress: () -> Unit,
    state: CardHistoryUiState,
    onDelete: (CardTransactionUi) -> Unit,
    onUpdate: (CardTransactionUi) -> Unit
) {
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
                            Image(
                                painter = painterResource(id = R.drawable.outline_align_flex_center_24),
                                contentDescription = "Back Press",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clickable(onClick = {
                                        onBackClickPress()
                                    })
                            )
                            Text(
                                bankName + " - " + cardLast4,
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
                    state.groupedTransaction.forEach { (month, txns) ->

                        item {
                            MonthlyHeader(month, txns)
                        }

                        items(
                            txns,
                        ) { transaction ->
                            IndividualCardTransaction(
                                transaction = transaction,
                                onDeleteClick = {
                                    onDelete(transaction)
                                },
                                onLongClick = {
                                    selectedSms = transaction.sms
                                    showDialog = true
                                },
                                updateCashbackPoints = {
                                    onUpdate(it)
                                }
                            )

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

@Composable
private fun MonthlyHeader(
    month: String,
    txns: List<CardTransactionUi>
) {
    val total = txns.sumOf {
        if (it.type != TransactionType.CREDIT) it.amount else 0
    }

    val cashbackTotal = txns
        .groupBy { it.cashBackCategory }
        .values
        .sumOf { categoryTxns ->
            minOf(categoryTxns.sumOf { it.cashback }, 2000)
        }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$total",
                /*text = buildAnnotatedString {
                        withStyle(style = SpanStyle()){ append(total.toString())}
                        withStyle(style = SpanStyle(color = Color(0xFF4CAF50),
                            fontSize = 15.sp)){ append("($cashbackTotal)")}

                } ,*/
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )

            if (cashbackTotal > 0) {
                Text(
                    text = "($cashbackTotal)",
                    color = Color(0xFF4CAF50),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IndividualCardTransaction(
    transaction: CardTransactionUi,
    onDeleteClick: () -> Unit,
    onLongClick: () -> Unit,
    updateCashbackPoints: (CardTransactionUi) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .combinedClickable(
                onClick = onLongClick,
                onLongClick = onDeleteClick
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

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = transaction.merchant,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row() {
                    Text(
                        text = DateUtils.formatDate(transaction.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (transaction.cashback > 0)
                        Text(modifier = Modifier.clickable(
                            onClick = {
                                updateCashbackPoints(transaction)
                            }
                        ),
                            text = "* ${transaction.cashback}",
                            color = Color(0xFF4CAF50),
                            textAlign = TextAlign.Center
                        )
                }
            }

            Text(
                text = if (transaction.type == TransactionType.DEBIT)
                    "₹${abs(transaction.amount)}"
                else
                    "+₹${abs(transaction.amount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (transaction.type != TransactionType.DEBIT)
                    Color(0xFF2E7D32)
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CardScreenRoute(
    cardLast4: String,
    bankName: String,
    onBackClickPress: () -> Unit,
    viewModel: CardHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CardHistoryScreen(
        cardLast4,
        bankName,
        onBackClickPress,
        state,
        onDelete = {
            viewModel.deleteEntryForThisCard(it)
        },
        onUpdate = {
            viewModel.updateCashbackPoints(it)
        }
    )
}

@Composable
@Preview
fun CardScreenPreview() {
    CardHistoryScreen(
        "1234",
        "hdfc",
        {},
        CardHistoryUiState(
            mapOf(
                "1" to listOf<CardTransactionUi>(
                    CardTransactionUi(
                        id = 1,
                        merchant = "test1",
                        amount = 100,
                        date = 1234567890,
                        type = TransactionType.DEBIT,
                        sms = "this is sms",
                        cashBackCategory = CashBackCategory.PHONE_PE,
                        cashback = 10
                    ),
                    CardTransactionUi(
                        id = 2,
                        merchant = "test2",
                        amount = 1000,
                        date = 1234567890,
                        type = TransactionType.DEBIT,
                        sms = "this is sms",
                        cashBackCategory = CashBackCategory.ONLINE,
                        cashback = 50
                    )
                )
            ),
            false
        ),
        {},{}
    )
}