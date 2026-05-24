package com.example.finly.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finly.data.model.Category
import com.example.finly.data.model.Transaction
import com.example.finly.data.model.TransactionType
import com.example.finly.data.repository.CategoryRepository
import com.example.finly.data.repository.TransactionsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(private val transactionsRepository: TransactionsRepository, private val categoryRepository: CategoryRepository) : ViewModel() {
    val transactions: StateFlow<List<Transaction>> = transactionsRepository
        .getAllTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val incomeTransactions: StateFlow<List<Transaction>> = transactionsRepository
        .getTransactionsByType(TransactionType.INCOME.name)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val expenseTransactions: StateFlow<List<Transaction>> = transactionsRepository
        .getTransactionsByType(TransactionType.EXPENSE.name)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allCategories: StateFlow<List<Category>> = categoryRepository
        .getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val balance: Double
        get() = transactions.value.sumOf {
            if (it.type == TransactionType.INCOME) it.amount else -it.amount
        }

    val totalIncome: Double
        get() = incomeTransactions.value.sumOf { it.amount }

    val totalExpense: Double
        get() = expenseTransactions.value.sumOf { it.amount }

    fun addTransaction(amount: Double, type: TransactionType, categoryId: Int, note: String = "") {
        viewModelScope.launch {
            transactionsRepository.insert(
                Transaction(amount = amount, type = type, categoryId = categoryId, note = note)
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { transactionsRepository.delete(transaction) }
    }

    fun addCategory(name: String, isForIncome: Boolean) {
        viewModelScope.launch {
            categoryRepository.insert(Category(nameResId = name, isForIncome = isForIncome, isDefault = false))
        }
    }

    fun getCategoriesForType(isForIncome: Boolean): StateFlow<List<Category>> =
        categoryRepository.getCategoriesByType(isForIncome)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


}