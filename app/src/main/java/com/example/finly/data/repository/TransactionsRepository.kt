package com.example.finly.data.repository

import com.example.finly.data.model.Transaction
import com.example.finly.data.dao.CategoryTotal
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    fun getTransactionsByPeriod(startDate: Long, endDate: Long): Flow<List<Transaction>>

    fun getTotalsByCategory(type: String): Flow<List<CategoryTotal>>

    suspend fun insert(transaction: Transaction)

    suspend fun delete(transaction: Transaction)

    suspend fun update(transaction: Transaction)
}