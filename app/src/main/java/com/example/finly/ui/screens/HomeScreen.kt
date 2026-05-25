package com.example.finly.ui.screens

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.example.finly.R
import com.example.finly.data.model.TransactionType
import com.example.finly.viewModel.BudgetViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun HomeScreen(viewModel: BudgetViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val categories by viewModel.allCategories.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

    val expensesByCategory = transactions
        .filter { it.type == TransactionType.EXPENSE }
        .groupBy { it.categoryId }
        .mapValues { (_, list) -> list.sumOf { it.amount } }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.btn_add))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(160.dp)) {
                        AndroidView(
                            factory = { ctx -> PieChart(ctx).apply {
                                holeRadius = 65f; transparentCircleRadius = 0f
                                description.isEnabled = false;
                                legend.isEnabled = false
                            }},
                            update = { chart ->
                                val entries = mutableListOf<PieEntry>()
                                val chartColors = mutableListOf<Int>()

                                if (viewModel.totalIncome <= 0.0 && expensesByCategory.isEmpty()) {
                                    // Если денег нет вообще — рисуем пустую серую баранку
                                    entries.add(PieEntry(1f, ""))
                                    chartColors.add(AndroidColor.parseColor("#333333")) // Темно-серый для пустоты
                                } else {
                                    // 1. Добавляем реальные траты по категориям
                                    expensesByCategory.forEach { (catId, amount) ->
                                        entries.add(PieEntry(amount.toFloat(), ""))
                                        val hexColor = categories.find { it.id == catId }?.color ?: "#B0BEC5"
                                        chartColors.add(hexColor.toColorInt())
                                    }

                                    // 2. ДОБАВЛЯЕМ ОСТАТОК (БАЛАНС)!
                                    // Именно он не даст одной трате занять весь круг
                                    if (viewModel.balance > 0) {
                                        entries.add(PieEntry(viewModel.balance.toFloat(), ""))
                                        // Цвет остатка (свободных денег) делаем светло-серым
                                        chartColors.add(AndroidColor.parseColor("#E0E0E0"))
                                    }
                                }

                                chart.data = PieData(PieDataSet(entries, "").apply {
                                    this.colors = chartColors
                                    setDrawValues(false)
                                    // Убираем промежутки между кусками, чтобы смотрелось монолитно
                                    sliceSpace = 2f
                                })
                                chart.setTouchEnabled(expensesByCategory.isNotEmpty())
                                chart.invalidate()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "%.0f€".format(viewModel.balance),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                stringResource(R.string.get_balance),
                                fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column {
                            // Income
                            Text(
                                stringResource(R.string.get_income),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "+%.2f €".format(totalIncome),
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Column {
                            // Expenses
                            Text(
                                stringResource(R.string.get_expenses),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "-%.2f €".format(totalExpense),
                                color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            if (expensesByCategory.isNotEmpty()) {
                item {
                    Text(
                        stringResource(R.string.get_expenses_by_category),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
                items(expensesByCategory.entries.toList()) { (catId, amount) ->
                    val category = categories.find { it.id == catId }
                    val categoryName = category?.nameResId ?: "Other"
                    val hexColor = category?.color ?: "#B0BEC5"
                    val composeColor = Color(hexColor.toColorInt())
                    val percent = if (totalExpense > 0) (amount / totalExpense * 100).toInt() else 0

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(composeColor))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(categoryName)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                "$percent%",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "%.2f €".format(amount),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            item {
                // All transactions
                Text(stringResource(R.string.get_all_transactions), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
            }

            if (transactions.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_transaction), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(transactions) { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    val categoryName = category?.nameResId ?: "Other"
                    TransactionCard(
                        transaction = transaction,
                        categoryName = categoryName,
                        onDelete = { viewModel.deleteTransaction(transaction) }
                    )
                }
            }
        }
    }
    if (showDialog) { AddTransactionDialog(viewModel = viewModel, onDismiss = { showDialog = false }) }
}