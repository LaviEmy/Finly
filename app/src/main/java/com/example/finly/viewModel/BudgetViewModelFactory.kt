package com.example.finly.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finly.data.repository.CategoryRepository
import com.example.finly.data.repository.DebtRepository
import com.example.finly.data.repository.GoalRepository
import com.example.finly.data.repository.SubscriptionRepository
import com.example.finly.data.repository.TransactionsRepository


class BudgetViewModelFactory
    (private val transactionsRepository: TransactionsRepository,
     private val categoryRepository: CategoryRepository,
            private val goalRepository: GoalRepository,
            private val debtRepository: DebtRepository,
            private val subsriptionRepository: SubscriptionRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModelFactory(transactionsRepository, categoryRepository,
                goalRepository, debtRepository, subsriptionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}