package com.example.finly.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finly.data.model.TransactionType
import com.example.finly.R
import com.example.finly.viewModel.BudgetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    viewModel: BudgetViewModel,
    onDismiss: () -> Unit,
    defaultIsIncome: Boolean = true
) {
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(defaultIsIncome) }
    var amountError by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableIntStateOf(-1) }

    val categories by viewModel.getCategoriesForType(isIncome).collectAsState()
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_add_transaction_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isIncome,
                        onClick = { isIncome = true; selectedCategoryId = -1 },
                        label = { Text(stringResource(R.string.get_income)) }
                    )
                    FilterChip(
                        selected = !isIncome,
                        onClick = { isIncome = false; selectedCategoryId = -1 },
                        label = { Text(stringResource(R.string.get_expenses)) }
                    )
                }
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it; amountError = false },
                    label = { Text(stringResource(R.string.input_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = amountError,
                    supportingText = if (amountError) {
                        { Text(stringResource(R.string.error_invalid_amount)) }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.nameResId ?: stringResource(R.string.select_category),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.input_category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.nameResId) },
                                onClick = { selectedCategoryId = category.id; categoryExpanded = false }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amountDouble = amount.replace(",", ".").toDoubleOrNull()
                if (amountDouble == null || amountDouble <= 0) { amountError = true; return@Button }
                val categoryId = if (selectedCategoryId == -1) categories.firstOrNull()?.id ?: 0 else selectedCategoryId
                viewModel.addTransaction(
                    amountDouble,
                    if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE, categoryId
                )
                onDismiss()
            }) { Text(stringResource(R.string.btn_add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}



