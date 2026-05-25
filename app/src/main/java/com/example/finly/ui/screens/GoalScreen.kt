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
import com.example.finly.data.model.Goal
import com.example.finly.viewModel.BudgetViewModel

@Composable
fun GoalsScreen(viewModel: BudgetViewModel) {
    val goals by viewModel.goals.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_goal))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (goals.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.no_goals),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(goals) { goal ->
                    GoalCard(goal = goal,
                        onDelete = { viewModel.deleteGoal(goal) },
                        onAddProgress = { amount -> viewModel.updateGoal(goal.copy(currentAmount = goal.currentAmount + amount)) })
                }
            }
        }
    }
    if (showDialog) {
        AddGoalDialog(onDismiss = { showDialog = false }, onConfirm = { name, target -> viewModel.addGoal(name, target) })
    }
}

@Composable
fun GoalCard(goal: Goal, onDelete: () -> Unit, onAddProgress: (Double) -> Unit) {
    val progress = if (goal.targetAmount > 0) (goal.currentAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f) else 0f
    var showProgressDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(goal.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                color = MaterialTheme.colorScheme.primary // Цвет берется из темы
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("%.2f €".format(goal.currentAmount), fontSize = 13.sp)
                Text("%.2f €".format(goal.targetAmount), fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { showProgressDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.add_progress))
            }
        }
    }
    if (showProgressDialog) {
        AddAmountDialog(stringResource(R.string.add_progress),
            onDismiss = { showProgressDialog = false },
            onConfirm = { onAddProgress(it); showProgressDialog = false }
        )
    }
}

@Composable
fun AddGoalDialog(onDismiss: () -> Unit, onConfirm: (String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(stringResource(R.string.new_goal)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it },
                    label = { Text(stringResource(R.string.goal_name)) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = target, onValueChange = { target = it },
                    label = { Text(stringResource(R.string.goal_target)) }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                val t = target.replace(",", ".").toDoubleOrNull() ?: return@Button
                onConfirm(name, t); onDismiss()
            }) { Text(stringResource(R.string.btn_create)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.btn_cancel)) } })
}

@Composable
fun AddAmountDialog(title: String, onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(title) },
        text = {
            OutlinedTextField(value = amount, onValueChange = { amount = it },
                label = { Text(stringResource(R.string.amount_euro)) }, modifier = Modifier.fillMaxWidth())
        },
        confirmButton = {
            Button(onClick = {
                val a = amount.replace(",", ".").toDoubleOrNull() ?: return@Button
                onConfirm(a)
            }) { Text(stringResource(R.string.btn_add)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.btn_cancel)) } })
}