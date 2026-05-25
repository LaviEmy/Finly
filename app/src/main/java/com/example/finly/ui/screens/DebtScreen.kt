package com.example.finly.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finly.R
import com.example.finly.data.model.Debt
import com.example.finly.viewModel.BudgetViewModel


@Composable
fun DebtsScreen(viewModel: BudgetViewModel) {
    val debts by viewModel.debts.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_debt))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (debts.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.no_debts),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(debts) { debt ->
                    DebtCard(
                        debt = debt,
                        onDelete = { viewModel.deleteDebt(debt) },
                        onPay = { amount -> viewModel.updateDebt(debt.copy(paidAmount = debt.paidAmount + amount)) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddDebtDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, amount -> viewModel.addDebt(name, amount) }
        )
    }
}

@Composable
fun DebtCard(debt: Debt, onDelete: () -> Unit, onPay: (Double) -> Unit) {
    val remaining = debt.totalAmount - debt.paidAmount
    val progress = if (debt.totalAmount > 0) (debt.paidAmount / debt.totalAmount).toFloat().coerceIn(0f, 1f) else 0f
    var showPayDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(debt.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.btn_delete),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.debt_left)
                        .format(remaining), fontSize = 13.sp
                )
                Text(
                    stringResource(R.string.debt_total).format(debt.totalAmount),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { showPayDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.mark_paid))
            }
        }
    }

    if (showPayDialog) {
        AddAmountDialog(
            title = stringResource(R.string.mark_paid),
            onDismiss = { showPayDialog = false },
            onConfirm = { onPay(it); showPayDialog = false }
        )
    }
}

@Composable
fun AddDebtDialog(onDismiss: () -> Unit, onConfirm: (String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.new_debt)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.debt_person)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.amount_euro)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val a = amount.replace(",", ".").toDoubleOrNull() ?: return@Button
                onConfirm(name, a)
                onDismiss()
            }) { Text(stringResource(R.string.btn_add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.btn_cancel)) }
        }
    )
}