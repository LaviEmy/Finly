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
import com.example.finly.R
import com.example.finly.viewModel.BudgetViewModel


@Composable
fun IncomeScreen(viewModel: BudgetViewModel) {
    val incomeTransactions by viewModel.incomeTransactions.collectAsState()

    // 1. ДОБАВИЛИ ЭТУ СТРОЧКУ, чтобы экран доходов тоже знал все категории
    val categories by viewModel.allCategories.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_income))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.total_income), fontSize = 14.sp)
                        Text(
                            "+%.2f €".format(viewModel.totalIncome),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (incomeTransactions.isEmpty()) {
                item {
                    Box(modifier = Modifier.
                    fillMaxWidth().
                    padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.no_incomes),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(incomeTransactions) { transaction ->
                    // 2. ИЩЕМ КАТЕГОРИЮ ПО ID ТРАНЗАКЦИИ:
                    val category = categories.find { it.id == transaction.categoryId }
                    val categoryName = category?.nameResId ?: "Other"

                    // 3. ПЕРЕДАЕМ ИМЯ В КАРТОЧКУ!
                    TransactionCard(
                        transaction = transaction,
                        categoryName = categoryName,
                        onDelete = { viewModel.deleteTransaction(transaction) }
                    )
                }
            }
        }
    }
    if (showDialog) { AddTransactionDialog(viewModel = viewModel, onDismiss = { showDialog = false }, defaultIsIncome = true) }
}