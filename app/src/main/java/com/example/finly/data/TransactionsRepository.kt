package com.example.finly.data

import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    fun getTransactionsByPeriod(startDate: Long, endDate: Long): Flow<List<Transaction>>

    fun getTotalsByCategory(type: String): Flow<List<CategoryTotal>>

    suspend fun insert(transaction: Transaction)

    suspend fun delete(transaction: Transaction)

    suspend fun update(transaction: Transaction)
}