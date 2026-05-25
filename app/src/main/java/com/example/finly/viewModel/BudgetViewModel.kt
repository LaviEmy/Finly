package com.example.finly.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finly.data.model.Category
import com.example.finly.data.model.Debt
import com.example.finly.data.model.Goal
import com.example.finly.data.model.Subscription
import com.example.finly.data.model.Transaction
import com.example.finly.data.model.TransactionType
import com.example.finly.data.repository.CategoryRepository
import com.example.finly.data.repository.DebtRepository
import com.example.finly.data.repository.GoalRepository
import com.example.finly.data.repository.SubscriptionRepository
import com.example.finly.data.repository.TransactionsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(private val transactionsRepository: TransactionsRepository,
                      private val categoryRepository: CategoryRepository,
                      private val goalRepository: GoalRepository,
                      private val debtRepository: DebtRepository,
                      private val subscriptionRepository: SubscriptionRepository) : ViewModel() {
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


    val goals: StateFlow<List<Goal>> = goalRepository.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())





    fun addGoal(name: String, targetAmount: Double, deadline: Long? = null) {
        viewModelScope.launch { goalRepository.insert(Goal(name = name, targetAmount = targetAmount, deadline = deadline)) }
    }
    fun updateGoal(goal: Goal) { viewModelScope.launch { goalRepository.update(goal) } }
    fun deleteGoal(goal: Goal) { viewModelScope.launch { goalRepository.delete(goal) } }

    // Долги
    val debts: StateFlow<List<Debt>> = debtRepository.getAllDebts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addDebt(name: String, totalAmount: Double, dueDate: Long? = null, isOwedToMe: Boolean = false) {
        viewModelScope.launch { debtRepository.insert(Debt(name = name, totalAmount = totalAmount, dueDate = dueDate)) }
    }
    fun updateDebt(debt: Debt) { viewModelScope.launch { debtRepository.update(debt) } }
    fun deleteDebt(debt: Debt) { viewModelScope.launch { debtRepository.delete(debt) } }

    // Подписки
    val subscriptions: StateFlow<List<Subscription>> = subscriptionRepository.getAllSubscriptions()
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSubscription(name: String, amount: Double, billingDay: Int, note: String = "") {
        viewModelScope.launch { subscriptionRepository.insert(Subscription(name = name, amount = amount, billingDay = billingDay)) }
    }
    fun deleteSubscription(sub: Subscription) { viewModelScope.launch { subscriptionRepository.delete(sub) } }
}


