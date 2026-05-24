package com.example.finly.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finly.data.dao.CategoryDao
import com.example.finly.data.repository.CategoryRepository
import com.example.finly.data.repository.TransactionsRepository


class BudgetViewModelFactory(private val transactionsRepository: TransactionsRepository, private val categoryRepository: CategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModelFactory(transactionsRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}