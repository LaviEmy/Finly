package com.example.finly.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finly.viewModel.BudgetViewModel
import com.example.finly.R

@Composable
fun ExpensesScreen(viewModel: BudgetViewModel) {
    val expenseTransactions by viewModel.expenseTransactions.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_expense))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            stringResource(R.string.total_expenses),
                            fontSize = 14.sp
                        )
                        Text(
                            "-%.2f €".format(viewModel.totalExpense),
                            fontSize = 28.sp, fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (expenseTransactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.
                        fillMaxWidth().
                        padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.no_expenses),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(expenseTransactions) { transaction ->
                    TransactionCard(transaction = transaction, onDelete = { viewModel.deleteTransaction(transaction) })
                }
            }
        }
    }
    if (showDialog) { AddTransactionDialog(viewModel = viewModel, onDismiss = { showDialog = false }, defaultIsIncome = false) }
}