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
import com.example.finly.data.model.Subscription
import com.example.finly.viewModel.BudgetViewModel


@Composable
fun SubscriptionsScreen(viewModel: BudgetViewModel) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val totalMonthly = subscriptions.sumOf { it.amount }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_subscription))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.per_month), fontSize = 14.sp)
                        Text("-%.2f €".format(totalMonthly), fontSize = 24.sp,
                            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (subscriptions.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.no_subscriptions),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(subscriptions) { sub ->
                    SubscriptionCard(sub = sub, onDelete = { viewModel.deleteSubscription(sub) })
                }
            }
        }
    }
    if (showDialog) {
        AddSubscriptionDialog(onDismiss = { showDialog = false },
            onConfirm = { name, amount, day -> viewModel.addSubscription(name, amount, day) })
    }
}

@Composable
fun SubscriptionCard(sub: Subscription, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(sub.name, fontWeight = FontWeight.Medium)
                Text(
                    stringResource(R.string.billing_day_format).format(sub.billingDay),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("-%.2f €".format(sub.amount), fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.btn_delete),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AddSubscriptionDialog(onDismiss: () -> Unit, onConfirm: (String, Double, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var billingDay by remember { mutableStateOf("1") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text(stringResource(R.string.new_subscription)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it },
                    label = { Text(stringResource(R.string.goal_name)) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = amount, onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.sub_amount_month)) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = billingDay, onValueChange = { billingDay = it },
                    label = { Text(stringResource(R.string.sub_billing_day)) }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                val a = amount.replace(",", ".").toDoubleOrNull() ?: return@Button
                val d = billingDay.toIntOrNull()?.coerceIn(1, 31) ?: 1
                onConfirm(name, a, d); onDismiss()
            }) { Text(stringResource(R.string.btn_add)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.btn_cancel)) } })
}